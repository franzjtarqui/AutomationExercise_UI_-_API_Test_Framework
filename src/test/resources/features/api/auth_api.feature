@api @auth
Feature: Verify Login API
  Contract of the public verifyLogin resource (PLAN.md section 4). The site always responds
  with HTTP 200; the real contract code travels in the "responseCode"/"message" fields of the
  body. The scenario that needs valid credentials creates its own "seed" user via the API
  (there is no real persistent account to use as a fixture) and removes it when done.

  Scenario: Verify login with valid credentials
    Given a registered user created via the API
    When I verify login with that user's credentials
    Then the verify login response code should be 200
    And the verify login message should be "User exists!"

  Scenario: Verify login with invalid credentials
    When I verify login with an email that does not exist
    Then the verify login response code should be 404

  Scenario: Verify login without the email parameter
    When I send a verify login request without the "email" parameter
    Then the verify login response code should be 400

  Scenario: DELETE is not supported on the verify login endpoint
    When I send a DELETE request to the verify login endpoint
    Then the verify login response code should be 405
