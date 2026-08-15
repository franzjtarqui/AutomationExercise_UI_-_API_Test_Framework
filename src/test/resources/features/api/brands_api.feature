@api @brands
Feature: Brands API
  Contract of the public Brands resource (PLAN.md section 4). The site always responds with
  HTTP 200; the real contract code travels in the "responseCode" field of the body.

  Scenario: Get the full brands list
    When I request the full brands list
    Then the brands API response code should be 200
    And the brands list should not be empty

  Scenario: PUT is not supported on the brands list endpoint
    When I send a PUT request to the brands list endpoint
    Then the brands API response code should be 405
