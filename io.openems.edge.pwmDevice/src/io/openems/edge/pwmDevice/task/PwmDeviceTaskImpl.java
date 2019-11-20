package io.openems.edge.pwmDevice.task;

import io.openems.common.exceptions.OpenemsError;
import io.openems.edge.bridgei2c.task.I2cTaskImpl;
import io.openems.edge.common.channel.WriteChannel;

public class PwmDeviceTaskImpl extends I2cTaskImpl {

    private WriteChannel<Float> powerLevel;
    private String pwmModule;
    private short pinPosition;
    private int offset;
    //    private int pulseDuration;
    private boolean isInverse;
    private float initialValue;
    private float scaling = 10.f;
    private boolean initalWasSet = false;
    private String deviceId;


    public PwmDeviceTaskImpl(String deviceId, WriteChannel<Float> powerLevel, String pwmModule, short pinPosition, boolean isInverse, float percentageValue) {
        super(pwmModule, deviceId);
        this.powerLevel = powerLevel;
        this.pwmModule = pwmModule;
        this.pinPosition = pinPosition;
        //   this.pulseDuration = pulseDuration;
        this.isInverse = isInverse;
        this.initialValue = percentageValue;
        this.deviceId = deviceId;
    }


    @Override
    public WriteChannel<Float> getFloatPowerLevel() {
        return powerLevel;
    }

    @Override
    public void setFloatPowerLevel(float powerLevel) throws OpenemsError.OpenemsNamedException {
        this.powerLevel.setNextWriteValue(powerLevel);
    }

    @Override
    public int getPinPosition() {
        return pinPosition;
    }

    @Override
    public boolean isInverse() {
        return isInverse;
    }

    @Override
    public int calculateDigit(int digitRange) {
        int digitValue = -5;
        float singleDigitValue = (float)(digitRange) / (100 * scaling);

        if (this.powerLevel.value().isDefined()) {

            float power = powerLevel.getNextValue().get();

//            float power = Float.parseFloat(powerLevel.value().get().toString().replaceAll("[a-zA-Z _%]", ""));
            if (isInverse) {
                power = 100 - power;
            }
            return (int) (power * singleDigitValue * scaling);
        } else {
            return digitValue;
        }
    }


}
