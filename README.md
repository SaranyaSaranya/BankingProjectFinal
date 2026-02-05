# Banking Project Automation – Selenium & Cucumber

## Project Overview
This is a real-time Selenium automation framework developed for a sample banking application.
The project validates both **individual page-level functionality** and **end-to-end user journeys**
using **TestNG** and **Cucumber (BDD)** frameworks.

## Tech Stack
- Java
- Selenium WebDriver
- TestNG
- Cucumber (BDD)
- Maven
- Page Object Model (POM)
- Log4j (Logging)
- Git & GitHub

## Framework Architecture
- Page Object Model (POM) for all application pages
- Cucumber feature files with step definitions
- TestNG listeners for reporting and execution tracking
- Runner classes for execution control
- Utility layer for reusable common functions
- Configuration-driven execution using properties files

## Utilities Implemented
- Explicit Wait Utility for stable execution
- Screenshot Utility (captured on failures)
- Configuration Reader (properties file)
- Dynamic data handling from config files
- Reusable browser and driver utilities

## Test Coverage
- Customer page functionality tested independently
- Bank Manager page functionality tested independently
- Validation using both static and dynamic input data

## End-to-End (E2E) Flow Validation
- Complete customer journey automated
- Full banking flow executed using explicit waits
- Screenshots captured for every failure scenario
- Logging enabled for all major test steps

### Cucumber Automation
- Feature files written in Gherkin language
- Step definitions mapped using POM methods
- Full flow scenarios validated using BDD approach
- Cucumber runners used for controlled execution

## Reporting & Logging
- TestNG default reports
- Listener-based execution tracking
- Log4j integrated logging
- Screenshot evidence on failure

## Execution Options
- Run via TestNG XML
- Run via Cucumber Runner
- Run using Maven commands
