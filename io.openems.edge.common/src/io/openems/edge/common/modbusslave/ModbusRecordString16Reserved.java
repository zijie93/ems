package io.openems.edge.common.modbusslave;

public class ModbusRecordString16Reserved extends ModbusRecordString16 {

	public ModbusRecordString16Reserved(int offset) {
		super(offset, "", (String) null);
	}

	@Override
	public String toString() {
		return "ModbusRecordString16Reserved [type=" + getType() + "]";
	}

}
