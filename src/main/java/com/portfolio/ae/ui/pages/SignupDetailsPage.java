package com.portfolio.ae.ui.pages;

import com.portfolio.ae.data.AccountDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * "Enter Account Information" page (second signup step, after {@link LoginSignupPage#signup}).
 * The email field is shown but disabled by the site (it's already pre-filled);
 * it's not touched here.
 */
public class SignupDetailsPage extends BasePage {

    private static final By TITLE_MR_RADIO = By.id("id_gender1");
    private static final By TITLE_MRS_RADIO = By.id("id_gender2");
    private static final By PASSWORD_INPUT = By.cssSelector("input[data-qa='password']");
    private static final By DAYS_SELECT = By.cssSelector("select[data-qa='days']");
    private static final By MONTHS_SELECT = By.cssSelector("select[data-qa='months']");
    private static final By YEARS_SELECT = By.cssSelector("select[data-qa='years']");
    private static final By FIRST_NAME_INPUT = By.cssSelector("input[data-qa='first_name']");
    private static final By LAST_NAME_INPUT = By.cssSelector("input[data-qa='last_name']");
    private static final By COMPANY_INPUT = By.cssSelector("input[data-qa='company']");
    private static final By ADDRESS1_INPUT = By.cssSelector("input[data-qa='address']");
    private static final By ADDRESS2_INPUT = By.cssSelector("input[data-qa='address2']");
    private static final By COUNTRY_SELECT = By.cssSelector("select[data-qa='country']");
    private static final By STATE_INPUT = By.cssSelector("input[data-qa='state']");
    private static final By CITY_INPUT = By.cssSelector("input[data-qa='city']");
    private static final By ZIPCODE_INPUT = By.cssSelector("input[data-qa='zipcode']");
    private static final By MOBILE_NUMBER_INPUT = By.cssSelector("input[data-qa='mobile_number']");
    private static final By CREATE_ACCOUNT_BUTTON = By.cssSelector("button[data-qa='create-account']");

    public SignupDetailsPage(WebDriver driver) {
        super(driver);
    }

    public void fillAccountDetails(AccountDetails details) {
        click("Mrs".equalsIgnoreCase(details.title()) ? TITLE_MRS_RADIO : TITLE_MR_RADIO);
        type(PASSWORD_INPUT, details.password());
        selectByVisibleText(DAYS_SELECT, details.birthDay());
        selectByVisibleText(MONTHS_SELECT, details.birthMonth());
        selectByVisibleText(YEARS_SELECT, details.birthYear());
        type(FIRST_NAME_INPUT, details.firstName());
        type(LAST_NAME_INPUT, details.lastName());
        if (details.company() != null) {
            type(COMPANY_INPUT, details.company());
        }
        type(ADDRESS1_INPUT, details.address1());
        if (details.address2() != null) {
            type(ADDRESS2_INPUT, details.address2());
        }
        selectByVisibleText(COUNTRY_SELECT, details.country());
        type(STATE_INPUT, details.state());
        type(CITY_INPUT, details.city());
        type(ZIPCODE_INPUT, details.zipcode());
        type(MOBILE_NUMBER_INPUT, details.mobileNumber());
    }

    public void submit() {
        click(CREATE_ACCOUNT_BUTTON);
    }

    private void selectByVisibleText(By locator, String visibleText) {
        WebElement selectElement = waitUtils.waitVisible(locator);
        new Select(selectElement).selectByVisibleText(visibleText);
    }
}
