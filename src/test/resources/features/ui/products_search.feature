@ui @catalog
Feature: Product search and details
  Product listing, search and detail view (PLAN.md section 3).

  Scenario: View the list of all products
    Given I open the homepage
    When I go to the products page
    Then I should see the full product listing

  Scenario: Search a product by name and get coherent results
    Given I am on the products page
    When I search for the product "Top"
    Then all the visible products should contain "Top" in their name

  Scenario: View a product's detail
    Given I am on the products page
    When I view the details of product "1"
    Then the product name should be "Blue Top"
    And the product category should contain "Tops"
    And the product should be "In Stock"
