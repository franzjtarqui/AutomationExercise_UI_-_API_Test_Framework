package com.portfolio.ae.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Runner for API scenarios (REST Assured + Cucumber).
 * The parallel data provider enables concurrent execution per scenario (PLAN.md decision, section 1).
 */
@CucumberOptions(
        features = "src/test/resources/features/api",
        glue = {"com.portfolio.ae.stepdefs.api", "com.portfolio.ae.hooks"},
        tags = "@api",
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        publish = false
)
public class ApiRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
