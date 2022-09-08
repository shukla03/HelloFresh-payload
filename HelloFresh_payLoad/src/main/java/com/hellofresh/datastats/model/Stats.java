package com.hellofresh.datastats.model;

import java.util.Locale;

public class Stats {

    public Stats(int total, double sumX, double avgX, long sumY, double avgY) {
        this.total = total;
        this.sumX = sumX;
        this.avgX = avgX;
        this.sumY = sumY;
        this.avgY = avgY;
    }

    private int total;
    private double sumX;
    private double avgX;
    private long sumY;
    private double avgY;

    public String printResponse() {
        String avYString = String.format(Locale.ROOT,"%.3f\n", avgY);
        return total+","+sumX+","+avgX+","+sumY+","+avYString+"\n";
    }

    public int getTotal() {
        return total;
    }

    public double getSumX() {
        return sumX;
    }

    public double getAvgX() {
        return avgX;
    }

    public long getSumY() {
        return sumY;
    }

    public double getAvgY() {
        return avgY;
    }
}
