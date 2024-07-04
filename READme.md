This repository has the Assignment 2 for the Verification and Validation of Software (VVS) course, academic year 2023/2024. The project received a perfect score of 20/20 and focuses on comprehensive testing techniques for a web application following a layered architecture.

## Goals
The main goals of this assignment are:

1. **Integration Testing:** Validate web application functionalities through simulated user interactions.
2. **Database Testing:** Ensure data integrity and validate operations using test data.
3. **Mock Testing:** Isolate and test business logic components by mocking dependencies.

## Key Features

1. **Integration Testing with HtmlUnit:**
    - Tests for inserting new addresses and customers.
    - Validation of sales creation and status updates.
    - Complete workflow tests from customer creation to delivery handling.
    - Robustness tests including handling of invalid inputs and concurrency.

2. **Database Testing with DbSetup:**
    - Setup and teardown of database states for reliable testing.
    - Validation of constraints such as unique VAT numbers for customers.
    - Tests for customer deletion, updates, and ensuring data integrity.

3. **Mock Testing with Mockito:**
    - Mocking dependencies in the business layer to test individual components.
    - Refactoring to introduce interfaces and dependency injection for better testability.
    - Examples of isolated unit tests demonstrating effective use of mocks.

## Tools and Technologies
- **HtmlUnit:** For simulating browser interactions and verifying web application behavior.
- **DbSetup:** For setting up and tearing down database states for tests.
- **Mockito:** For creating mocks and stubs to isolate and test components.
- **Wildfly:** Application server used to deploy the web application.
- **Maven:** Build automation and project management tool.

## Getting Started
To get a local copy up and running, follow these simple steps:

1. **Clone the repository:**
   ```sh
   git clone https://github.com/9-whiterose-9/Verification-And-Validation-for-Web-Application.git

### Import the project into Eclipse:

1. Open Eclipse IDE.
2. Go to `File > Import...`.
3. Select `Maven > Existing Maven Projects` and click `Next`.
4. Browse to the cloned repository directory and click `Finish`.

### Build the project:

1. Right-click on the project in the Project Explorer.
2. Select `Run As > Maven clean`.
3. After the clean process completes, select `Run As > Maven install`.

### Configure Wildfly in Eclipse:

1. Go to `Window > Show View > Servers`.
2. In the `Servers` view, right-click and select `New > Server`.
3. Select `Wildfly` from the list and click `Next`.
4. Configure the Wildfly server runtime environment and click `Finish`.

### Deploy the application to Wildfly:

1. Right-click on the project in the Project Explorer.
2. Select `Run As > Run on Server`.
3. Choose the Wildfly server configured earlier and click `Finish`.

### Run the tests:

1. Right-click on the project in the Project Explorer.
2. Select `Run As > JUnit Test`.
