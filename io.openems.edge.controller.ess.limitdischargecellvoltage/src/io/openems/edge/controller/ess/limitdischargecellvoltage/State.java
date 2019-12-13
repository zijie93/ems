package io.openems.edge.controller.ess.limitdischargecellvoltage;

import io.openems.common.types.OptionsEnum;

public enum State implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), //
	NORMAL(0, "Normal"), //
	LIMIT(1, "Limit"), //	
	FORCE_CHARGE(3, "Force ForceCharge"), //
	FULL_CHARGE(4, "Full ForceCharge"), //
	CHECK(5, "Check");

	private final int value;
	private final String name;

	private State(int value, String name) {
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