package io.openems.edge.edge2edge.ess;

import io.openems.common.channel.AccessMode;
import io.openems.common.channel.Level;
import io.openems.common.channel.Unit;
import io.openems.common.exceptions.OpenemsError.OpenemsNamedException;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.FloatWriteChannel;
import io.openems.edge.common.channel.StateChannel;
import io.openems.edge.common.channel.value.Value;
import io.openems.edge.common.component.OpenemsComponent;

public interface Edge2EdgeEss extends OpenemsComponent {

	public enum ChannelId implements io.openems.edge.common.channel.ChannelId {
		MINIMUM_POWER_SET_POINT(Doc.of(OpenemsType.FLOAT) //
				.accessMode(AccessMode.READ_ONLY)//
				.unit(Unit.WATT)), //
		MAXIMUM_POWER_SET_POINT(Doc.of(OpenemsType.FLOAT) //
				.accessMode(AccessMode.READ_ONLY)//
				.unit(Unit.WATT)), //
		REMOTE_NO_OPENEMS(Doc.of(Level.FAULT)), //
		MAPPING_REMOTE_PROTOCOL_FAULT(Doc.of(Level.FAULT)), //

		REMOTE_FAULT(Doc.of(Level.FAULT)), //
		REMOTE_WARNING(Doc.of(Level.WARNING)), //
		REMOTE_INFO(Doc.of(Level.INFO)),

		REMOTE_SET_ACTIVE_POWER_EQUALS(Doc.of(OpenemsType.FLOAT) //
				.accessMode(AccessMode.WRITE_ONLY)//
				.unit(Unit.WATT)), //
		REMOTE_SET_REACTIVE_POWER_EQUALS(Doc.of(OpenemsType.FLOAT) //
				.accessMode(AccessMode.WRITE_ONLY)//
				.unit(Unit.VOLT_AMPERE_REACTIVE)) //
		; //

		private final Doc doc;

		private ChannelId(Doc doc) {
			this.doc = doc;
		}

		@Override
		public Doc doc() {
			return this.doc;
		}
	}

	/**
	 * Gets the Channel for {@link ChannelId#REMOTE_SET_ACTIVE_POWER_EQUALS}.
	 *
	 * @return the Channel
	 */
	public default FloatWriteChannel getRemoteSetActivePowerEqualsChannel() {
		return this.channel(ChannelId.REMOTE_SET_ACTIVE_POWER_EQUALS);
	}

	/**
	 * Sets an Active Power Equals setpoint in [W]. Negative values for Charge;
	 * positive for Discharge. See {@link ChannelId#REMOTE_SET_ACTIVE_POWER_EQUALS}.
	 *
	 * @param value the next write value
	 * @throws OpenemsNamedException on error
	 */
	public default void setRemoteActivePowerEquals(Float value) throws OpenemsNamedException {
		this.getRemoteSetActivePowerEqualsChannel().setNextWriteValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#REMOTE_SET_REACTIVE_POWER_EQUALS}.
	 *
	 * @return the Channel
	 */
	public default FloatWriteChannel getRemoteSetReactivePowerEqualsChannel() {
		return this.channel(ChannelId.REMOTE_SET_REACTIVE_POWER_EQUALS);
	}

	/**
	 * Sets an Active Power Equals setpoint in [W]. Negative values for Charge;
	 * positive for Discharge. See {@link ChannelId#REMOTE_SET_ACTIVE_POWER_EQUALS}.
	 *
	 * @param value the next write value
	 * @throws OpenemsNamedException on error
	 */
	public default void setRemoteReactivePowerEquals(Float value) throws OpenemsNamedException {
		this.getRemoteSetReactivePowerEqualsChannel().setNextWriteValue(value);
	}

	/**
	 * Gets the Channel for {@link ChannelId#MAPPING_REMOTE_PROTOCOL_FAULT}.
	 *
	 * @return the Channel
	 */
	public default StateChannel getMappingRemoteProtocolFaultChannel() {
		return this.channel(ChannelId.MAPPING_REMOTE_PROTOCOL_FAULT);
	}

	/**
	 * Gets the boolean if the state channel is active. See
	 * {@link ChannelId#MAPPING_REMOTE_PROTOCOL_FAULT}.
	 *
	 * @return the Channel {@link Value}
	 */
	public default Value<Boolean> getMappingRemoteProtocolFault() {
		return this.getMappingRemoteProtocolFaultChannel().value();
	}

	/**
	 * Internal method to set the 'nextValue' on
	 * {@link ChannelId#MAPPING_REMOTE_PROTOCOL_FAULT} Channel.
	 *
	 * @param value the next value
	 */
	public default void _setMappingRemoteProtocolFault(Boolean value) {
		this.getMappingRemoteProtocolFaultChannel().setNextValue(value);
	}
}
