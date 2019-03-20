package io.openems.edge.fenecon.mini.ess;

import io.openems.edge.common.channel.Doc;
import io.openems.edge.common.channel.Level;
import io.openems.edge.common.channel.Unit;
import io.openems.common.types.OpenemsType;
import io.openems.edge.common.channel.AccessMode;

public enum EssChannelId implements io.openems.edge.common.channel.ChannelId {
	// EnumReadChannels
	SYSTEM_STATE(Doc.of(SystemState.values())), //
	CONTROL_MODE(Doc.of(ControlMode.values())), //
	BATTERY_GROUP_STATE(Doc.of(BatteryGroupState.values())), //

	// EnumWriteChannels
	PCS_MODE(Doc.of(PcsMode.values()) //
			.accessMode(AccessMode.WRITE_ONLY)), //
	SETUP_MODE(Doc.of(SetupMode.values()) //
			.accessMode(AccessMode.WRITE_ONLY)), //
	SET_WORK_STATE(Doc.of(SetWorkState.values()) //
			.accessMode(AccessMode.WRITE_ONLY)),

	// IntegerWriteChannels
	RTC_YEAR(Doc.of(OpenemsType.INTEGER) //
			.accessMode(AccessMode.WRITE_ONLY) //
			.text("Year")), //
	RTC_MONTH(Doc.of(OpenemsType.INTEGER) //
			.accessMode(AccessMode.WRITE_ONLY) //
			.text("Month")), //
	RTC_DAY(Doc.of(OpenemsType.INTEGER) //
			.accessMode(AccessMode.WRITE_ONLY) //
			.text("Day")), //
	RTC_HOUR(Doc.of(OpenemsType.INTEGER) //
			.accessMode(AccessMode.WRITE_ONLY) //
			.text("Hour")), //
	RTC_MINUTE(Doc.of(OpenemsType.INTEGER) //
			.accessMode(AccessMode.WRITE_ONLY) //
			.text("Minute")), //
	RTC_SECOND(Doc.of(OpenemsType.INTEGER) //
			.accessMode(AccessMode.WRITE_ONLY) //
			.text("Second")), //

