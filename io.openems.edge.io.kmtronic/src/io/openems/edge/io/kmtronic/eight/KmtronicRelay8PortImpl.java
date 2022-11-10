package io.openems.edge.io.kmtronic.eight;

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
import org.osgi.service.metatype.annotations.Designate;

import io.openems.common.channel.AccessMode;
import io.openems.common.exceptions.OpenemsException;
import io.openems.edge.bridge.modbus.api.BridgeModbus;
import io.openems.edge.bridge.modbus.api.ModbusComponent;
import io.openems.edge.bridge.modbus.api.ModbusProtocol;
import io.openems.edge.bridge.modbus.api.element.CoilElement;
import io.openems.edge.bridge.modbus.api.task.FC1ReadCoilsTask;
import io.openems.edge.bridge.modbus.api.task.FC5WriteCoilTask;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.common.modbusslave.ModbusSlave;
import io.openems.edge.common.modbusslave.ModbusSlaveNatureTable;
import io.openems.edge.common.modbusslave.ModbusSlaveTable;
import io.openems.edge.common.modbusslave.ModbusType;
import io.openems.edge.common.taskmanager.Priority;
import io.openems.edge.io.api.DigitalOutput;
import io.openems.edge.io.kmtronic.AbstractKmtronicRelay;

@Designate(ocd = Config.class, factory = true)
@Component(//
		name = "IO.KMtronic", //
		immediate = true, //
		configurationPolicy = ConfigurationPolicy.REQUIRE //
)
public class KmtronicRelay8PortImpl extends AbstractKmtronicRelay
		implements KmtronicRelay8Port, DigitalOutput, ModbusComponent, OpenemsComponent, ModbusSlave {

	@Reference
	protected ConfigurationAdmin cm;

	//TMP-Storage for Modbus-Prio
	public Priority prio;
	
	@Override
	@Reference(policy = ReferencePolicy.STATIC, policyOption = ReferencePolicyOption.GREEDY, cardinality = ReferenceCardinality.MANDATORY)
	protected void setModbus(BridgeModbus modbus) {
		super.setModbus(modbus);
	}

	public KmtronicRelay8PortImpl() {
		super(KmtronicRelay8Port.ChannelId.values());
	}

	@Activate
	void activate(ComponentContext context, Config config) throws OpenemsException {
		//Setting the Modbus-Prio in a OpenEMS-Readable Format
		if(config.mprio() == mprio.LOW) {
			this.prio = Priority.LOW;
		}else if(config.mprio() == mprio.HIGH){
			this.prio = Priority.HIGH;
		}
		//Normal Activate-Function
		if (super.activate(context, config.id(), config.alias(), config.enabled(), config.modbusUnitId(), this.cm,
				"Modbus", config.modbus_id())) {
			return;
		}
	}

	@Override
	@Deactivate
	protected void deactivate() {
		super.deactivate();
	}
	
	@Override
	protected ModbusProtocol defineModbusProtocol() throws OpenemsException {
		return new ModbusProtocol(this, //
				/*
				 * For Read: Read Coils
				 */
				
				new FC1ReadCoilsTask(0, this.prio, //
						m(KmtronicRelay8Port.ChannelId.RELAY_1, new CoilElement(0)), //
						m(KmtronicRelay8Port.ChannelId.RELAY_2, new CoilElement(1)), //
						m(KmtronicRelay8Port.ChannelId.RELAY_3, new CoilElement(2)), //
						m(KmtronicRelay8Port.ChannelId.RELAY_4, new CoilElement(3)), //
						m(KmtronicRelay8Port.ChannelId.RELAY_5, new CoilElement(4)), //
						m(KmtronicRelay8Port.ChannelId.RELAY_6, new CoilElement(5)), //
						m(KmtronicRelay8Port.ChannelId.RELAY_7, new CoilElement(6)), //
						m(KmtronicRelay8Port.ChannelId.RELAY_8, new CoilElement(7)) //
				),
				/*
				 * For Write: Write Single Coil
				 */
				new FC5WriteCoilTask(0, m(KmtronicRelay8Port.ChannelId.RELAY_1, new CoilElement(0))), //
				new FC5WriteCoilTask(1, m(KmtronicRelay8Port.ChannelId.RELAY_2, new CoilElement(1))), //
				new FC5WriteCoilTask(2, m(KmtronicRelay8Port.ChannelId.RELAY_3, new CoilElement(2))), //
				new FC5WriteCoilTask(3, m(KmtronicRelay8Port.ChannelId.RELAY_4, new CoilElement(3))), //
				new FC5WriteCoilTask(4, m(KmtronicRelay8Port.ChannelId.RELAY_5, new CoilElement(4))), //
				new FC5WriteCoilTask(5, m(KmtronicRelay8Port.ChannelId.RELAY_6, new CoilElement(5))), //
				new FC5WriteCoilTask(6, m(KmtronicRelay8Port.ChannelId.RELAY_7, new CoilElement(6))), //
				new FC5WriteCoilTask(7, m(KmtronicRelay8Port.ChannelId.RELAY_8, new CoilElement(7))) //
		);
	}

	@Override
	public ModbusSlaveTable getModbusSlaveTable(AccessMode accessMode) {
		return new ModbusSlaveTable(//
				OpenemsComponent.getModbusSlaveNatureTable(accessMode), //
				ModbusSlaveNatureTable.of(KmtronicRelay8Port.class, accessMode, 100)//
						.channel(0, KmtronicRelay8Port.ChannelId.RELAY_1, ModbusType.UINT16) //
						.channel(1, KmtronicRelay8Port.ChannelId.RELAY_2, ModbusType.UINT16) //
						.channel(2, KmtronicRelay8Port.ChannelId.RELAY_3, ModbusType.UINT16) //
						.channel(3, KmtronicRelay8Port.ChannelId.RELAY_4, ModbusType.UINT16) //
						.channel(4, KmtronicRelay8Port.ChannelId.RELAY_5, ModbusType.UINT16) //
						.channel(5, KmtronicRelay8Port.ChannelId.RELAY_6, ModbusType.UINT16) //
						.channel(6, KmtronicRelay8Port.ChannelId.RELAY_7, ModbusType.UINT16) //
						.channel(7, KmtronicRelay8Port.ChannelId.RELAY_8, ModbusType.UINT16) //
						.build()//
		);
	}

}
