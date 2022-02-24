package io.openems.edge.consolinno.leaflet.core.api;

public enum Error {
    //65535 = 0xFFFF
    ERROR(65535),
    NOT_CONNECTED(-1);
    private final int value;

    private Error(int value) {
        this.value = value;

    }

    /**
     * Get Error Value.
     *
     * @return Value
     */
    public int getValue() {
        return this.value;
    }
}
