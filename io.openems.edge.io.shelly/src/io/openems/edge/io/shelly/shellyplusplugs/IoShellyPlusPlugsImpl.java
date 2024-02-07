package io.openems.edge.io.shelly.shellyplusplugs;

import java.util.Objects;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.event.propertytypes.EventTopics;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.openems.common.utils.JsonUtils;
import io.openems.edge.bridge.http.api.BridgeHttp;
import io.openems.edge.common.channel.BooleanWriteChannel;
import io.openems.edge.common.component.AbstractOpenemsComponent;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.io.api.DigitalOutput;
import io.openems.edge.meter.api.ElectricityMeter;
import io.openems.edge.meter.api.MeterType;
import io.openems.edge.meter.api.SinglePhase;
import io.openems.edge.meter.api.SinglePhaseMeter;
import io.openems.edge.timedata.api.Timedata;
import io.openems.edge.timedata.api.TimedataProvider;
import io.openems.edge.timedata.api.utils.CalculateEnergyFromPower;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "IO.Shelly.PlusPlugS", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE//
)
@EventTopics({ //
		EdgeEventConstants.TOPIC_CYCLE_EXECUTE_WRITE, //
		EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE //
})
public class IoShellyPlusPlugsImpl extends AbstractOpenemsComponent implements IoShellyPlusPlugs, DigitalOutput,
		SinglePhaseMeter, ElectricityMeter, OpenemsComponent, TimedataProvider, EventHandler {

	private final CalculateEnergyFromPower calculateProductionEnergy = new CalculateEnergyFromPower(this,
			ElectricityMeter.ChannelId.ACTIVE_PRODUCTION_ENERGY);
	private final CalculateEnergyFromPower calculateConsumptionEnergy = new CalculateEnergyFromPower(this,
			ElectricityMeter.ChannelId.ACTIVE_CONSUMPTION_ENERGY);

	private final Logger log = LoggerFactory.getLogger(IoShellyPlusPlugsImpl.class);
	private final BooleanWriteChannel[] digitalOutputChannels;

	private MeterType meterType = null;
	private SinglePhase phase = null;
	private String baseUrl;

	private volatile Timedata timedata;

	@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED)
	private BridgeHttp httpBridge;

	public IoShellyPlusPlugsImpl() {
		super(//
				OpenemsComponent.ChannelId.values(), //
				ElectricityMeter.ChannelId.values(), //
				DigitalOutput.ChannelId.values(), //
				IoShellyPlusPlugs.ChannelId.values() //
		);
		this.digitalOutputChannels = new BooleanWriteChannel[] { //
				this.channel(IoShellyPlusPlugs.ChannelId.RELAY) //
		};

		SinglePhaseMeter.calculateSinglePhaseFromActivePower(this);
	}

	@Activate
	private void activate(ComponentContext context, Config config) {
		super.activate(context, config.id(), config.alias(), config.enabled());
		this.meterType = config.type();
		this.phase = config.phase();
		this.baseUrl = "http://" + config.ip();

		if (!this.isEnabled()) {
			return;
		}

		this.httpBridge.subscribeJsonEveryCycle(this.baseUrl + "/rpc/Shelly.GetStatus", this::processHttpResult);
	}

	@Override
	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	public BooleanWriteChannel[] digitalOutputChannels() {
		return this.digitalOutputChannels;
	}

	@Override
	public String debugLog() {
		var b = new StringBuilder();
		var valueOpt = this.getRelayChannel().value().asOptional();
		if (valueOpt.isPresent()) {
			b.append(valueOpt.get() ? "ON" : "OFF");
		} else {
			b.append("Unknown");
		}
		b.append("|");
		b.append(this.getActivePowerChannel().value().asString());

		return b.toString();
	}

	@Override
	public void handleEvent(Event event) {
		if (!this.isEnabled()) {
			return;
		}

		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE -> this.calculateEnergy();
		case EdgeEventConstants.TOPIC_CYCLE_EXECUTE_WRITE -> {
			this.executeWrite(this.getRelayChannel(), 0);

		}
		}
	}

	private void processHttpResult(JsonElement result, Throwable error) {
		this._setSlaveCommunicationFailed(result == null);
		if (error != null) {
			this._setRelay(null);
			this._setActivePower(null);
			this.logDebug(this.log, error.getMessage());
			return;
		}

		try {
			JsonObject jsonResponse = JsonUtils.getAsJsonObject(result);
			JsonObject switch0 = JsonUtils.getAsJsonObject(jsonResponse, "switch:0");
			if (switch0 != null) {
				boolean relayIson = JsonUtils.getAsBoolean(switch0, "output");
				float power = JsonUtils.getAsFloat(switch0, "apower");
				int voltage = JsonUtils.getAsInt(switch0, "voltage");
				int current = JsonUtils.getAsInt(switch0, "current");

				this._setRelay(relayIson);
				this._setActivePower(Math.round(power));

				int millivolt = voltage * 1000;
				int milliamp = current * 1000;

				if (this.phase != null) {
					switch (this.phase) {
					case L1:
						this._setVoltageL1(millivolt);
						this._setCurrentL1(milliamp);
						this._setVoltageL2(0);
						this._setCurrentL2(0);
						this._setVoltageL3(0);
						this._setCurrentL3(0);
						this._setActivePowerL2(0);
						this._setActivePowerL3(0);
						break;
					case L2:
						this._setVoltageL2(millivolt);
						this._setCurrentL2(milliamp);
						this._setVoltageL1(0);
						this._setCurrentL1(0);
						this._setVoltageL3(0);
						this._setCurrentL3(0);
						this._setActivePowerL1(0);
						this._setActivePowerL3(0);
						break;
					case L3:
						this._setVoltageL3(millivolt);
						this._setCurrentL3(milliamp);
						this._setVoltageL1(0);
						this._setCurrentL1(0);
						this._setVoltageL2(0);
						this._setCurrentL2(0);
						this._setActivePowerL1(0);
						this._setActivePowerL2(0);
						break;
					}
				}
			}
		} catch (Exception e) {
			this._setRelay(null);
			this._setActivePower(null);
			this.logDebug(this.log, e.getMessage());
		}
	}

	/**
	 * Execute on Cycle Event "Execute Write".
	 * 
	 * @param channel write channel
	 * @param index   index
	 */
	private void executeWrite(BooleanWriteChannel channel, int index) {
		var readValue = channel.value().get();
		var writeValue = channel.getNextWriteValueAndReset();
		if (writeValue.isEmpty()) {
			// no write value
			return;
		}
		if (Objects.equals(readValue, writeValue.get())) {
			// read value = write value
			return;
		}
		final var url = this.baseUrl + "/relay/" + index + "?turn=" + (writeValue.get() ? "on" : "off");
		this.httpBridge.request(url).whenComplete((t, e) -> {
			this._setSlaveCommunicationFailed(e != null);
		});
	}

	/**
	 * Calculate the Energy values from ActivePower.
	 */
	private void calculateEnergy() {
		// Calculate Energy
		final var activePower = this.getActivePower().get();
		if (activePower == null) {
			this.calculateProductionEnergy.update(null);
			this.calculateConsumptionEnergy.update(null);
		} else if (activePower >= 0) {
			this.calculateProductionEnergy.update(activePower);
			this.calculateConsumptionEnergy.update(0);
		} else {
			this.calculateProductionEnergy.update(0);
			this.calculateConsumptionEnergy.update(-activePower);
		}
	}

	@Override
	public MeterType getMeterType() {
		return this.meterType;
	}

	@Override
	public SinglePhase getPhase() {
		return this.phase;
	}

	@Override
	public Timedata getTimedata() {
		return this.timedata;
	}

}
