@api @products
Feature: Products API
  Contract of the public Products resource (PLAN.md section 4). The site always responds with
  HTTP 200; the real contract code travels in the "responseCode" field of the body.

  Scenario: Get the full products list
    When I request the full products list
    Then the products API response code should be 200
    And the products list should not be empty

  Scenario: POST is not supported on the products list endpoint
    When I send a POST request to the products list endpoint
    Then the products API response code should be 405
