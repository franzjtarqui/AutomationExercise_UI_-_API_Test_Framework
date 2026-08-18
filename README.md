# AutomationExercise UI + API Test Framework

E2E (UI) + API test automation framework for [automationexercise.com](https://automationexercise.com), built as a professional portfolio piece for QA Automation Engineer / SDET roles.

**Java 17 · Selenium 4 · Cucumber 7 + TestNG 7 · REST Assured 6 · Allure 2 · GitHub Actions**

## Highlights

- 18 UI Gherkin scenarios (registration, login/logout, catalog, cart, checkout)
- 11 API Gherkin scenarios (products, brands, search, login, account lifecycle)
- ~37 unit/integration tests (utils, data layer, API models and clients)
- Full Page Object Model (pages + reusable components)
- Parallel execution per scenario (Cucumber + TestNG `@DataProvider`)
- Automatic retry of failed tests (mitigates flakiness of the site under load)
- Allure report with history, published to GitHub Pages via CI
- GitHub Actions pipeline: build → test-ui (headless) / test-api → Allure report

## Stack

| Category    | Technology                                                |
|-------------|-------------------------------------------------------------|
| Language    | Java 17                                                      |
| UI          | Selenium WebDriver 4.x (Selenium Manager, no WebDriverManager) |
| BDD         | Cucumber 7.x (`cucumber-java`, `cucumber-testng`)            |
| Runner      | TestNG 7.x (parallelism via `@DataProvider`)                 |
| API         | REST Assured 6.x + Jackson (Databind)                        |
| Reporting   | Allure 2.x (`allure-cucumber7-jvm`, `allure-testng`, `allure-rest-assured`) |
| Test data   | Datafaker (unique users) + JSON fixtures (stable data)       |
| Logging     | SLF4J + Logback                                              |
| Boilerplate | Lombok                                                       |
| CI/CD       | GitHub Actions + GitHub Pages                                |

## Prerequisites

- Java 17 (JDK)
- Maven 3.9+
- Google Chrome installed (default browser; Selenium Manager resolves the driver automatically). Firefox and Edge are also supported via `-Dbrowser=firefox|edge`.

## How to run

```bash
# Full suite (UI + API + unit tests)
mvn test -Dsuite=all

# UI only
mvn test -Dsuite=ui

# API only (no browser required)
mvn test -Dsuite=api

# UI in headless mode (what CI uses)
mvn test -Dsuite=ui -Dheadless=true

# Different browser
mvn test -Dsuite=ui -Dbrowser=firefox

# Local Allure report (starts a server and opens the browser)
mvn allure:serve

# Static Allure report at target/site/allure-maven-plugin
mvn allure:report
```

If `-Dsuite` is omitted, `pom.xml` defaults to `testng.xml` (the `all` suite).

### Configuration

`src/test/resources/config/config.properties` centralizes the configuration (UI/API baseUrl, browser, headless, timeouts). `ConfigManager` resolves each value with this precedence (highest to lowest priority):

1. System property (`-Dkey=value`)
2. Environment variable (`BROWSER`, `HEADLESS`, `BASE_URL_UI`, `BASE_URL_API`, `TIMEOUT_EXPLICIT_SECONDS`, `TIMEOUT_POLLING_MILLIS`, `ENV`)
3. `config.properties`
4. Default value in code

This allows tuning execution in CI or locally without touching the versioned file. No secrets need to be configured: all test users/accounts are generated dynamically with Faker and removed at the end of each scenario.

## Project structure

```text
AutomationExercise-UI-API-Test-Framework/
├─ pom.xml
├─ testng.xml / testng-ui.xml / testng-api.xml   # suites and parallelism
├─ .github/workflows/ci.yml                       # CI/CD pipeline
├─ src/
│  ├─ main/java/com/portfolio/ae/
│  │  ├─ config/          # ConfigManager, Environment, BrowserType
│  │  ├─ driver/          # DriverFactory, DriverManager (ThreadLocal)
│  │  ├─ ui/pages/        # Page Objects (BasePage, HomePage, Login/Signup, Cart, Checkout...)
│  │  ├─ ui/components/   # Header, Footer, ProductCard, CartModal
│  │  ├─ api/clients/     # Product/Brand/Search/Auth/AccountApiClient (REST Assured)
│  │  ├─ api/models/      # Request/response POJOs (Jackson)
│  │  ├─ api/specs/       # RequestSpecFactory / ResponseSpecFactory
│  │  ├─ data/            # UserBuilder/UserDataFactory (Faker), ExpectedMessages, ProductFixtures
│  │  └─ utils/           # WaitUtils, JsonReader, FakerUtil, Assertions
│  └─ test/
│     ├─ java/com/portfolio/ae/
│     │  ├─ runners/      # UIRunner, ApiRunner (TestNG + Cucumber, parallel per scenario)
│     │  ├─ stepdefs/ui/  # UI step definitions
│     │  ├─ stepdefs/api/ # API step definitions
│     │  ├─ hooks/        # WebDriver setup/teardown, screenshot on failure
│     │  └─ listeners/    # RetryAnalyzer/RetryTransformer (automatic retry)
│     └─ resources/
│        ├─ features/ui/  # registration, login, products_search, cart, checkout, home_page_smoke
│        ├─ features/api/ # products_api, brands_api, search_api, auth_api, account_lifecycle_api
│        ├─ testdata/      # JSON fixtures (expected messages, known products)
│        ├─ config/        # config.properties, allure.properties
│        └─ logback.xml
└─ target/                 # allure-results, reports (generated, not versioned)
```

## What it covers

### UI (Page Object Model + Cucumber)

- `home_page_smoke.feature` — home page load, newsletter subscription, category navigation
- `registration.feature` — user sign-up, duplicate email, account deletion
- `login.feature` — valid/invalid login, logout
- `products_search.feature` — listing, search, product detail
- `cart.feature` — add, update quantity, remove, subtotals
- `checkout.feature` — shipping address, order with dummy card, total recalculation

### API (REST Assured)

The 5 public resources from `/api_list`, with assertions on the body (`responseCode`/`message`), since the site always responds with HTTP 200:

- `products_api.feature` — `GET /productsList` (200), unsupported `POST` (405)
- `brands_api.feature` — `GET /brandsList` (200), unsupported `PUT` (405)
- `search_api.feature` — `POST /searchProduct` with and without parameter (200/400)
- `auth_api.feature` — `POST /verifyLogin` valid/invalid/missing email (200/404/400), unsupported `DELETE` (405)
- `account_lifecycle_api.feature` — full chain `createAccount → verifyLogin → updateAccount → getUserDetailByEmail → deleteAccount`

## CI/CD

`.github/workflows/ci.yml` (triggers: `push`/`pull_request` to `main`, nightly `schedule`, `workflow_dispatch`):

```
build (compile) ──▶ test-ui (headless) ─┐
                 └─▶ test-api            ├─▶ allure-report ─▶ GitHub Pages
```

- `test-ui` runs on `ubuntu-latest` (Chrome preinstalled + Selenium Manager), uploads `allure-results` as an artifact.
- `test-api` runs `mvn test -Dsuite=api`, same artifact pattern.
- `allure-report` combines both results, recovers the history of previous builds from the `gh-pages` branch, generates the report and publishes it (with trends across runs) in addition to uploading it as a downloadable artifact.

## Decisions and limitations

- **API always returns HTTP 200**: the real contract code travels in the body's `responseCode`/`message`; assertions are made against the body, not the HTTP status (a limitation of the site, not the framework).
- **No real coupon at checkout**: the flow is covered via total recalculation by quantity instead.
- **Dynamic data with Faker + teardown**: each scenario creates its own data and cleans it up at the end (best-effort); there are no persistent account fixtures or secrets to manage.
- **Explicit waits, no implicit waits**: `WaitUtils`/`BasePage` centralize `WebDriverWait`/`FluentWait`; click retry on `StaleElementReferenceException`/`ElementClickInterceptedException`.
- **Practice site behind Cloudflare + third-party ads**: Cloudflare challenges headless Selenium from data-center IPs (e.g. GitHub Actions) much more aggressively than from residential IPs — this made the scenarios that register a user and navigate right after fail consistently in CI while they passed locally. Mitigations: `WaitUtils` detects the "Just a moment..." interstitial and waits for it to resolve before failing (also after every navigation in `BasePage`), parallelism was reduced to 2 scenarios (`data-provider-thread-count="2"`), Selenium is kept close to the latest to match Chrome's CDP version, and a `RetryAnalyzer` retries any failing test once. Residual flakiness is monitored via the nightly CI run.
