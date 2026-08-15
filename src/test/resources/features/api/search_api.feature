@api @search
Feature: Search Product API
  Contract of the public Search resource (PLAN.md section 4). The site always responds with
  HTTP 200; the real contract code travels in the "responseCode" field of the body.

  Scenario: Search for an existing product term
    When I search products for "top"
    Then the search API response code should be 200
    And the search results should not be empty

  Scenario: Searching without the required parameter fails
    When I send a search request without the "search_product" parameter
    Then the search API response code should be 400
