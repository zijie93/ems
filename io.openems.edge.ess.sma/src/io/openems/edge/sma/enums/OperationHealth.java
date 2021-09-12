package io.openems.edge.sma.enums;

import io.openems.common.types.OptionsEnum;

public enum OperationHealth implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	ERROR(35, "Error"), //
	OFF(303, "Off"), //
	OK(307, "Ok"), //
	WARN(455, "Warning"); 

	private final int value;
	private final String name;

	private OperationHealth(int value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public OptionsEnum getUndefined() {
		return UNDEFINED;
	}
}