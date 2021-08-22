package io.openems.edge.bosch.bpts5hybrid.meter;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition(//
		name = "Bosch BPT-S 5 Meter", //
		description = "Bosch BPT-S 5 Hybrid energy storage system - Meter component")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "boschBpts5hybridMeter0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;
	
	@AttributeDefinition(name = "Core-ID", description = "Component-ID of \"Bosch BPT-S 5 Hybrid Core\" component ?")
	String core_id() default "boschBpts5hybridCore0";

	@AttributeDefinition(name = "Core target filter", description = "This is auto-generated by 'Core-ID'.")
	String core_target() default "";
		
	String webconsole_configurationFactory_nameHint() default "Bosch BPT-S 5 Hybrid Meter [{id}]";
}