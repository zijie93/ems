package io.openems.edge.ess.mr.gridcon;

import java.util.Arrays;
import java.util.stream.Stream;

import io.openems.edge.common.channel.AbstractReadChannel;
import io.openems.edge.common.channel.BooleanReadChannel;
import io.openems.edge.common.channel.BooleanWriteChannel;
import io.openems.edge.common.channel.FloatReadChannel;
import io.openems.edge.common.channel.FloatWriteChannel;
import io.openems.edge.common.channel.IntegerReadChannel;
import io.openems.edge.common.channel.IntegerWriteChannel;
import io.openems.edge.common.channel.LongReadChannel;
import io.openems.edge.common.channel.ShortReadChannel;
import io.openems.edge.common.channel.StateCollectorChannel;
import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.ess.api.ManagedSymmetricEss;
import io.openems.edge.ess.api.SymmetricEss;
import io.openems.edge.ess.mr.gridcon.enums.GridConChannelId;

public class Utils {
	public static Stream<? extends AbstractReadChannel<?>> initializeChannels(GridconPCS ess) {
		// Define the channels. Using streams + switch enables Eclipse IDE to tell us if
		// we are missing an Enum value.
		return Stream.of( //
				Arrays.stream(OpenemsComponent.ChannelId.values()).map(channelId -> {
					switch (channelId) {
					case STATE:
						return new StateCollectorChannel(ess, channelId);
					}
					return null;
				}), Arrays.stream(SymmetricEss.ChannelId.values()).map(channelId -> {
					switch (channelId) {
					case SOC:					
					case ACTIVE_CHARGE_ENERGY:						
					case ACTIVE_DISCHARGE_ENERGY:
						return new IntegerReadChannel(ess, channelId);
					case MAX_APPARENT_POWER:
						return new IntegerReadChannel(ess, channelId, GridconPCS.MAX_APPARENT_POWER);
					case GRID_MODE:
						return new IntegerReadChannel(ess, channelId, SymmetricEss.GridMode.UNDEFINED.ordinal());
					case ACTIVE_POWER:
					case REACTIVE_POWER:
						return new IntegerReadChannel(ess, channelId);
						//return new FloatReadChannel(ess, channelId); // causes java.lang.IllegalArgumentException: [null/ActivePower]: Types do not match. Got [FLOAT]. Expected [INTEGER].
					}
					return null;
				}), Arrays.stream(ManagedSymmetricEss.ChannelId.values()).map(channelId -> {
					switch (channelId) {
					case DEBUG_SET_ACTIVE_POWER:
					case DEBUG_SET_REACTIVE_POWER:
					case ALLOWED_CHARGE_POWER:
					case ALLOWED_DISCHARGE_POWER:
						return new IntegerReadChannel(ess, channelId);
					case SET_ACTIVE_POWER_EQUALS:
					case SET_REACTIVE_POWER_EQUALS:
						return new IntegerWriteChannel(ess, channelId);
					}
					return null;
				}), Arrays.stream(GridConChannelId.values()).map(channelId -> {
					switch (channelId) {
							
					case CCU_ERROR_CODE:
						
					case IPU_1_STATUS_FAN_SPEED_MAX:
					case IPU_1_STATUS_FAN_SPEED_MIN:
															
					case IPU_2_STATUS_FAN_SPEED_MAX:
					case IPU_2_STATUS_FAN_SPEED_MIN:
					
					case IPU_3_STATUS_FAN_SPEED_MAX:
					case IPU_3_STATUS_FAN_SPEED_MIN:
					
					case IPU_4_STATUS_FAN_SPEED_MAX:
					case IPU_4_STATUS_FAN_SPEED_MIN:
											
					case MIRROR_COMMAND_ERROR_CODE_FEEDBACK:						
						return new IntegerReadChannel(ess, channelId);
						
					case MIRROR_COMMAND_CONTROL_WORD:
						return new LongReadChannel(ess, channelId);

					case IPU_1_STATUS_STATUS_STATE_MACHINE:
					case IPU_1_STATUS_STATUS_MCU:
					case IPU_2_STATUS_STATUS_STATE_MACHINE:
					case IPU_2_STATUS_STATUS_MCU:
					case IPU_3_STATUS_STATUS_STATE_MACHINE:
					case IPU_3_STATUS_STATUS_MCU:	
					case IPU_4_STATUS_STATUS_STATE_MACHINE:
					case IPU_4_STATUS_STATUS_MCU:
						return new ShortReadChannel(ess, channelId);
		
					case CCU_STATE_DERATING_HARMONICS:
					case CCU_STATE_DERATING_POWER:
					case CCU_STATE_ERROR:
					case CCU_STATE_IDLE:
					case CCU_STATE_OVERLOAD:
					case CCU_STATE_PAUSE:
					case CCU_STATE_PRECHARGE:
					case CCU_STATE_READY:
					case CCU_STATE_RUN:
					case CCU_STATE_SHORT_CIRCUIT_DETECTED:
					case CCU_STATE_SIA_ACTIVE:
					case CCU_STATE_STOP_PRECHARGE:
					case CCU_STATE_VOLTAGE_RAMPING_UP:

						return new BooleanReadChannel(ess, channelId);
	
					case CCU_CURRENT_IL1:
					case CCU_CURRENT_IL2:
					case CCU_CURRENT_IL3:
					case CCU_FREQUENCY:
					case CCU_POWER_P:
					case CCU_POWER_Q:
					case CCU_VOLTAGE_U12:
					case CCU_VOLTAGE_U23:
					case CCU_VOLTAGE_U31:
						
					case IPU_1_DC_DC_MEASUREMENTS_ACCUMULATED_DC_UTILIZATION:
					case IPU_1_DC_DC_MEASUREMENTS_ACCUMULATED_SUM_DC_CURRENT:
					case IPU_1_DC_DC_MEASUREMENTS_CURRENT_STRING_A:
					case IPU_1_DC_DC_MEASUREMENTS_CURRENT_STRING_B:
					case IPU_1_DC_DC_MEASUREMENTS_CURRENT_STRING_C:
					case IPU_1_DC_DC_MEASUREMENTS_POWER_STRING_A:
					case IPU_1_DC_DC_MEASUREMENTS_POWER_STRING_B:
					case IPU_1_DC_DC_MEASUREMENTS_POWER_STRING_C:
					case IPU_1_DC_DC_MEASUREMENTS_RESERVE_1:
					case IPU_1_DC_DC_MEASUREMENTS_RESERVE_2:
					case IPU_1_DC_DC_MEASUREMENTS_UTILIZATION_STRING_A:
					case IPU_1_DC_DC_MEASUREMENTS_UTILIZATION_STRING_B:
					case IPU_1_DC_DC_MEASUREMENTS_UTILIZATION_STRING_C:
					case IPU_1_DC_DC_MEASUREMENTS_VOLTAGE_STRING_A:
					case IPU_1_DC_DC_MEASUREMENTS_VOLTAGE_STRING_B:
					case IPU_1_DC_DC_MEASUREMENTS_VOLTAGE_STRING_C:
						
					case IPU_2_DC_DC_MEASUREMENTS_ACCUMULATED_DC_UTILIZATION:
					case IPU_2_DC_DC_MEASUREMENTS_ACCUMULATED_SUM_DC_CURRENT:
					case IPU_2_DC_DC_MEASUREMENTS_CURRENT_STRING_A:
					case IPU_2_DC_DC_MEASUREMENTS_CURRENT_STRING_B:
					case IPU_2_DC_DC_MEASUREMENTS_CURRENT_STRING_C:
					case IPU_2_DC_DC_MEASUREMENTS_POWER_STRING_A:
					case IPU_2_DC_DC_MEASUREMENTS_POWER_STRING_B:
					case IPU_2_DC_DC_MEASUREMENTS_POWER_STRING_C:
					case IPU_2_DC_DC_MEASUREMENTS_RESERVE_1:
					case IPU_2_DC_DC_MEASUREMENTS_RESERVE_2:
					case IPU_2_DC_DC_MEASUREMENTS_UTILIZATION_STRING_A:
					case IPU_2_DC_DC_MEASUREMENTS_UTILIZATION_STRING_B:
					case IPU_2_DC_DC_MEASUREMENTS_UTILIZATION_STRING_C:
					case IPU_2_DC_DC_MEASUREMENTS_VOLTAGE_STRING_A:
					case IPU_2_DC_DC_MEASUREMENTS_VOLTAGE_STRING_B:
					case IPU_2_DC_DC_MEASUREMENTS_VOLTAGE_STRING_C:

					case IPU_3_DC_DC_MEASUREMENTS_ACCUMULATED_DC_UTILIZATION:
					case IPU_3_DC_DC_MEASUREMENTS_ACCUMULATED_SUM_DC_CURRENT:
					case IPU_3_DC_DC_MEASUREMENTS_CURRENT_STRING_A:
					case IPU_3_DC_DC_MEASUREMENTS_CURRENT_STRING_B:
					case IPU_3_DC_DC_MEASUREMENTS_CURRENT_STRING_C:
					case IPU_3_DC_DC_MEASUREMENTS_POWER_STRING_A:
					case IPU_3_DC_DC_MEASUREMENTS_POWER_STRING_B:
					case IPU_3_DC_DC_MEASUREMENTS_POWER_STRING_C:
					case IPU_3_DC_DC_MEASUREMENTS_RESERVE_1:
					case IPU_3_DC_DC_MEASUREMENTS_RESERVE_2:
					case IPU_3_DC_DC_MEASUREMENTS_UTILIZATION_STRING_A:
					case IPU_3_DC_DC_MEASUREMENTS_UTILIZATION_STRING_B:
					case IPU_3_DC_DC_MEASUREMENTS_UTILIZATION_STRING_C:
					case IPU_3_DC_DC_MEASUREMENTS_VOLTAGE_STRING_A:
					case IPU_3_DC_DC_MEASUREMENTS_VOLTAGE_STRING_B:
					case IPU_3_DC_DC_MEASUREMENTS_VOLTAGE_STRING_C:
						
					case IPU_4_DC_DC_MEASUREMENTS_ACCUMULATED_DC_UTILIZATION:
					case IPU_4_DC_DC_MEASUREMENTS_ACCUMULATED_SUM_DC_CURRENT:
					case IPU_4_DC_DC_MEASUREMENTS_CURRENT_STRING_A:
					case IPU_4_DC_DC_MEASUREMENTS_CURRENT_STRING_B:
					case IPU_4_DC_DC_MEASUREMENTS_CURRENT_STRING_C:
					case IPU_4_DC_DC_MEASUREMENTS_POWER_STRING_A:
					case IPU_4_DC_DC_MEASUREMENTS_POWER_STRING_B:
					case IPU_4_DC_DC_MEASUREMENTS_POWER_STRING_C:
					case IPU_4_DC_DC_MEASUREMENTS_RESERVE_1:
					case IPU_4_DC_DC_MEASUREMENTS_RESERVE_2:
					case IPU_4_DC_DC_MEASUREMENTS_UTILIZATION_STRING_A:
					case IPU_4_DC_DC_MEASUREMENTS_UTILIZATION_STRING_B:
					case IPU_4_DC_DC_MEASUREMENTS_UTILIZATION_STRING_C:
					case IPU_4_DC_DC_MEASUREMENTS_VOLTAGE_STRING_A:
					case IPU_4_DC_DC_MEASUREMENTS_VOLTAGE_STRING_B:
					case IPU_4_DC_DC_MEASUREMENTS_VOLTAGE_STRING_C:

					case IPU_1_STATUS_DC_LINK_CURRENT:
					case IPU_1_STATUS_DC_LINK_NEGATIVE_VOLTAGE:
					case IPU_1_STATUS_DC_LINK_POSITIVE_VOLTAGE:
					case IPU_1_STATUS_DC_LINK_UTILIZATION:
					case IPU_1_STATUS_FILTER_CURRENT:
					case IPU_1_STATUS_RESERVE_1:
					case IPU_1_STATUS_RESERVE_2:
					case IPU_1_STATUS_RESERVE_3:
					case IPU_1_STATUS_DC_LINK_ACTIVE_POWER:
					case IPU_1_STATUS_TEMPERATURE_GRID_CHOKE:
					case IPU_1_STATUS_TEMPERATURE_IGBT_MAX:
					case IPU_1_STATUS_TEMPERATURE_INVERTER_CHOKE:
					case IPU_1_STATUS_TEMPERATURE_MCU_BOARD:

					case IPU_2_STATUS_DC_LINK_ACTIVE_POWER:
					case IPU_2_STATUS_DC_LINK_CURRENT:
					case IPU_2_STATUS_DC_LINK_NEGATIVE_VOLTAGE:
					case IPU_2_STATUS_DC_LINK_POSITIVE_VOLTAGE:
					case IPU_2_STATUS_DC_LINK_UTILIZATION:
					case IPU_2_STATUS_FILTER_CURRENT:
					case IPU_2_STATUS_RESERVE_1:
					case IPU_2_STATUS_RESERVE_2:
					case IPU_2_STATUS_RESERVE_3:
					case IPU_2_STATUS_TEMPERATURE_GRID_CHOKE:
					case IPU_2_STATUS_TEMPERATURE_IGBT_MAX:
					case IPU_2_STATUS_TEMPERATURE_INVERTER_CHOKE:
					case IPU_2_STATUS_TEMPERATURE_MCU_BOARD:

					case IPU_3_STATUS_DC_LINK_ACTIVE_POWER:
					case IPU_3_STATUS_DC_LINK_CURRENT:
					case IPU_3_STATUS_DC_LINK_NEGATIVE_VOLTAGE:
					case IPU_3_STATUS_DC_LINK_POSITIVE_VOLTAGE:
					case IPU_3_STATUS_DC_LINK_UTILIZATION:
					case IPU_3_STATUS_FILTER_CURRENT:
					case IPU_3_STATUS_RESERVE_1:
					case IPU_3_STATUS_RESERVE_2:
					case IPU_3_STATUS_RESERVE_3:
					case IPU_3_STATUS_TEMPERATURE_GRID_CHOKE:
					case IPU_3_STATUS_TEMPERATURE_IGBT_MAX:
					case IPU_3_STATUS_TEMPERATURE_INVERTER_CHOKE:
					case IPU_3_STATUS_TEMPERATURE_MCU_BOARD:
					
					case IPU_4_STATUS_DC_LINK_ACTIVE_POWER:
					case IPU_4_STATUS_DC_LINK_CURRENT:
					case IPU_4_STATUS_DC_LINK_NEGATIVE_VOLTAGE:
					case IPU_4_STATUS_DC_LINK_POSITIVE_VOLTAGE:
					case IPU_4_STATUS_DC_LINK_UTILIZATION:
					case IPU_4_STATUS_FILTER_CURRENT:
					case IPU_4_STATUS_RESERVE_1:
					case IPU_4_STATUS_RESERVE_2:
					case IPU_4_STATUS_RESERVE_3:
					case IPU_4_STATUS_TEMPERATURE_GRID_CHOKE:
					case IPU_4_STATUS_TEMPERATURE_IGBT_MAX:
					case IPU_4_STATUS_TEMPERATURE_INVERTER_CHOKE:
					case IPU_4_STATUS_TEMPERATURE_MCU_BOARD:
						
					case MIRROR_COMMAND_CONTROL_PARAMETER_F0:
					case MIRROR_COMMAND_CONTROL_PARAMETER_P_REFERENCE:
					case MIRROR_COMMAND_CONTROL_PARAMETER_Q_REF:
					case MIRROR_COMMAND_CONTROL_PARAMETER_U0:
					case MIRROR_COMMAND_CONTROL_WORD_ACKNOWLEDGE:
					case MIRROR_COMMAND_CONTROL_WORD_ACTIVATE_HARMONIC_COMPENSATION:
					case MIRROR_COMMAND_CONTROL_WORD_ACTIVATE_SHORT_CIRCUIT_HANDLING:
					case MIRROR_COMMAND_CONTROL_WORD_BLACKSTART_APPROVAL:
					case MIRROR_COMMAND_CONTROL_WORD_ENABLE_IPU_1:
					case MIRROR_COMMAND_CONTROL_WORD_ENABLE_IPU_2:
					case MIRROR_COMMAND_CONTROL_WORD_ENABLE_IPU_3:
					case MIRROR_COMMAND_CONTROL_WORD_ENABLE_IPU_4:
					case MIRROR_COMMAND_CONTROL_WORD_ID_1_SD_CARD_PARAMETER_SET:
					case MIRROR_COMMAND_CONTROL_WORD_ID_2_SD_CARD_PARAMETER_SET:
					case MIRROR_COMMAND_CONTROL_WORD_ID_3_SD_CARD_PARAMETER_SET:
					case MIRROR_COMMAND_CONTROL_WORD_ID_4_SD_CARD_PARAMETER_SET:
					case MIRROR_COMMAND_CONTROL_WORD_MODE_SELECTION:
					case MIRROR_COMMAND_CONTROL_WORD_PLAY:
					case MIRROR_COMMAND_CONTROL_WORD_READY:
					case MIRROR_COMMAND_CONTROL_WORD_STOP:
					case MIRROR_COMMAND_CONTROL_WORD_SYNC_APPROVAL:
					case MIRROR_COMMAND_CONTROL_WORD_TRIGGER_SIA:
					case MIRROR_CONTROL_IPU_1_PARAMETERS_DC_CURRENT_SETPOINT:
					case MIRROR_CONTROL_IPU_1_PARAMETERS_DC_VOLTAGE_SETPOINT:
					case MIRROR_CONTROL_IPU_1_PARAMETERS_F0_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_1_PARAMETERS_P_MAX_CHARGE:
					case MIRROR_CONTROL_IPU_1_PARAMETERS_P_MAX_DISCHARGE:
					case MIRROR_CONTROL_IPU_1_PARAMETERS_P_REF_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_1_PARAMETERS_Q_REF_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_1_PARAMETERS_U0_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_2_PARAMETERS_DC_CURRENT_SETPOINT:
					case MIRROR_CONTROL_IPU_2_PARAMETERS_DC_VOLTAGE_SETPOINT:
					case MIRROR_CONTROL_IPU_2_PARAMETERS_F0_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_2_PARAMETERS_P_MAX_CHARGE:
					case MIRROR_CONTROL_IPU_2_PARAMETERS_P_MAX_DISCHARGE:
					case MIRROR_CONTROL_IPU_2_PARAMETERS_P_REF_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_2_PARAMETERS_Q_REF_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_2_PARAMETERS_U0_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_3_PARAMETERS_DC_CURRENT_SETPOINT:
					case MIRROR_CONTROL_IPU_3_PARAMETERS_DC_VOLTAGE_SETPOINT:
					case MIRROR_CONTROL_IPU_3_PARAMETERS_F0_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_3_PARAMETERS_P_MAX_CHARGE:
					case MIRROR_CONTROL_IPU_3_PARAMETERS_P_MAX_DISCHARGE:
					case MIRROR_CONTROL_IPU_3_PARAMETERS_P_REF_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_3_PARAMETERS_Q_REF_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_3_PARAMETERS_U0_OFFSET_TO_CCU_VALUE:
					case MIRROR_CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_DC_DC_STRING_CONTROL_MODE:
					case MIRROR_CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_DC_VOLTAGE_SETPOINT:
					case MIRROR_CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_I_REF_STRING_A:
					case MIRROR_CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_I_REF_STRING_B:
					case MIRROR_CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_I_REF_STRING_C:
					case MIRROR_CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_WEIGHT_STRING_A:
					case MIRROR_CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_WEIGHT_STRING_B:
					case MIRROR_CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_WEIGHT_STRING_C:
					case MIRROR_CONTROL_PARAMETER_F_P_DROOP_T1_MAIN:
					case MIRROR_CONTROL_PARAMETER_F_P_DRROP_MAIN:
					case MIRROR_CONTROL_PARAMETER_P_CONTROL_LIM_ONE:
					case MIRROR_CONTROL_PARAMETER_P_CONTROL_LIM_TWO:
					case MIRROR_CONTROL_PARAMETER_P_CONTROL_MODE:
					case MIRROR_CONTROL_PARAMETER_P_F_DEAD_BAND:
					case MIRROR_CONTROL_PARAMETER_P_F_DROOP_MAIN:
					case MIRROR_CONTROL_PARAMETER_P_U_DEAD_BAND:
					case MIRROR_CONTROL_PARAMETER_P_U_DROOP:
					case MIRROR_CONTROL_PARAMETER_P_U_MAX_CHARGE:
					case MIRROR_CONTROL_PARAMETER_P_U_MAX_DISCHARGE:
					case MIRROR_CONTROL_PARAMETER_Q_LIMIT:
					case MIRROR_CONTROL_PARAMETER_Q_U_DEAD_BAND:
					case MIRROR_CONTROL_PARAMETER_Q_U_DROOP_MAIN:
					case MIRROR_CONTROL_PARAMETER_U_Q_DROOP_MAIN:
					case MIRROR_CONTROL_PARAMETER_U_Q_DROOP_T1_MAIN:
						return new FloatReadChannel(ess, channelId);
					
					case COMMAND_ERROR_CODE_FEEDBACK:
					case COMMAND_TIME_SYNC_DATE:
					case COMMAND_TIME_SYNC_TIME:
					case COMMAND_CONTROL_WORD:
					
						return new IntegerWriteChannel(ess, channelId);
					
					case COMMAND_CONTROL_WORD_ACKNOWLEDGE:
					case COMMAND_CONTROL_WORD_ACTIVATE_HARMONIC_COMPENSATION:
					case COMMAND_CONTROL_WORD_ACTIVATE_SHORT_CIRCUIT_HANDLING:
					case COMMAND_CONTROL_WORD_BLACKSTART_APPROVAL:
					case COMMAND_CONTROL_WORD_ENABLE_IPU_1:
					case COMMAND_CONTROL_WORD_ENABLE_IPU_2:
					case COMMAND_CONTROL_WORD_ENABLE_IPU_3:
					case COMMAND_CONTROL_WORD_ENABLE_IPU_4:
					case COMMAND_CONTROL_WORD_ID_1_SD_CARD_PARAMETER_SET:
					case COMMAND_CONTROL_WORD_ID_2_SD_CARD_PARAMETER_SET:
					case COMMAND_CONTROL_WORD_ID_3_SD_CARD_PARAMETER_SET:
					case COMMAND_CONTROL_WORD_ID_4_SD_CARD_PARAMETER_SET:
					case COMMAND_CONTROL_WORD_MODE_SELECTION:
					case COMMAND_CONTROL_WORD_PLAY:
					case COMMAND_CONTROL_WORD_READY:
					case COMMAND_CONTROL_WORD_STOP:
					case COMMAND_CONTROL_WORD_SYNC_APPROVAL:
					case COMMAND_CONTROL_WORD_TRIGGER_SIA:
						return new BooleanWriteChannel(ess, channelId);

					case COMMAND_CONTROL_PARAMETER_F0:
					case COMMAND_CONTROL_PARAMETER_P_REF:
					case COMMAND_CONTROL_PARAMETER_Q_REF:
					case COMMAND_CONTROL_PARAMETER_U0:
						
					case CONTROL_PARAMETER_F_P_DROOP_T1_MAIN:
					case CONTROL_PARAMETER_F_P_DRROP_MAIN:
					case CONTROL_PARAMETER_P_CONTROL_LIM_ONE:
					case CONTROL_PARAMETER_P_CONTROL_LIM_TWO:
					
					case CONTROL_PARAMETER_P_F_DEAD_BAND:
					case CONTROL_PARAMETER_P_F_DROOP_MAIN:
					case CONTROL_PARAMETER_P_U_DEAD_BAND:
					case CONTROL_PARAMETER_P_U_DROOP:
					case CONTROL_PARAMETER_P_U_MAX_CHARGE:
					case CONTROL_PARAMETER_P_U_MAX_DISCHARGE:
					case CONTROL_PARAMETER_Q_LIMIT:
					case CONTROL_PARAMETER_Q_U_DEAD_BAND:
					case CONTROL_PARAMETER_Q_U_DROOP_MAIN:
					case CONTROL_PARAMETER_U_Q_DROOP_MAIN:
					case CONTROL_PARAMETER_U_Q_DROOP_T1_MAIN:
					case CONTROL_PARAMETER_P_CONTROL_MODE:
						
					case CONTROL_IPU_1_PARAMETERS_DC_VOLTAGE_SETPOINT:
					case CONTROL_IPU_1_PARAMETERS_DC_CURRENT_SETPOINT:
					case CONTROL_IPU_1_PARAMETERS_U0_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_1_PARAMETERS_F0_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_1_PARAMETERS_Q_REF_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_1_PARAMETERS_P_REF_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_1_PARAMETERS_P_MAX_DISCHARGE:
					case CONTROL_IPU_1_PARAMETERS_P_MAX_CHARGE:
					
					case CONTROL_IPU_2_PARAMETERS_DC_VOLTAGE_SETPOINT:
					case CONTROL_IPU_2_PARAMETERS_DC_CURRENT_SETPOINT:
					case CONTROL_IPU_2_PARAMETERS_U0_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_2_PARAMETERS_F0_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_2_PARAMETERS_Q_REF_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_2_PARAMETERS_P_REF_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_2_PARAMETERS_P_MAX_DISCHARGE:
					case CONTROL_IPU_2_PARAMETERS_P_MAX_CHARGE:
						
					case CONTROL_IPU_3_PARAMETERS_DC_VOLTAGE_SETPOINT:
					case CONTROL_IPU_3_PARAMETERS_DC_CURRENT_SETPOINT:
					case CONTROL_IPU_3_PARAMETERS_U0_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_3_PARAMETERS_F0_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_3_PARAMETERS_Q_REF_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_3_PARAMETERS_P_REF_OFFSET_TO_CCU_VALUE:
					case CONTROL_IPU_3_PARAMETERS_P_MAX_DISCHARGE:
					case CONTROL_IPU_3_PARAMETERS_P_MAX_CHARGE:
					
					case CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_DC_VOLTAGE_SETPOINT:
					case CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_WEIGHT_STRING_A:
					case CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_WEIGHT_STRING_B:
					case CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_WEIGHT_STRING_C:
					case CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_I_REF_STRING_A:
					case CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_I_REF_STRING_B:
					case CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_I_REF_STRING_C:	
					case CONTROL_IPU_4_DC_DC_CONVERTER_PARAMETERS_DC_DC_STRING_CONTROL_MODE:
						return new FloatWriteChannel(ess, channelId);
										
					}
					return null;
				}) //
		).flatMap(channel -> channel);
	}
}
