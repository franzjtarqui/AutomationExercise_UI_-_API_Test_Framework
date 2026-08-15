@ui @cart
Feature: Shopping cart
  Adding, updating quantity and removing products from the cart (PLAN.md section 3).

  Scenario: Add a product to the cart and verify quantity and price
    Given I am on the products page
    When I add product "1" to the cart
    And I view the cart from the confirmation modal
    Then the cart should contain product "1" with quantity "1"

  Scenario: Add multiple products and validate each subtotal
    Given I am on the products page
    When I add product "1" to the cart
    And I continue shopping
    And I add product "2" to the cart
    And I view the cart from the confirmation modal
    Then the cart should contain product "1" with quantity "1"
    And the cart should contain product "2" with quantity "1"

  Scenario: Update quantity from the product detail page
    Given I am on the products page
    When I view the details of product "1"
    And I set the quantity to "3"
    And I add the product to the cart from the detail page
    And I view the cart from the confirmation modal
    Then the cart should contain product "1" with quantity "3"

  Scenario: Remove a product from the cart
    Given I am on the products page
    When I add product "1" to the cart
    And I view the cart from the confirmation modal
    And I remove product "1" from the cart
    Then the cart should not contain product "1"
