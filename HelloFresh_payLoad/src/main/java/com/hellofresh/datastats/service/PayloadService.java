package com.hellofresh.datastats.service;



import com.hellofresh.datastats.model.DataEvent;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayloadService {

    private static String TIMESTAMP_EXCEPTION = "TIMESTAMP_EXCEPTION";
    private static String INVALID_X_EXCEPTION = "INVALID_X_EXCEPTION";
    private static String INVALID_Y_EXCEPTION = "INVALID_Y_EXCEPTION";

    private static int MIN_RANGE = 1073741823;
    private static int MAX_RANGE = 2147483647;

    public List<DataEvent> processPayload(String payload) {
        List<DataEvent> events = new ArrayList<>();
        String[] lines = payload.split("\n");
        for (String line : lines) {
            String[] data = line.split(",");
            String x = data[1];
            String y = data[2];
            try {
                Timestamp ts = createTimestampFromString(data[0]);
                if (valid(x, y, ts)){
                    DataEvent event = new DataEvent(ts, Double.parseDouble(x), Integer.parseInt(y));
                    events.add(event);
                }
            } catch (Exception e) {
                System.out.println(e.getClass());
            }
        }

        return events;
    }

    public boolean valid(String x, String y, Timestamp ts) throws InvalidXException, InvalidYException {
        return validX(x) && validY(y) && (ts.getClass() == Timestamp.class);
    }

    public Timestamp createTimestampFromString(String stringUnixMs) throws InvalidTimeStampException {
        try {
            long unixTimestamp = Long.parseLong(stringUnixMs);
            return new Timestamp(unixTimestamp);
        } catch (Exception e) {
            throw new InvalidTimeStampException(TIMESTAMP_EXCEPTION, "Couldn't create a Timestamp for input: " + stringUnixMs);
        }
    }

    public boolean validX (String x) throws InvalidXException {
        int integerPlaces = x.indexOf('.');
        int decimalPlaces = x.length() - integerPlaces - 1;
        if (decimalPlaces > 10){
            throw new InvalidXException(INVALID_X_EXCEPTION, "The fractional part or x : " + x + "  is more than 10 digits ");
        }
        return true;
    }

    public boolean validY (String stringY) throws InvalidYException {
        int y = Integer.parseInt(stringY);
        if (y < MIN_RANGE || y > MAX_RANGE){
            throw new InvalidYException(INVALID_Y_EXCEPTION, "Value " + y + " of y is out of range");
        }
        return true;
    }

}