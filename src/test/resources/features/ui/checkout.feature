@ui @checkout
Feature: Checkout
  Full checkout with a logged-in user: address, dummy card payment and total recalculation by
  quantity (PLAN.md section 3). The site has no real coupon field (PLAN.md section 11); that
  scope is covered here via the total recalculation instead.

  Scenario: Proceed to checkout as a logged-in user and see the correct address
    Given I am logged in with a registered account
    When I add product "1" to the cart
    And I go to the cart and proceed to checkout
    Then the checkout page should show my address details

  Scenario: Place an order with a dummy card and see the order confirmation
    Given I am logged in with a registered account
    When I add product "1" to the cart
    And I go to the cart and proceed to checkout
    And I place the order and pay with a dummy card
    Then I should see the order placed confirmation

  Scenario: Recalculate totals when the quantity changes
    Given I am logged in with a registered account
    When I add product "1" to the cart with quantity "3"
    And I go to the cart and proceed to checkout
    Then the checkout total amount should match product "1" with quantity "3"
