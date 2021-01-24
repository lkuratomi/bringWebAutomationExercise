package com.bring.selenium.Ryanair.tests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="Features", glue= {"com.bring.selenium.Ryanair.steps"}, plugin = {"pretty"})
public class RunCucumberTest {

}
