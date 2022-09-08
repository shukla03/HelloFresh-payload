package com.hellofresh.datastats.service;


import com.hellofresh.datastats.model.DataEvent;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


class PayloadServiceTest {

    private static String validYString = "1852154378";
    private static String invalidYString = "111212767";
    private static String TIMESTAMP_EXCEPTION = "TIMESTAMP_EXCEPTION";
    private static String INVALID_X_EXCEPTION = "INVALID_X_EXCEPTION";
    private static String INVALID_Y_EXCEPTION = "INVALID_Y_EXCEPTION";

    private static String validXString = "0.0442672968";
    private static String invalidXString = "0.0442672968999999999999999";

    private static String validTsString = "1607341341814";
    private static String invalidTsString = "timestamp";

    private static String payloadWithInvalidData = "1607341331814,0.0899538547,1852154378\n" + "1607341271814,0.0586780608,111212767\n" + "1607341261814,0.0231608748,1539565646";

    @Test
    public void validYisValid() throws InvalidYException {
        PayloadService payloadService = new PayloadService();
        boolean result = payloadService.validY(validYString);
        assertTrue(result);
    }

    @Test
    public void invalidYisInvalid() throws InvalidYException {
        PayloadService payloadService = new PayloadService();
        InvalidYException e = assertThrows(InvalidYException.class, () -> payloadService.validY(invalidYString));
        String expectedMessage = "Value " + invalidYString + " of y is out of range";
        assertEquals(expectedMessage, e.getMessage());
        String expectedCode = INVALID_Y_EXCEPTION;
        assertEquals(expectedCode, e.getCode());
    }

    @Test
    public void validXisValid() throws InvalidXException {
        PayloadService payloadService = new PayloadService();
        boolean result = payloadService.validX(validXString);
        assertTrue(result);
    }

    @Test
    public void invalidXisInvalid() throws InvalidXException {
        PayloadService payloadService = new PayloadService();
        InvalidXException e = assertThrows(InvalidXException.class, () -> payloadService.validX(invalidXString));
        String expectedMessage = "The fractional part or x : " + invalidXString + "  is more than 10 digits ";
        assertEquals(expectedMessage, e.getMessage());
        String expectedCode = INVALID_X_EXCEPTION;
        assertEquals(expectedCode, e.getCode());
    }

    @Test
    public void createTimestampForValidString() throws InvalidTimeStampException {
        PayloadService payloadService = new PayloadService();
        Timestamp result = payloadService.createTimestampFromString(validTsString);
        assertSame(result.getClass(), Timestamp.class);
    }

    @Test
    public void canNotCreateTimestampForValidString() throws InvalidTimeStampException {
        PayloadService payloadService = new PayloadService();
        InvalidTimeStampException e = assertThrows(InvalidTimeStampException.class, () -> payloadService.createTimestampFromString(invalidTsString));
        String expectedMessage = "Couldn't create a Timestamp for input: " + invalidTsString;
        assertEquals(expectedMessage, e.getMessage());
        String expectedCode = TIMESTAMP_EXCEPTION;
        assertEquals(expectedCode, e.getCode());
    }

    @Test
    public void processOnlyValidData() {
        PayloadService payloadService = new PayloadService();
        List<DataEvent> events = payloadService.processPayload(payloadWithInvalidData);
        assertEquals(events.size(), 2);
        assertTrue(events.stream().filter( e -> e.getY() == 111212767).collect(Collectors.toList()).isEmpty());
    }



}