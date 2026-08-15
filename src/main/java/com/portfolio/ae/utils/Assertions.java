package com.portfolio.ae.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Single point of assertions for step definitions (PLAN.md section 5: "Page Objects only
 * expose actions and state queries; assertions live in step definitions"). Centralizing
 * logging here gives uniform traceability of what was validated and why it failed, without
 * tying each feature to a different assertions framework.
 */
public final class Assertions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Assertions.class);

    private Assertions() {
    }

    public static void assertEquals(Object actual, Object expected, String message) {
        LOGGER.info("Assert equals -> {} | actual='{}' expected='{}'", message, actual, expected);
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (AssertionError e) {
            LOGGER.error("Assert equals FAILED -> {} | actual='{}' expected='{}'", message, actual, expected);
            throw e;
        }
    }

    public static void assertTrue(boolean condition, String message) {
        LOGGER.info("Assert true -> {} | condition={}", message, condition);
        if (!condition) {
            LOGGER.error("Assert true FAILED -> {}", message);
        }
        Assert.assertTrue(condition, message);
    }

    public static void assertFalse(boolean condition, String message) {
        LOGGER.info("Assert false -> {} | condition={}", message, condition);
        if (condition) {
            LOGGER.error("Assert false FAILED -> {}", message);
        }
        Assert.assertFalse(condition, message);
    }

    public static void assertContains(String actual, String expectedSubstring, String message) {
        boolean contains = actual != null && actual.contains(expectedSubstring);
        LOGGER.info("Assert contains -> {} | actual='{}' expectedSubstring='{}'", message, actual, expectedSubstring);
        if (!contains) {
            LOGGER.error("Assert contains FAILED -> {} | actual='{}' expectedSubstring='{}'", message, actual, expectedSubstring);
        }
        Assert.assertTrue(contains, message);
    }

    public static void fail(String message) {
        LOGGER.error("Assert fail -> {}", message);
        Assert.fail(message);
    }
}
