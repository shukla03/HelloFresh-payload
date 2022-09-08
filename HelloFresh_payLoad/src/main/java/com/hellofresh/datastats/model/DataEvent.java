package com.hellofresh.datastats.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;


public class DataEvent {
    private final Timestamp timestamp;
    private final double x; // Double stores up to 15 decimal digits. // Be careful when calculating the AVG (0,0000000001 / 1000000 = 16 decimal digits ) // BigDecimal?
    private final long y;

    public DataEvent(@JsonProperty("timestamp") Timestamp timestamp,
                     @JsonProperty("x")double x,
                     @JsonProperty("y")long y) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public double getX() {
        return x;
    }

    public long getY() {
        return y;
    }

}
