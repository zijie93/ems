package io.openems.edge.predictor.persistant.model;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition( //
		name = "Predictor PersistantModel", //
		description = "This Predictor predicts the values configured")

@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "predictor0";
	
	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;
	

	String webconsole_configurationFactory_nameHint() default "Persistant Model [{id}]";
}
