package com.hellofresh.datastats.service;

import com.hellofresh.datastats.model.Stats;
import com.hellofresh.datastats.model.DataEvent;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EventService {
    private Timestamp lastUpdate;
    private int[] countX;
    private int[] countY;
    private double[] sumX;
    private long[] sumY;

    private final int offset = 60;

    public EventService() {
        this.countX = new int[offset];
        this.countY = new int[offset];
        this.sumX = new double[offset];
        this.sumY = new long[offset];
        this.lastUpdate = null;
    }

    public void addEvents(List<DataEvent> events) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        updateData(events, now);
    }

    public synchronized void updateData(List<DataEvent> events, Timestamp now) {
        if (this.lastUpdate == null)  {this.lastUpdate = now;}

        Integer diffPrevUpdate = getDifferenceInSeconds(now, this.lastUpdate);

        if (diffPrevUpdate > offset ) {
            // Stored Data is too old, so can set all values in arrays to be zero again
            Arrays.fill(countX, 0);
            Arrays.fill(sumX, 0);
            Arrays.fill(countY, 0);
            Arrays.fill(sumY, 0);

        } else if (diffPrevUpdate > 0) {
            // There is some data stored that is still in range of 60 seconds and I need to use. Some other data is old and I have to delete.
            // Will shift the array to save the values that are still in range of the 60 seconds and put 0s in the outdated positions.

            Integer lastValid = offset - diffPrevUpdate - 1; // Index of last valid second

            for (int i = lastValid; i > 0; i--) {
                countX[i+diffPrevUpdate] = countY[i];
                sumX[i+diffPrevUpdate] = sumX[i];
                countY[i+diffPrevUpdate] = countY[i];
                sumY[i+diffPrevUpdate] = sumY[i];
            }

            for (int i = 0; i < diffPrevUpdate; i++){
                countX[i] = 0;
                sumX[i] = 0;
                countY[i] = 0;
                sumY[i] = 0;
            }

        }

        events.forEach(event -> {
            Integer diff = getDifferenceInSeconds(now, event.getTimestamp());
            if (diff >= 0 && diff < 60){ // More than 60 secs diffs events are discarded. Negative diff are events with a future timeStamp which should occur.
                countX[diff]= countX[diff] + 1;
                sumX[diff]+=event.getX();

                countY[diff] = countY[diff] + 1;
                sumY[diff]+=event.getY();
            }
        });

        this.lastUpdate = now;
    }


    public Integer getDifferenceInSeconds(Timestamp now, Timestamp eventTimeStamp){
        Long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - eventTimeStamp.getTime());
        Integer diff = seconds.intValue();
        return diff;
    }

    public synchronized Stats getStats() {
        int total = Arrays.stream(countX).sum();
        double sumOfX = Arrays.stream(sumX).sum();
        long sumOfY = Arrays.stream(sumY).sum();
        double avgX = sumOfX / total;
        double avgY = (double)sumOfY / (double)total;
        return new Stats(total, sumOfX, avgX, sumOfY, avgY);
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public int[] getCountX() {
        return countX;
    }

    public int[] getCountY() {
        return countY;
    }

    public double[] getSumX() {
        return sumX;
    }

    public long[] getSumY() {
        return sumY;
    }

    public int getOffset() {
        return offset;
    }


}