package com.portfolio.ae.stepdefs.ui;

import com.portfolio.ae.driver.DriverManager;
import com.portfolio.ae.ui.pages.HomePage;
import com.portfolio.ae.utils.Assertions;
import com.portfolio.ae.utils.FakerUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step definitions for the UI smoke test (pre-flight of the UI suite, "base Page Objects and
 * navigation" component). Exercises {@link HomePage} and its components (Header/Footer) against
 * the real site.
 */
public class HomePageSmokeSteps {

    private HomePage homePage;

    @Given("I open the homepage")
    public void i_open_the_homepage() {
        homePage = new HomePage(DriverManager.getDriver());
        homePage.open();
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expectedFragment) {
        Assertions.assertContains(homePage.getTitle(), expectedFragment,
                "The page title should contain '" + expectedFragment + "'");
    }

    @Then("the login link should be visible in the header")
    public void the_login_link_should_be_visible_in_the_header() {
        Assertions.assertTrue(homePage.header().isSignupLoginVisible(),
                "The 'Signup / Login' link should be visible in the header");
    }

    @When("I subscribe to the newsletter with a random email")
    public void i_subscribe_to_the_newsletter_with_a_random_email() {
        homePage.footer().subscribe(FakerUtil.uniqueEmail());
    }

    @Then("I should see the subscription success message")
    public void i_should_see_the_subscription_success_message() {
        Assertions.assertContains(homePage.footer().getSubscriptionSuccessMessage(), "successfully subscribed",
                "The subscription success message should be visible");
    }

    @When("I select the {string} subcategory under the {string} category")
    public void i_select_the_subcategory_under_the_category(String subCategoryName, String categoryName) {
        homePage.selectSubCategory(categoryName, subCategoryName);
    }

    @Then("the current URL should contain {string}")
    public void the_current_url_should_contain(String expectedFragment) {
        Assertions.assertContains(homePage.getCurrentUrl(), expectedFragment,
                "The current URL should contain '" + expectedFragment + "'");
    }
}
