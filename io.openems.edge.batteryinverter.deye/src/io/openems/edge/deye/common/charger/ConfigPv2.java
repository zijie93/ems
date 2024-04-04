package io.openems.edge.deye.common.charger;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(//
		name = "ESS FENECON Commercial 40 DC Charger PV2", //
		description = "Implements the FENECON Commercial 40 DC Charger.")
@interface ConfigPv2 {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "charger1";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "PV2";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;

	@AttributeDefinition(name = "FENECON Commercial40-ID", description = "ID of FENECON Commercial 40 device.")
	String ess_id() default "ess0";

	@AttributeDefinition(name = "FENECON Commercial40 target filter", description = "This is auto-generated by 'FENECON Commercial40-ID'.")
	String Ess_target() default "(enabled=true)";

	@AttributeDefinition(name = "Modbus target filter", description = "This is auto-generated by 'Modbus-ID' from FENECON Commercial40.")
	String Modbus_target() default "(enabled=true)";

	String webconsole_configurationFactory_nameHint() default "ESS FENECON Commercial 40 DC Charger PV2 [{id}]";
}