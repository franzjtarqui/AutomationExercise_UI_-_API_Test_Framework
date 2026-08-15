@ui @smoke
Feature: Home page smoke checks
  Quick check used as a pre-flight for the UI suite: confirms that the base Page Objects
  (BasePage, HeaderComponent, FooterComponent, HomePage) work correctly against the real site.

  Scenario: The homepage loads with a working header
    When I open the homepage
    Then the page title should contain "Automation Exercise"
    And the login link should be visible in the header

  Scenario: Newsletter subscription from the footer succeeds
    Given I open the homepage
    When I subscribe to the newsletter with a random email
    Then I should see the subscription success message

  Scenario: Category navigation from the homepage works
    Given I open the homepage
    When I select the "Dress" subcategory under the "Women" category
    Then the current URL should contain "category_products"
