package io.openems.edge.raspberrypi.sensor.api.Adc;

import io.openems.edge.raspberrypi.sensor.api.Adc.Pins.Pin;
import io.openems.edge.raspberrypi.sensor.api.Board;

import java.util.List;

public abstract class Typ4 extends Adc  {

<<<<<<< HEAD
    public Typ4(List<Long> pins,int inputType, Board board, int id, int SpiChannel) {
        super(pins, inputType ,(byte)4, board, id, SpiChannel );
=======
    public Typ4(List<Long> pins, Board board, int id) {
        super(pins, (byte)4, board, id);
>>>>>>> SPI
    }
}
