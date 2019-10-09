package io.openems.edge.raspberrypi.sensor.api.Adc;

import io.openems.edge.raspberrypi.sensor.api.Adc.Pins.Pin;
import io.openems.edge.raspberrypi.sensor.api.Board;
import io.openems.edge.raspberrypi.spi.SpiInitial;
import io.openems.edge.raspberrypi.spi.SpiInitialImpl;
import jdk.nashorn.internal.ir.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

public abstract class Adc {
    //TODO Add Input Type --> 12 bit or so for each concrete MCP
    //TODO IMPORTANT!!!! Adc needs SPI channel not the sensor!!!! remember!!
    @Reference
    protected SpiInitialImpl spiInitial;
    //Adc abstract class: Created by Sensor Type
    private List<Pin> pins = new ArrayList<>();
    private int spiChannel;
    private int inputType;
    private byte MAX_SIZE;
    private Board board;
    private int id;

    public Adc(List<Long> pins,int inputType, byte max_size, Board board, int id, int spiChannel) {
        this.MAX_SIZE = max_size;
        this.board = board;
        this.id = id;
        int position = 0;

        for (long l : pins) {

            this.pins.add(new Pin(l, position++));
        }
        this.spiChannel = spiChannel;
        this.inputType = inputType;



        //if(this.spiInitial.addAdcList(this)){

            //TODO
            //OpenSpiChannel --> SpiChannelForNewMcp
       // }

    }



    public Board getBoard() {
        return board;
    }

    public int getId() {
        return id;
    }

    public List<Pin> getPins() {
        return pins;
    }

    public int getSpiChannel() {
        return spiChannel;
    }

    public int getInputType() {
        return inputType;
    }
}
