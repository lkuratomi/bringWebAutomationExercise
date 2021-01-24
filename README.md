# Readmne
This project was created to solve the QA WEb Exercise for Bring Global.

It was created in Windows 10 using:
- Eclipse IDE for Java Developers Version: 2020-12 (4.18.0)
- Maven 3.6.3
- JUnit 4.13.1
- Cucumber 6.9.1

## How to run it
The Gecko driver was included in the utils inside the project, so the test scenario should run by importing the project in Eclipse, right-clicking on the RunCucumberTest.java (src/test/java > com.bring.selenium.Ryanair.tests) and selecting "Run as" > "JUnit Test":

## Next steps
If more time was invested in this project I would like to work on:
- Add more scenarios to improve test coverage
- Applying Page Object Model to the site to have more flexibility when the site experiences changes and add additional validations for the UI elements. The current test is mainly focusing on the functional step by step
- Improve navigation to reduce execution time, especially with the carousel navigation (maybe try something with waits instead?)
- Add more browsers and screen resolutions
- Add reporting
