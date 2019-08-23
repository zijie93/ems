package io.openems.edge.goodwe.et;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition( //
		name = "GoodweET Battery-Inverter", //
		description = "Implements the Goodwe battery inverter for 3 phase systems.")

@interface Config {
	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "ess0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;
	
	@AttributeDefinition(name = "Modbus Unit-id", description = "Unit-id")
	int unit_id() default 0xF7;
	
	@AttributeDefinition(name = "Read-Only mode", description = "Enables Read-Only")
	boolean readonly() default false;

	@AttributeDefinition(name = "Modbus-ID", description = "ID of Modbus brige.")
	String modbus_id();

	@AttributeDefinition(name = "Modbus target filter", description = "This is auto-generated by 'Modbus-ID'.")
	String Modbus_target() default "";

	String webconsole_configurationFactory_nameHint() default "GoodweET Battery-Inverter [{id}]";
}
