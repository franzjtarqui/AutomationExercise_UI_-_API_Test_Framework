package com.portfolio.ae.config;

import java.util.Arrays;

/**
 * Environments supported by the framework. automationexercise.com only has "prod",
 * but the enum leaves the extension point ready (adding QA/STAGING only requires
 * a new value here + the {@code <env>.base.url.*} block in config.properties).
 */
public enum Environment {

    PROD("prod");

    private final String code;

    Environment(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Environment fromCode(String code) {
        return Arrays.stream(values())
                .filter(env -> env.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown environment: '" + code + "'. Supported values: " + Arrays.toString(values())));
    }
}
