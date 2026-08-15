@ui @smoke @auth
Feature: User registration
  New user sign-up via UI (PLAN.md section 3). Each scenario is independent and self-cleaning
  (PLAN.md section 7): they don't depend on each other and don't leave orphaned test accounts,
  so they can run safely in parallel.

  Scenario: Register a new user with valid data and then delete the account
    Given I am on the signup and login page
    When I sign up with a new unique account
    Then I should see the account created confirmation
    When I delete the account
    Then I should see the account deleted confirmation

  Scenario: Attempt to register with an already existing email shows an error
    Given a user is already registered with a known email
    When I try to sign up again with that same email
    Then I should see the email already exists error
