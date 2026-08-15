@api @account
Feature: Account Lifecycle API
  Full account lifecycle chained together with generated data (PLAN.md section 4):
  create -> verify -> update -> getDetail -> delete.

  Scenario: Create, verify, update, fetch and delete an account
    When I create a new account via the API
    Then the account creation response code should be 201
    When I verify login for the account I just created
    Then the account login verification response code should be 200
    When I fetch the user details by email
    Then the user details should match the account I created
    When I update the account with new details
    Then the account update response code should be 200
    When I fetch the user details by email
    Then the user details should match the updated account
    When I delete the account
    Then the account deletion response code should be 200
    When I fetch the user details by email
    Then the user details response code should be 404
