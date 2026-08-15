package com.portfolio.ae.stepdefs.ui;

import com.portfolio.ae.data.AccountDetails;
import com.portfolio.ae.data.ExpectedMessages;
import com.portfolio.ae.data.UserDataFactory;
import com.portfolio.ae.driver.DriverManager;
import com.portfolio.ae.ui.components.HeaderComponent;
import com.portfolio.ae.ui.pages.AccountCreatedPage;
import com.portfolio.ae.ui.pages.AccountDeletedPage;
import com.portfolio.ae.ui.pages.HomePage;
import com.portfolio.ae.ui.pages.LoginSignupPage;
import com.portfolio.ae.ui.pages.SignupDetailsPage;
import com.portfolio.ae.utils.Assertions;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Step definitions for {@code registration.feature}. The account data comes from
 * {@link UserDataFactory} and the expected texts from {@link ExpectedMessages} (Data layer,
 * PLAN.md section 7).
 */
public class RegistrationSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationSteps.class);

    private LoginSignupPage loginSignupPage;
    private SignupDetailsPage signupDetailsPage;
    private AccountCreatedPage accountCreatedPage;
    private AccountDeletedPage accountDeletedPage;
    private HomePage homePage;
    private AccountDetails accountDetails;
    private boolean accountPendingCleanup;

    private WebDriver driver() {
        return DriverManager.getDriver();
    }

    @Given("I am on the signup and login page")
    public void i_am_on_the_signup_and_login_page() {
        loginSignupPage = new LoginSignupPage(driver());
        loginSignupPage.open();
    }

    @When("I sign up with a new unique account")
    public void i_sign_up_with_a_new_unique_account() {
        accountDetails = UserDataFactory.randomUser();
        loginSignupPage.signup(accountDetails.name(), accountDetails.email());
        signupDetailsPage = new SignupDetailsPage(driver());
        signupDetailsPage.fillAccountDetails(accountDetails);
        signupDetailsPage.submit();
        accountCreatedPage = new AccountCreatedPage(driver());
        accountPendingCleanup = true;
    }

    @Then("I should see the account created confirmation")
    public void i_should_see_the_account_created_confirmation() {
        // The site renders the heading in uppercase via CSS (text-transform); we compare in
        // uppercase so we don't depend on that style.
        Assertions.assertContains(accountCreatedPage.getConfirmationMessage().toUpperCase(),
                ExpectedMessages.accountCreated().toUpperCase(), "The account created confirmation should be visible");
    }

    @When("I delete the account")
    public void i_delete_the_account() {
        homePage = accountCreatedPage.clickContinue();
        homePage.header().clickDeleteAccount();
        accountDeletedPage = new AccountDeletedPage(driver());
        accountPendingCleanup = false;
    }

    @Then("I should see the account deleted confirmation")
    public void i_should_see_the_account_deleted_confirmation() {
        Assertions.assertContains(accountDeletedPage.getConfirmationMessage().toUpperCase(),
                ExpectedMessages.accountDeleted().toUpperCase(), "The account deleted confirmation should be visible");
    }

    @Given("a user is already registered with a known email")
    public void a_user_is_already_registered_with_a_known_email() {
        loginSignupPage = new LoginSignupPage(driver());
        loginSignupPage.open();
        accountDetails = UserDataFactory.randomUser();
        loginSignupPage.signup(accountDetails.name(), accountDetails.email());
        signupDetailsPage = new SignupDetailsPage(driver());
        signupDetailsPage.fillAccountDetails(accountDetails);
        signupDetailsPage.submit();
        accountCreatedPage = new AccountCreatedPage(driver());
        Assertions.assertContains(accountCreatedPage.getConfirmationMessage().toUpperCase(),
                ExpectedMessages.accountCreated().toUpperCase(),
                "The seed user should have been created successfully before testing the duplicate email");
        accountPendingCleanup = true;
        homePage = accountCreatedPage.clickContinue();
        homePage.header().clickLogout();
    }

    @When("I try to sign up again with that same email")
    public void i_try_to_sign_up_again_with_that_same_email() {
        loginSignupPage = new LoginSignupPage(driver());
        loginSignupPage.open();
        loginSignupPage.signup(accountDetails.name(), accountDetails.email());
    }

    @Then("I should see the email already exists error")
    public void i_should_see_the_email_already_exists_error() {
        Assertions.assertContains(loginSignupPage.getSignupErrorMessage(), ExpectedMessages.signupEmailExists(),
                "The already-existing email error should be visible");
    }

    /**
     * Safety net (PLAN.md section 7/10: "best-effort cleanup"). If the scenario created an
     * account and didn't get to delete it (for example, because an earlier assertion failed), we
     * attempt to delete it here to avoid leaving orphaned test accounts on the site.
     */
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
