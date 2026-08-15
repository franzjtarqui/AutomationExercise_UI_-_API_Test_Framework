package com.portfolio.ae.utils;

import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class JsonReaderTest {

    @Test
    public void readsStringMapFixtureFromClasspath() {
        Map<String, String> messages = JsonReader.readStringMap("testdata/expected_messages.json");

        assertNotNull(messages);
        assertEquals(messages.get("accountCreated"), "Account Created!");
        assertEquals(messages.get("orderPlaced"), "Order Placed!");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void throwsWhenResourceDoesNotExist() {
        JsonReader.readStringMap("testdata/does_not_exist.json");
    }
}
