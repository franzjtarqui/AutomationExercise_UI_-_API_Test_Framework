@ui @auth
Feature: Login and logout
  Login/logout with an already registered account (PLAN.md section 3). The "seed" user is
  created via UI at the start of each scenario that needs it and removed in a best-effort
  cleanup hook (PLAN.md section 7).

  Scenario: Login with valid credentials shows the logged-in user
    Given a registered user with known credentials
    When I log in with valid credentials
    Then I should be logged in as that user

  Scenario: Login with invalid credentials shows an error
    When I try to log in with invalid credentials
    Then I should see the login error message

  Scenario: Logout from a logged-in session
    Given a registered user with known credentials
    And I log in with valid credentials
    When I log out
    Then I should not be logged in anymore
