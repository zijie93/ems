package io.openems.edge.ess.byd.container;

import io.openems.edge.common.channel.doc.OptionsEnum;

public enum SystemWorkmode implements OptionsEnum {
	UNDEFINED(-1, "Undefined"), 
	PQ_MODE(2, "PQ-mode");
	
	private final int value;
	private final String name;

	private SystemWorkmode(int value, String name) {
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
