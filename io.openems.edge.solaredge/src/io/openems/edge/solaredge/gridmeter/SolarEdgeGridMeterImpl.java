package io.openems.edge.solaredge.gridmeter;

import java.util.Map;

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

import org.osgi.service.event.EventHandler;
import org.osgi.service.event.propertytypes.EventTopics;

import org.osgi.service.metatype.annotations.Designate;

import com.google.common.collect.ImmutableMap;

import io.openems.common.channel.AccessMode;
import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.bridge.modbus.api.BridgeModbus;
import io.openems.edge.bridge.modbus.api.ElementToChannelConverter;
import io.openems.edge.bridge.modbus.api.ModbusComponent;
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
import io.openems.edge.bridge.modbus.api.element.SignedWordElement;
import io.openems.edge.bridge.modbus.api.task.FC3ReadRegistersTask;
import io.openems.edge.bridge.modbus.sunspec.DefaultSunSpecModel;
import io.openems.edge.bridge.modbus.sunspec.SunSpecModel;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.event.EdgeEventConstants;
import io.openems.edge.common.modbusslave.ModbusSlave;
import io.openems.edge.common.modbusslave.ModbusSlaveNatureTable;
import io.openems.edge.common.modbusslave.ModbusSlaveTable;
import io.openems.edge.common.taskmanager.Priority;
import io.openems.edge.meter.api.ElectricityMeter;
import io.openems.edge.meter.api.MeterType;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "SolarEdge.Grid-Meter", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE, //
		property = { //
				"type=GRID" //
		})
@EventTopics({ //
		EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE, //
		EdgeEventConstants.TOPIC_CYCLE_BEFORE_CONTROLLERS //
})

public class SolarEdgeGridMeterImpl extends AbstractSolarEdgeGridmeter
		implements SolarEdgeGridMeter, ElectricityMeter, ModbusComponent, OpenemsComponent, ModbusSlave, EventHandler {

	private static final Map<SunSpecModel, Priority> ACTIVE_MODELS = ImmutableMap.<SunSpecModel, Priority>builder()
			.put(DefaultSunSpecModel.S_1, Priority.LOW) //
			.put(DefaultSunSpecModel.S_201, Priority.LOW) //
			.put(DefaultSunSpecModel.S_202, Priority.LOW) //
			.put(DefaultSunSpecModel.S_203, Priority.LOW) //
			.put(DefaultSunSpecModel.S_204, Priority.LOW) //
			.build();

	private static final int READ_FROM_MODBUS_BLOCK = 2;

	private Config config;

	public SolarEdgeGridMeterImpl() throws OpenemsException {
		super(//
				ACTIVE_MODELS, //
				OpenemsComponent.ChannelId.values(), //
				ModbusComponent.ChannelId.values(), //
				ElectricityMeter.ChannelId.values(), //
				SolarEdgeGridMeter.ChannelId.values());
		this.addStaticModbusTasks(this.getModbusProtocol());
	}

	private void addStaticModbusTasks(ModbusProtocol protocol) throws OpenemsException {
		protocol.addTask(//
				new FC3ReadRegistersTask(0x9D0E, Priority.HIGH, //

						m(SolarEdgeGridMeter.ChannelId.POWER, //
								new SignedWordElement(0x9D0E), ElementToChannelConverter.INVERT),
						m(SolarEdgeGridMeter.ChannelId.POWER_L1, //
								new SignedWordElement(0x9D0F), ElementToChannelConverter.INVERT),
						m(SolarEdgeGridMeter.ChannelId.POWER_L2, //
								new SignedWordElement(0x9D10), ElementToChannelConverter.INVERT),
						m(SolarEdgeGridMeter.ChannelId.POWER_L3, //
								new SignedWordElement(0x9D11), ElementToChannelConverter.INVERT),
						m(SolarEdgeGridMeter.ChannelId.POWER_SCALE, //
								new SignedWordElement(0x9D12)

						)));
	}

	@Reference
	protected ConfigurationAdmin cm;

	@Override
	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
	protected void setModbus(BridgeModbus modbus) {
		super.setModbus(modbus);
	}

	@Override
	public void handleEvent(Event event) {
		if (!this.isEnabled()) {
			return;
		}
		switch (event.getTopic()) {
		case EdgeEventConstants.TOPIC_CYCLE_AFTER_PROCESS_IMAGE:
			this._calculateAndSetActivePower();
			break;
		}
	}

	/**
	 * Aktuelle Erzeugung durch den Hybrid-WR ist der aktuelle Verbrauch +
	 * Batterie-Ladung/Entladung * -1
	 * 
	 * <p>Actual power from inverter comes from house consumption + battery inverter +
	 * power (*-1).
	 */
	public void _calculateAndSetActivePower() {

		int power = this.getPower().orElse(0);

		int powerScale = this.getPowerScale().orElse(0);

		double powerValue = power * Math.pow(10, powerScale);

		this._setActivePower((int) powerValue);

	}

	@Activate
	void activate(ComponentContext context, Config config) throws OpenemsException {
		if (super.activate(context, config.id(), config.alias(), config.enabled(), config.modbusUnitId(), this.cm,
				"Modbus", config.modbus_id(), READ_FROM_MODBUS_BLOCK)) {
			return;
		}
		this.config = config;
	}

	@Override
	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}

	@Override
	public MeterType getMeterType() {
		return this.config.type();
	}

	@Override
	public ModbusSlaveTable getModbusSlaveTable(AccessMode accessMode) {
		return new ModbusSlaveTable(//
				OpenemsComponent.getModbusSlaveNatureTable(accessMode), //
				ElectricityMeter.getModbusSlaveNatureTable(accessMode), //
				ModbusSlaveNatureTable.of(ElectricityMeter.class, accessMode, 100) //
						.build());
	}
}
