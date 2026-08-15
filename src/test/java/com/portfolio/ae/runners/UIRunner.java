package com.portfolio.ae.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Runner for UI scenarios (Selenium + Cucumber).
 * The parallel data provider enables concurrent execution per scenario (PLAN.md decision, section 1).
 */
@CucumberOptions(
        features = "src/test/resources/features/ui",
        glue = {"com.portfolio.ae.stepdefs.ui", "com.portfolio.ae.hooks"},
        tags = "@ui",
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        publish = false
)
public class UIRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