	// IntegerReadChannels
	BATTERY_VOLTAGE(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.MILLIVOLT)), //
	BATTERY_CURRENT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.MILLIAMPERE)), //
	BATTERY_POWER(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.WATT)), //

	BECU1_CHARGE_CURRENT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.AMPERE)), //
	BECU1_DISCHARGE_CURRENT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.AMPERE)), //
	BECU1_VOLT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.VOLT)), //
	BECU1_CURRENT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.AMPERE)), //
	BECU1_SOC(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.PERCENT)), //

	BECU1_VERSION(Doc.of(OpenemsType.INTEGER)), //
	BECU1_MIN_VOLT_NO(Doc.of(OpenemsType.INTEGER)), //
	BECU1_MIN_VOLT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.VOLT)), //
	BECU1_MAX_VOLT_NO(Doc.of(OpenemsType.INTEGER)), //
	BECU1_MAX_VOLT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.VOLT)), //
	BECU1_MIN_TEMP_NO(Doc.of(OpenemsType.INTEGER)), //
	BECU1_MIN_TEMP(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.DEGREE_CELSIUS)), //
	BECU1_MAX_TEMP_NO(Doc.of(OpenemsType.INTEGER)), //
	BECU1_MAX_TEMP(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.DEGREE_CELSIUS)), //

	BECU2_CHARGE_CURRENT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.AMPERE)), //
	BECU2_DISCHARGE_CURRENT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.AMPERE)), //
	BECU2_VOLT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.VOLT)), //
	BECU2_CURRENT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.AMPERE)), //
	BECU2_SOC(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.PERCENT)), //

	BECU2_VERSION(Doc.of(OpenemsType.INTEGER)), //
	BECU2_MIN_VOLT_NO(Doc.of(OpenemsType.INTEGER)), //
	BECU2_MIN_VOLT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.VOLT)), //
	BECU2_MAX_VOLT_NO(Doc.of(OpenemsType.INTEGER)), //
	BECU2_MAX_VOLT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.VOLT)), //
	BECU2_MIN_TEMP_NO(Doc.of(OpenemsType.INTEGER)), //
	BECU2_MIN_TEMP(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.DEGREE_CELSIUS)), //
	BECU2_MAX_TEMP_NO(Doc.of(OpenemsType.INTEGER)), //
	BECU2_MAX_TEMP(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.DEGREE_CELSIUS)), //

	SYSTEM_WORK_MODE_STATE(Doc.of(OpenemsType.INTEGER)), //
	SYSTEM_WORK_STATE(Doc.of(OpenemsType.INTEGER)), //

	BECU_NUM(Doc.of(OpenemsType.INTEGER)), //
	BECU_WORK_STATE(Doc.of(OpenemsType.INTEGER)), //
	BECU_CHARGE_CURRENT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.AMPERE)), //
	BECU_DISCHARGE_CURRENT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.AMPERE)), //
	BECU_VOLT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.VOLT)), //
	BECU_CURRENT(Doc.of(OpenemsType.INTEGER) //
			.unit(Unit.AMPERE)), //

	// StateChannels
	STATE_1(Doc.of(Level.WARNING) //
			.text("BECU1GeneralChargeOverCurrentAlarm")), //
	STATE_2(Doc.of(Level.WARNING) //
			.text("BECU1GeneralDischargeOverCurrentAlarm")), //
	STATE_3(Doc.of(Level.WARNING) //
			.text("BECU1ChargeCurrentLimitAlarm")), //
	STATE_4(Doc.of(Level.WARNING) //
			.text("BECU1DischargeCurrentLimitAlarm")), //
	STATE_5(Doc.of(Level.WARNING) //
			.text("BECU1GeneralHighVoltageAlarm")), //
	STATE_6(Doc.of(Level.WARNING) //
			.text("BECU1GeneralLowVoltageAlarm")), //
	STATE_7(Doc.of(Level.WARNING) //
			.text("BECU1AbnormalVoltageChangeAlarm")), //
	STATE_8(Doc.of(Level.WARNING) //
			.text("BECU1GeneralHighTemperatureAlarm")), //
	STATE_9(Doc.of(Level.WARNING) //
			.text("BECU1GeneralLowTemperatureAlarm")), //
	STATE_10(Doc.of(Level.WARNING) //
			.text("BECU1AbnormalTemperatureChangeAlarm")), //
	STATE_11(Doc.of(Level.WARNING) //
			.text("BECU1SevereHighVoltageAlarm")), //
	STATE_12(Doc.of(Level.WARNING) //
			.text("BECU1SevereLowVoltageAlarm")), //
	STATE_13(Doc.of(Level.WARNING) //
			.text("BECU1SevereLowTemperatureAlarm")), //
	STATE_14(Doc.of(Level.WARNING) //
			.text("BECU1SeverveChargeOverCurrentAlarm")), //
	STATE_15(Doc.of(Level.WARNING) //
			.text("BECU1SeverveDischargeOverCurrentAlarm")), //
	STATE_16(Doc.of(Level.WARNING) //
			.text("BECU1AbnormalCellCapacityAlarm")), //
	STATE_17(Doc.of(Level.WARNING) //
			.text("BECU1BalancedSamplingAlarm")), //
	STATE_18(Doc.of(Level.WARNING) //
			.text("BECU1BalancedControlAlarm")), //
	STATE_19(Doc.of(Level.WARNING) //
			.text("BECU1HallSensorDoesNotWorkAccurately")), //
	STATE_20(Doc.of(Level.WARNING) //
			.text("BECU1Generalleakage")), //
	STATE_21(Doc.of(Level.WARNING) //
			.text("BECU1Severeleakage")), //
	STATE_22(Doc.of(Level.WARNING) //
			.text("BECU1Contactor1TurnOnAbnormity")), //
	STATE_23(Doc.of(Level.WARNING) //
			.text("BECU1Contactor1TurnOffAbnormity")), //
	STATE_24(Doc.of(Level.WARNING) //
			.text("BECU1Contactor2TurnOnAbnormity")), //
	STATE_25(Doc.of(Level.WARNING) //
			.text("BECU1Contactor2TurnOffAbnormity")), //
	STATE_26(Doc.of(Level.WARNING) //
			.text("BECU1Contactor4CheckAbnormity")), //
	STATE_27(Doc.of(Level.WARNING) //
			.text("BECU1ContactorCurrentUnsafe")), //
	STATE_28(Doc.of(Level.WARNING) //
			.text("BECU1Contactor5CheckAbnormity")), //
	STATE_29(Doc.of(Level.WARNING) //
			.text("BECU1HighVoltageOffset")), //
	STATE_30(Doc.of(Level.WARNING) //
			.text("BECU1LowVoltageOffset")), //
	STATE_31(Doc.of(Level.WARNING) //
			.text("BECU1HighTemperatureOffset")), //
	STATE_32(Doc.of(Level.FAULT) //
			.text("BECU1DischargeSevereOvercurrent")), //
	STATE_33(Doc.of(Level.FAULT) //
			.text("BECU1ChargeSevereOvercurrent")), //
	STATE_34(Doc.of(Level.FAULT) //
			.text("BECU1GeneralUndervoltage")), //
	STATE_35(Doc.of(Level.FAULT) //
			.text("BECU1SevereOvervoltage")), //
	STATE_36(Doc.of(Level.FAULT) //
			.text("BECU1GeneralOvervoltage")), //
	STATE_37(Doc.of(Level.FAULT) //
			.text("BECU1SevereUndervoltage")), //
	STATE_38(Doc.of(Level.FAULT) //
			.text("BECU1InsideCANBroken")), //
	STATE_39(Doc.of(Level.FAULT) //
			.text("BECU1GeneralUndervoltageHighCurrentDischarge")), //
	STATE_40(Doc.of(Level.FAULT) //
			.text("BECU1BMUError")), //
	STATE_41(Doc.of(Level.FAULT) //
			.text("BECU1CurrentSamplingInvalidation")), //
	STATE_42(Doc.of(Level.FAULT) //
			.text("BECU1BatteryFail")), //
	STATE_43(Doc.of(Level.FAULT) //
			.text("BECU1TemperatureSamplingBroken")), //
	STATE_44(Doc.of(Level.FAULT) //
			.text("BECU1Contactor1TestBackIsAbnormalTurnOnAbnormity")), //
	STATE_45(Doc.of(Level.FAULT) //
			.text("BECU1Contactor1TestBackIsAbnormalTurnOffAbnormity")), //
	STATE_46(Doc.of(Level.FAULT) //
			.text("BECU1Contactor2TestBackIsAbnormalTurnOnAbnormity")), //
	STATE_47(Doc.of(Level.FAULT) //
			.text("BECU1Contactor2TestBackIsAbnormalTurnOffAbnormity")), //
	STATE_48(Doc.of(Level.FAULT) //
			.text("BECU1SevereHighTemperatureFault")), //
	STATE_49(Doc.of(Level.FAULT) //
			.text("BECU1HallInvalidation")), //
	STATE_50(Doc.of(Level.FAULT) //
			.text("BECU1ContactorInvalidation")), //
	STATE_51(Doc.of(Level.FAULT) //
			.text("BECU1OutsideCANBroken")), //
	STATE_52(Doc.of(Level.FAULT) //
			.text("BECU1CathodeContactorBroken")), //

	STATE_53(Doc.of(Level.WARNING) //
			.text("BECU2GeneralChargeOverCurrentAlarm")), //
	STATE_54(Doc.of(Level.WARNING) //
			.text("BECU2GeneralDischargeOverCurrentAlarm")), //
	STATE_55(Doc.of(Level.WARNING) //
			.text("BECU2ChargeCurrentLimitAlarm")), //
	STATE_56(Doc.of(Level.WARNING) //
			.text("BECU2DischargeCurrentLimitAlarm")), //
	STATE_57(Doc.of(Level.WARNING) //
			.text("BECU2GeneralHighVoltageAlarm")), //
	STATE_58(Doc.of(Level.WARNING) //
			.text("BECU2GeneralLowVoltageAlarm")), //
	STATE_59(Doc.of(Level.WARNING) //
			.text("BECU2AbnormalVoltageChangeAlarm")), //
	STATE_60(Doc.of(Level.WARNING) //
			.text("BECU2GeneralHighTemperatureAlarm")), //
	STATE_61(Doc.of(Level.WARNING) //
			.text("BECU2GeneralLowTemperatureAlarm")), //
	STATE_62(Doc.of(Level.WARNING) //
			.text("BECU2AbnormalTemperatureChangeAlarm")), //
	STATE_63(Doc.of(Level.WARNING) //
			.text("BECU2SevereHighVoltageAlarm")), //
	STATE_64(Doc.of(Level.WARNING) //
			.text("BECU2SevereLowVoltageAlarm")), //
	STATE_65(Doc.of(Level.WARNING) //
			.text("BECU2SevereLowTemperatureAlarm")), //
	STATE_66(Doc.of(Level.WARNING) //
			.text("BECU2SeverveChargeOverCurrentAlarm")), //
	STATE_67(Doc.of(Level.WARNING) //
			.text("BECU2SeverveDischargeOverCurrentAlarm")), //
	STATE_68(Doc.of(Level.WARNING) //
			.text("BECU2AbnormalCellCapacityAlarm")), //
	STATE_69(Doc.of(Level.WARNING) //
			.text("BECU2BalancedSamplingAlarm")), //
	STATE_70(Doc.of(Level.WARNING) //
			.text("BECU2BalancedControlAlarm")), //
	STATE_71(Doc.of(Level.WARNING) //
			.text("BECU2HallSensorDoesNotWorkAccurately")), //
	STATE_72(Doc.of(Level.WARNING) //
			.text("BECU2Generalleakage")), //
	STATE_73(Doc.of(Level.WARNING) //
			.text("BECU2Severeleakage")), //
	STATE_74(Doc.of(Level.WARNING) //
			.text("BECU2Contactor1TurnOnAbnormity")), //
	STATE_75(Doc.of(Level.WARNING) //
			.text("BECU2Contactor1TurnOffAbnormity")), //
	STATE_76(Doc.of(Level.WARNING) //
			.text("BECU2Contactor2TurnOnAbnormity")), //
	STATE_77(Doc.of(Level.WARNING) //
			.text("BECU2Contactor2TurnOffAbnormity")), //
	STATE_78(Doc.of(Level.WARNING) //
			.text("BECU2Contactor4CheckAbnormity")), //
	STATE_79(Doc.of(Level.WARNING) //
			.text("BECU2ContactorCurrentUnsafe")), //
	STATE_80(Doc.of(Level.WARNING) //
			.text("BECU2Contactor5CheckAbnormity")), //
	STATE_81(Doc.of(Level.WARNING) //
			.text("BECU2HighVoltageOffset")), //
	STATE_82(Doc.of(Level.WARNING) //
			.text("BECU2LowVoltageOffset")), //
	STATE_83(Doc.of(Level.WARNING) //
			.text("BECU2HighTemperatureOffset")), //
	STATE_84(Doc.of(Level.FAULT) //
			.text("BECU2DischargeSevereOvercurrent")), //
	STATE_85(Doc.of(Level.FAULT) //
			.text("BECU2ChargeSevereOvercurrent")), //
	STATE_86(Doc.of(Level.FAULT) //
			.text("BECU2GeneralUndervoltage")), //
	STATE_87(Doc.of(Level.FAULT) //
			.text("BECU2SevereOvervoltage")), //
	STATE_88(Doc.of(Level.FAULT) //
			.text("BECU2GeneralOvervoltage")), //
	STATE_89(Doc.of(Level.FAULT) //
			.text("BECU2SevereUndervoltage")), //
	STATE_90(Doc.of(Level.FAULT) //
			.text("BECU2InsideCANBroken")), //
	STATE_91(Doc.of(Level.FAULT) //
			.text("BECU2GeneralUndervoltageHighCurrentDischarge")), //
	STATE_92(Doc.of(Level.FAULT) //
			.text("BECU2BMUError")), //
	STATE_93(Doc.of(Level.FAULT) //
			.text("BECU2CurrentSamplingInvalidation")), //
	STATE_94(Doc.of(Level.FAULT) //
			.text("BECU2BatteryFail")), //
	STATE_95(Doc.of(Level.FAULT) //
			.text("BECU2TemperatureSamplingBroken")), //
	STATE_96(Doc.of(Level.FAULT) //
			.text("BECU2Contactor1TestBackIsAbnormalTurnOnAbnormity")), //
	STATE_97(Doc.of(Level.FAULT) //
			.text("BECU2Contactor1TestBackIsAbnormalTurnOffAbnormity")), //
	STATE_98(Doc.of(Level.FAULT) //
			.text("BECU2Contactor2TestBackIsAbnormalTurnOnAbnormity")), //
	STATE_99(Doc.of(Level.FAULT) //
			.text("BECU2Contactor2TestBackIsAbnormalTurnOffAbnormity")), //
	STATE_100(Doc.of(Level.FAULT) //
			.text("BECU2SevereHighTemperatureFault")), //
	STATE_101(Doc.of(Level.FAULT) //
			.text("BECU2HallInvalidation")), //
	STATE_102(Doc.of(Level.FAULT) //
			.text("BECU2ContactorInvalidation")), //
	STATE_103(Doc.of(Level.FAULT) //
			.text("BECU2OutsideCANBroken")), //
	STATE_104(Doc.of(Level.FAULT) //
			.text("BECU2CathodeContactorBroken")), //

	STATE_105(Doc.of(Level.FAULT) //
			.text("NoAvailableBatteryGroup")), //
	STATE_106(Doc.of(Level.FAULT) //
			.text("StackGeneralLeakage")), //
	STATE_107(Doc.of(Level.FAULT) //
			.text("StackSevereLeakage")), //
	STATE_108(Doc.of(Level.FAULT) //
			.text("StackStartingFail")), //
	STATE_109(Doc.of(Level.FAULT) //
			.text("StackStoppingFail")), //
	STATE_110(Doc.of(Level.FAULT) //
			.text("BatteryProtection")), //
	STATE_111(Doc.of(Level.FAULT) //
			.text("StackAndGroup1CANCommunicationInterrupt")), //
	STATE_112(Doc.of(Level.FAULT) //
			.text("StackAndGroup2CANCommunicationInterrupt")), //
	STATE_113(Doc.of(Level.WARNING) //
			.text("GeneralOvercurrentAlarmAtCellStackCharge")), //
	STATE_114(Doc.of(Level.WARNING) //
			.text("GeneralOvercurrentAlarmAtCellStackDischarge")), //
	STATE_115(Doc.of(Level.WARNING) //
			.text("CurrentLimitAlarmAtCellStackCharge")), //
	STATE_116(Doc.of(Level.WARNING) //
			.text("CurrentLimitAlarmAtCellStackDischarge")), //
	STATE_117(Doc.of(Level.WARNING) //
			.text("GeneralCellStackHighVoltageAlarm")), //
	STATE_118(Doc.of(Level.WARNING) //
			.text("GeneralCellStackLowVoltageAlarm")), //
	STATE_119(Doc.of(Level.WARNING) //
			.text("AbnormalCellStackVoltageChangeAlarm")), //
	STATE_120(Doc.of(Level.WARNING) //
			.text("GeneralCellStackHighTemperatureAlarm")), //
	STATE_121(Doc.of(Level.WARNING) //
			.text("GeneralCellStackLowTemperatureAlarm")), //
	STATE_122(Doc.of(Level.WARNING) //
			.text("AbnormalCellStackTemperatureChangeAlarm")), //
	STATE_123(Doc.of(Level.WARNING) //
			.text("SevereCellStackHighVoltageAlarm")), //
	STATE_124(Doc.of(Level.WARNING) //
			.text("SevereCellStackLowVoltageAlarm")), //
	STATE_125(Doc.of(Level.WARNING) //
			.text("SevereCellStackLowTemperatureAlarm")), //
	STATE_126(Doc.of(Level.WARNING) //
			.text("SeverveOverCurrentAlarmAtCellStackDharge")), //
	STATE_127(Doc.of(Level.WARNING) //
			.text("SeverveOverCurrentAlarmAtCellStackDischarge")), //
	STATE_128(Doc.of(Level.WARNING) //
			.text("AbnormalCellStackCapacityAlarm")), //
	STATE_129(Doc.of(Level.WARNING) //
			.text("TheParameterOfEEPROMInCellStackLoseEffectiveness")), //
	STATE_130(Doc.of(Level.WARNING) //
			.text("IsolatingSwitchInConfluenceArkBreak")), //
	STATE_131(Doc.of(Level.WARNING) //
			.text("TheCommunicationBetweenCellStackAndTemperatureOfCollectorBreak")), //
	STATE_132(Doc.of(Level.WARNING) //
			.text("TheTemperatureOfCollectorFail")), //
	STATE_133(Doc.of(Level.WARNING) //
			.text("HallSensorDoNotWorkAccurately")), //
	STATE_134(Doc.of(Level.WARNING) //
			.text("TheCommunicationOfPCSBreak")), //
	STATE_135(Doc.of(Level.WARNING) //
			.text("AdvancedChargingOrMainContactorCloseAbnormally")), //
	STATE_136(Doc.of(Level.WARNING) //
			.text("AbnormalSampledVoltage")), //
	STATE_137(Doc.of(Level.WARNING) //
			.text("AbnormalAdvancedContactorOrAbnormalRS485GalleryOfPCS")), //
	STATE_138(Doc.of(Level.WARNING) //
			.text("AbnormalMainContactor")), //
	STATE_139(Doc.of(Level.WARNING) //
			.text("GeneralCellStackLeakage")), //
	STATE_140(Doc.of(Level.WARNING) //
			.text("SevereCellStackLeakage")), //
	STATE_141(Doc.of(Level.WARNING) //
			.text("SmokeAlarm")), //
	STATE_142(Doc.of(Level.WARNING) //
			.text("TheCommunicationWireToAmmeterBreak")), //
	STATE_143(Doc.of(Level.INFO) //
			.text("TheCommunicationWireToDredBreak"));

	private final Doc doc;

	private EssChannelId(Doc doc) {
		this.doc = doc;
	}

	@Override
	public Doc doc() {
		return this.doc;
	}
}