package com.portfolio.ae.stepdefs.ui;

import com.portfolio.ae.data.AccountDetails;
import com.portfolio.ae.data.ExpectedMessages;
import com.portfolio.ae.data.UserDataFactory;
import com.portfolio.ae.driver.DriverManager;
import com.portfolio.ae.ui.components.HeaderComponent;
import com.portfolio.ae.ui.pages.AccountCreatedPage;
import com.portfolio.ae.ui.pages.HomePage;
import com.portfolio.ae.ui.pages.LoginSignupPage;
import com.portfolio.ae.ui.pages.SignupDetailsPage;
import com.portfolio.ae.utils.Assertions;
import com.portfolio.ae.utils.FakerUtil;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Step definitions for {@code login.feature}. The "seed" user is created via UI in
 * {@link #a_registered_user_with_known_credentials()} because the site has no persistent test
 * account that can be used as a fixture (Data layer, PLAN.md section 7): the data comes from
 * {@link UserDataFactory} and the expected texts from {@link ExpectedMessages}.
 */
public class LoginSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginSteps.class);

    private LoginSignupPage loginSignupPage;
    private AccountDetails accountDetails;
    private boolean accountPendingCleanup;

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    @Given("a registered user with known credentials")
    public void a_registered_user_with_known_credentials() {
        loginSignupPage = new LoginSignupPage(driver());
        loginSignupPage.open();
        accountDetails = UserDataFactory.randomUser();
        loginSignupPage.signup(accountDetails.name(), accountDetails.email());
        SignupDetailsPage signupDetailsPage = new SignupDetailsPage(driver());
        signupDetailsPage.fillAccountDetails(accountDetails);
        signupDetailsPage.submit();
        AccountCreatedPage accountCreatedPage = new AccountCreatedPage(driver());
        Assertions.assertContains(accountCreatedPage.getConfirmationMessage().toUpperCase(),
                ExpectedMessages.accountCreated().toUpperCase(),
                "The seed user should have been created successfully before testing login");
        accountPendingCleanup = true;
        HomePage homePage = accountCreatedPage.clickContinue();
        homePage.header().clickLogout();
    }

    @When("I log in with valid credentials")
    public void i_log_in_with_valid_credentials() {
        loginSignupPage = new LoginSignupPage(driver());
        loginSignupPage.open();
        loginSignupPage.login(accountDetails.email(), accountDetails.password());
    }

    @Then("I should be logged in as that user")
    public void i_should_be_logged_in_as_that_user() {
        HeaderComponent header = new HeaderComponent(driver());
        Assertions.assertTrue(header.isLoggedIn(), "'Logged in as' should be visible in the header after login");
        Assertions.assertEquals(header.getLoggedInUserName(), accountDetails.name(),
                "The logged-in name in the header should match the created user");
    }

    @When("I try to log in with invalid credentials")
    public void i_try_to_log_in_with_invalid_credentials() {
        loginSignupPage = new LoginSignupPage(driver());
        loginSignupPage.open();
        loginSignupPage.login(FakerUtil.uniqueEmail(), "WrongPassword123!");
    }

    @Then("I should see the login error message")
    public void i_should_see_the_login_error_message() {
        Assertions.assertContains(loginSignupPage.getLoginErrorMessage(), ExpectedMessages.loginError(),
                "The invalid credentials error message should be visible");
    }

    @When("I log out")
    public void i_log_out() {
        new HeaderComponent(driver()).clickLogout();
    }

    @Then("I should not be logged in anymore")
    public void i_should_not_be_logged_in_anymore() {
        HeaderComponent header = new HeaderComponent(driver());
        Assertions.assertTrue(header.isSignupLoginVisible(), "The Signup/Login link should be visible after logout");
        Assertions.assertFalse(header.isLoggedIn(), "'Logged in as' should not be visible after logout");
    }

    // High order => runs before Hooks.tearDownDriver (order=0), needs the driver still alive.
    @After(value = "@auth", order = 100)
    public void cleanupLeftoverAccount() {
        if (!accountPendingCleanup || accountDetails == null) {
            return;
        }
        try {
            LoginSignupPage cleanupLogin = new LoginSignupPage(driver());
            cleanupLogin.open();
            cleanupLogin.login(accountDetails.email(), accountDetails.password());
            new HeaderComponent(driver()).clickDeleteAccount();
        } catch (RuntimeException cleanupException) {
            LOGGER.warn("Best-effort cleanup of account '{}' failed, it may remain orphaned: {}",
                    accountDetails.email(), cleanupException.getMessage());
        } finally {
            accountPendingCleanup = false;
        }
    }
}
