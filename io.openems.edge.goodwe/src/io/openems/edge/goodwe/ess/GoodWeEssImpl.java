package io.openems.edge.goodwe.ess;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.service.metatype.annotations.Designate;

import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.edge.bridge.modbus.api.BridgeModbus;
import io.openems.edge.common.channel.Channel;
import io.openems.edge.common.component.ComponentManager;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.type.TypeUtils;
import io.openems.edge.ess.api.HybridEss;
import io.openems.edge.ess.api.ManagedSymmetricEss;
import io.openems.edge.ess.api.SymmetricEss;
import io.openems.edge.ess.power.api.Constraint;
import io.openems.edge.ess.power.api.Phase;
import io.openems.edge.ess.power.api.Power;
import io.openems.edge.ess.power.api.Pwr;
import io.openems.edge.ess.power.api.Relationship;
import io.openems.edge.goodwe.common.AbstractGoodWe;
import io.openems.edge.goodwe.common.ApplyPowerHandler;
import io.openems.edge.goodwe.common.GoodWe;
import io.openems.edge.timedata.api.Timedata;
import io.openems.edge.timedata.api.TimedataProvider;
import io.openems.edge.timedata.api.utils.CalculateEnergyFromPower;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "GoodWe.Ess", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = { //
				EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE, //
				EventConstants.EVENT_TOPIC + "=" + EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE //
		}) //
public class GoodWeEssImpl extends AbstractGoodWe implements GoodWeEss, GoodWe, HybridEss, ManagedSymmetricEss,
		SymmetricEss, OpenemsComponent, TimedataProvider, EventHandler {

	private final AllowedChargeDischargeHandler allowedChargeDischargeHandler = new AllowedChargeDischargeHandler(this);
	private Config config;

	@Reference(policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.OPTIONAL)
	private volatile Timedata timedata = null;

	@Reference
	protected ConfigurationAdmin cm;

	@Reference
	protected ComponentManager componentManager;

	@Reference
	private Power power;

	private final CalculateEnergyFromPower calculateAcChargeEnergy = new CalculateEnergyFromPower(this,
			SymmetricEss.ChannelId.ACTIVE_CHARGE_ENERGY);
	private final CalculateEnergyFromPower calculateAcDischargeEnergy = new CalculateEnergyFromPower(this,
			SymmetricEss.ChannelId.ACTIVE_DISCHARGE_ENERGY);

	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
	protected void setModbus(BridgeModbus modbus) {
		super.setModbus(modbus);
	}

	@Activate
	void activate(ComponentContext context, Config config) throws OpenemsNamedException {
		this.config = config;
		if (super.activate(context, config.id(), config.alias(), config.enabled(), config.modbusUnitId(), this.cm,
				"Modbus", config.modbus_id())) {
			return;
		}
		this._setCapacity(this.config.capacity());
	}

	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	public GoodWeEssImpl() throws OpenemsNamedException {
		super(//
				OpenemsComponent.ChannelId.values(), //
				SymmetricEss.ChannelId.values(), //
				ManagedSymmetricEss.ChannelId.values(), //
				HybridEss.ChannelId.values(), //
				GoodWe.ChannelId.values(), //
				GoodWeEss.ChannelId.values() //
		);
	}

	@Override
	public void applyPower(int activePower, int reactivePower) throws OpenemsNamedException {
		// Calculate ActivePower and Energy values.
		this.updatechannels();

		// Apply Power Set-Point
		ApplyPowerHandler.apply(this, false /* read-only mode is never true */, activePower);
	}

	@Override
	public String debugLog() {
		return "SoC:" + this.getSoc().asString() //
				+ "|L:" + this.getActivePower().asString() //
				+ "|" + this.getGridModeChannel().value().asOptionString()//
				+ "|Allowed:" + this.getAllowedChargePower().asStringWithoutUnit() + ";"
				+ this.getAllowedDischargePower().asString();
	}

	@Override
	public void handleEvent(Event event) {
		if (!this.isEnabled()) {
			return;
		}

		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_BEFORE_PROCESS_IMAGE:
			this.updatechannels();
			break;
		case EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE:
			this.allowedChargeDischargeHandler.accept(componentManager);
			break;
		}
	}

	private void updatechannels() {
		/*
		 * Update ActivePower from P_BATTERY1 and chargers ACTUAL_POWER
		 */
		Integer productionPower = this.calculatePvProduction();
		final Channel<Integer> batteryPower = this.channel(GoodWe.ChannelId.P_BATTERY1);
		Integer activePower = TypeUtils.sum(productionPower, batteryPower.value().get());
		this._setActivePower(activePower);

		/*
		 * Calculate AC Energy
		 */
		if (activePower == null) {
			// Not available
			this.calculateAcChargeEnergy.update(null);
			this.calculateAcDischargeEnergy.update(null);
		} else if (activePower > 0) {
			// Discharge
			this.calculateAcChargeEnergy.update(0);
			this.calculateAcDischargeEnergy.update(activePower);
		} else {
			// Charge
			this.calculateAcChargeEnergy.update(activePower * -1);
			this.calculateAcDischargeEnergy.update(0);
		}

		/*
		 * Update Allowed charge and Allowed discharge
		 */

		Integer soc = this.getSoc().get();
		Integer maxBatteryPower = this.config.maxBatteryPower();

		Integer allowedCharge = null;
		Integer allowedDischarge = null;

		if (productionPower == null) {
			productionPower = 0;
		}

		if (soc == null) {

			allowedCharge = 0;
			allowedDischarge = 0;

		} else if (soc == 100) {

			allowedDischarge = maxBatteryPower + productionPower;
			allowedCharge = 0;

		} else if (soc > 0) {

			allowedDischarge = maxBatteryPower + productionPower;
			allowedCharge = maxBatteryPower;

		} else if (soc == 0) {

			allowedDischarge = productionPower;
			allowedCharge = maxBatteryPower;

		}

		// to avoid charging when production is greater than maximum battery power.
		if (allowedCharge < 0) {
			allowedCharge = 0;
		}

		this._setAllowedChargePower(TypeUtils.multiply(allowedCharge * -1));
		this._setAllowedDischargePower(allowedDischarge);
	}

	@Override
	public Power getPower() {
		return this.power;
	}

	@Override
	public int getPowerPrecision() {
		return 1;
	}

	@Override
	public Constraint[] getStaticConstraints() throws OpenemsNamedException {
		// Handle Read-Only mode -> no charge/discharge
		if (this.config.readOnlyMode()) {
			return new Constraint[] { //
					this.createPowerConstraint("Read-Only-Mode", Phase.ALL, Pwr.ACTIVE, Relationship.EQUALS, 0), //
					this.createPowerConstraint("Read-Only-Mode", Phase.ALL, Pwr.REACTIVE, Relationship.EQUALS, 0) //
			};
		}

		return Power.NO_CONSTRAINTS;
	}

	@Override
	public Timedata getTimedata() {
		return this.timedata;
	}

	@Override
	public Integer getSurplusPower() {
		if (this.getSoc().orElse(0) < 99) {
			return null;
		}
		Integer productionPower = this.calculatePvProduction();
		if (productionPower == null || productionPower < 100) {
			return null;
		}
		return productionPower;
	}

}
