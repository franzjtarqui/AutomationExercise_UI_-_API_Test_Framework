package com.portfolio.ae.hooks;

import com.portfolio.ae.config.ConfigManager;
import com.portfolio.ae.driver.DriverFactory;
import com.portfolio.ae.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebDriver lifecycle for UI scenarios. Runs per scenario (not per feature) so that
 * per-scenario parallelism (testng.xml) doesn't share a browser across threads.
 */
public class Hooks {

    private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);

    @Before("@ui")
    public void setUpDriver() {
        DriverManager.setDriver(DriverFactory.createDriver(ConfigManager.getBrowser(), ConfigManager.isHeadless()));
    }

    // High order => runs first among the @After hooks (before any data cleanup), to capture
    // the page as it was at the moment of failure, not one already navigated away by a later
    // cleanup hook (PLAN.md section 9: "screenshots on failure").
    @After(value = "@ui", order = 200)
    public void takeScreenshotOnFailure(Scenario scenario) {
        if (!scenario.isFailed() || !DriverManager.hasDriver()) {
            return;
        }
        try {
            WebDriver driver = DriverManager.getDriver();
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "screenshot-on-failure");
        } catch (RuntimeException screenshotException) {
            LOGGER.warn("Could not capture the failure screenshot for scenario '{}': {}",
                    scenario.getName(), screenshotException.getMessage());
        }
    }

    // Low order => runs last among the @After hooks (which run in descending order), so data
    // cleanup hooks (e.g. RegistrationSteps) still have a driver available.
    @After(value = "@ui", order = 0)
    public void tearDownDriver() {
        DriverManager.quitDriver();
    }
}
