package io.openems.edge.ess.refubeckhoff;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import io.openems.edge.ess.refubeckhoff.enums.EssState;
import io.openems.edge.ess.refubeckhoff.enums.SetOperationMode;

@ObjectClassDefinition(//
		name = "ESS Refu Beckhoff", //
		description = "The energy storage system implementation of a Refu Ess along with beckhoff computer.")
@interface Config {

	@AttributeDefinition(name = "Component-ID", description = "Unique ID of this component")
	String id() default "ess0";

	@AttributeDefinition(name = "Alias", description = "Human-readable name of this component; defaults to Component-ID")
	String alias() default "";

	@AttributeDefinition(name = "Is enabled?", description = "Is this component enabled?")
	boolean enabled() default true;

	@AttributeDefinition(name = "Modbus-ID", description = "ID of modbus bridge.")
	String modbus_id() default "modbus0";

	@AttributeDefinition(name = "Ess state", description = "Switches the ess into the given state, if auto is used, ess state is set automatically.")
	EssState essState() default EssState.AUTO;

	@AttributeDefinition(name = "Operational state", description = "Switches the operational state.")
	SetOperationMode operationState() default SetOperationMode.PQ_SET_POINT;

	@AttributeDefinition(name = "Is Symmetric mode?", description = "In Symmetric mode no coefficients for L1/L2/L3 are available, resulting in a\n"
			+ "	 * smaller linear equation system that is faster to solve.")
	boolean symmetricMode() default false;

	@AttributeDefinition(name = "Acknowledge Error", description = "No of times error to be acknowledged.")
	int acknowledgeError() default 15;

	@AttributeDefinition(name = "Modbus target filter", description = "This is auto-generated by 'Modbus-ID'.")
	String Modbus_target() default "(enabled=true)";

	String webconsole_configurationFactory_nameHint() default "ESS Refu Beckhoff [{id}]";

}