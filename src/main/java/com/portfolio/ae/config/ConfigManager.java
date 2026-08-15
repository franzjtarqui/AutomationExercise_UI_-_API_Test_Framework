package com.portfolio.ae.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Single point of access to the framework's configuration (PLAN.md section 7).
 * <p>
 * Resolution precedence for each value (from highest to lowest priority):
 * <ol>
 *   <li>System property ({@code -Dkey=value})</li>
 *   <li>Environment variable (useful for CI / GitHub Secrets)</li>
 *   <li>{@code config.properties} (classpath: {@code config/config.properties})</li>
 *   <li>Default value</li>
 * </ol>
 * This allows, for example, running in CI with {@code -Dbrowser=chrome -Dheadless=true}
 * without touching the versioned file.
 */
public final class ConfigManager {

    private static final String CONFIG_RESOURCE = "config/config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private ConfigManager() {
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_RESOURCE)) {
            if (input == null) {
                throw new IllegalStateException("Could not find '" + CONFIG_RESOURCE + "' on the classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Error reading '" + CONFIG_RESOURCE + "'", e);
        }
        return properties;
    }

    private static String resolve(String propertyKey, String envVarName, String defaultValue) {
        String systemProperty = System.getProperty(propertyKey);
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }
        String envVar = System.getenv(envVarName);
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }
        return PROPERTIES.getProperty(propertyKey, defaultValue);
    }

    public static Environment getEnvironment() {
        return Environment.fromCode(resolve("env", "ENV", "prod"));
    }

    public static String getBaseUrlUi() {
        String envCode = getEnvironment().getCode();
        return resolve(envCode + ".base.url.ui", "BASE_URL_UI", null);
    }

    public static String getBaseUrlApi() {
        String envCode = getEnvironment().getCode();
        return resolve(envCode + ".base.url.api", "BASE_URL_API", null);
    }

    public static BrowserType getBrowser() {
        return BrowserType.fromCode(resolve("browser", "BROWSER", "chrome"));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(resolve("headless", "HEADLESS", "false"));
    }

    public static int getExplicitWaitSeconds() {
        return Integer.parseInt(resolve("timeout.explicit.seconds", "TIMEOUT_EXPLICIT_SECONDS", "15"));
    }

    public static int getPollingMillis() {
        return Integer.parseInt(resolve("timeout.polling.millis", "TIMEOUT_POLLING_MILLIS", "250"));
    }
}
