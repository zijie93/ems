package io.openems.edge.ess.sma.stpxx3se.battery;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "ESS SMA Sunny Tripower SE Battery", //
		description = "Collects Some more Sunspec unrelated data.")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "battery0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;

	@AttributeDefinition(name = "Modbus-ID", description = "ID of Modbus bridge.")
	String modbus_id() default "modbus0";

	@AttributeDefinition(name = "Modbus-Unit-ID", description = "Modbus Unit-ID.")
	int modbusUnitId() default 3;
	
	@AttributeDefinition(name = "Charge Max Voltage", description = "Maximum voltage for charging in V")
	int chargeMaxVoltage() default 505;
	
	@AttributeDefinition(name = "Discharge Min Voltage", description = "Minimum voltage for discharging in V")
	int dischargeMinVoltage() default 390;

	@AttributeDefinition(name = "Modbus target filter", description = "This is auto-generated by 'Modbus-ID'.")
	String Modbus_target() default "(enabled=true)";

	String webconsole_configurationFactory_nameHint() default "ESS SMA Sunny Tripower SE Battery [{id}]";

}
