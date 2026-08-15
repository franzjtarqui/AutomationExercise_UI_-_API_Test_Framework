package com.portfolio.ae.config;

import java.util.Arrays;

/**
 * Browsers supported by the DriverFactory (Selenium Manager resolves the binary/driver).
 */
public enum BrowserType {

    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");

    private final String code;

    BrowserType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BrowserType fromCode(String code) {
        return Arrays.stream(values())
                .filter(browser -> browser.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown browser: '" + code + "'. Supported values: " + Arrays.toString(values())));
    }
}
