package io.github.jhoanhurtado.domain.models;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SmsMessageTest {
    @Test
    public void testGetContent() {
        SmsMessage smsMessage = new SmsMessage("1234567890", "Hello, World!");
        assertEquals("Hello, World!", smsMessage.getContent());
    }

    @Test
    public void testGetDestination() {
        SmsMessage smsMessage = new SmsMessage("1234567890", "Hello, World!");
        assertEquals("1234567890", smsMessage.getDestination());
    }
}
