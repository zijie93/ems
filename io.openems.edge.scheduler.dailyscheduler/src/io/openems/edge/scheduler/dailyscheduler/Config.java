package io.openems.edge.scheduler.dailyscheduler;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import io.openems.edge.scheduler.api.Scheduler;

@ObjectClassDefinition( //
		name = "Scheduler Daily Scheduler", description = "")

@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this Component")
	String id() default "dailyScheduler0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this Component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this Component enabled?")
	boolean enabled() default true;

	int cycleTime() default Scheduler.DEFAULT_CYCLE_TIME;

	@AttributeDefinition(name = "Controller-IDs", description = "IDs of Controllers.")
	String controllers_ids_json() default "";

	@AttributeDefinition(name = "Controller-IDs", description = "IDs of Controllers. Controller execution is going to be sorted in the order of the IDs.")
	String[] controllers_ids() default {};

	String webconsole_configurationFactory_nameHint() default "Daily Scheduler [{id}]";

}
