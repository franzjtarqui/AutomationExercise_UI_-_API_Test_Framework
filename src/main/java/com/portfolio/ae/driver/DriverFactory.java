package com.portfolio.ae.driver;

import com.portfolio.ae.config.BrowserType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Creates {@link WebDriver} instances. Uses Selenium Manager (native since Selenium 4.6+)
 * to resolve the browser/driver binary: no need for WebDriverManager or manually configuring
 * paths (PLAN.md section 1).
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver(BrowserType browserType, boolean headless) {
        return switch (browserType) {
            case CHROME -> new ChromeDriver(chromeOptions(headless));
            case FIREFOX -> new FirefoxDriver(firefoxOptions(headless));
            case EDGE -> new EdgeDriver(edgeOptions(headless));
        };
    }

    // Automation fingerprint hidden from the page (navigator.webdriver, "Chrome is being
    // controlled by automated test software" banner, etc.). automationexercise.com sits behind
    // Cloudflare, which challenges/blocks the default headless Selenium fingerprint much more
    // aggressively when the request comes from a data-center IP range (e.g. GitHub Actions
    // runners) than from a residential/local IP; these flags plus a realistic desktop user agent
    // make headless Chrome look like a normal browser and avoid that block in CI.
    private static final String DESKTOP_USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
                    + "Chrome/131.0.0.0 Safari/537.36";

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[] {"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--user-agent=" + DESKTOP_USER_AGENT);
            // Required on CI runners (Linux containers without a generous /dev/shm or user sandbox).
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920", "--height=1080");
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--window-size=1920,1080");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return options;
    }
}
