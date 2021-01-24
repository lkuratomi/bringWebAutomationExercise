package com.bring.selenium.Ryanair.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;
import io.cucumber.java.After;

import io.cucumber.datatable.*;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StepDefinitions {
	/**
	 * Driver of the browser
	 */
	private WebDriver driver;
	
	/**
	 * Explicit wait to use when needed
	 */
	private WebDriverWait wait;
	
	@Given("/.* has navigated to the main website/")
	public void has_navigated_to_the_main_website(	) {
	    // Go to Ryanair's main website
		driver.get("https://www.ryanair.com/gb/en");
		
		// Click on button to accept cookies to continue
		WebElement agreeCookiesButton = driver.findElement(By.className("cookie-popup-with-overlay__button"));
		agreeCookiesButton.click();
	}

	@When("/.* searches for a flight with:/")
	public void searches_for_a_flight_with(DataTable flightData) {
	    List<Map<String, String>> rows = flightData.asMaps(String.class, String.class);
	    
		// Select the airport of origin
		WebElement fromField = driver.findElement(By.id("input-button__departure"));
		fromField.click();
		fromField.clear();
		// Use the origin airport from the data table
		fromField.sendKeys(rows.get(0).get("from"));
		
		WebElement departAirpotItem = driver.findElement(By.xpath("//fsw-airport-item"));
		departAirpotItem.click();
		
		// Select the airport of destination
		WebElement toField = driver.findElement(By.id("input-button__destination"));
		// Use the destination airport from the data table
		toField.sendKeys(rows.get(0).get("to"));
		
		WebElement returnAirpotItem = driver.findElement(By.xpath("//fsw-airport-item"));
		returnAirpotItem.click();
		
		// Select the depart date
		// Get the desired depart month from the data table
		String departureMonth = getMonth(rows.get(0).get("depart"));
		WebElement departureMonthToggle = driver.findElement(By.xpath("//div[contains(text(),'" + departureMonth + "')]"));
		departureMonthToggle.click();
		
		// Use the depart date from the data table
		WebElement departureDay = driver.findElement(By.xpath("//div[@data-id='" + rows.get(0).get("depart") + "']"));
		departureDay.click();
		
		// Select the return date
		// Get the desired return month from the data table
		String returnMonth = getMonth(rows.get(0).get("return"));
		WebElement returnMonthToggle = driver.findElement(By.xpath("//div[contains(text(),'" + returnMonth + "')]"));
		returnMonthToggle.click();
		
		// Use the return date from the data table
		WebElement returnDay = driver.findElement(By.xpath("//div[@data-id='" + rows.get(0).get("return") + "']"));
		returnDay.click();
		
		// Input passengers quantities by categories from the data table
		WebElement adultsCounter = driver.findElement(By.xpath("//fsw-passengers-picker/ry-counter[1]/div[2]/div[3]"));
		int adults = Integer.parseInt(rows.get(0).get("adults"));
		for(int i = adults; i > 1; i--)
		{
			adultsCounter.click();
		}
		
		WebElement teensCounter = driver.findElement(By.xpath("//fsw-passengers-picker/ry-counter[2]/div[2]/div[3]"));
		int teens = Integer.parseInt(rows.get(0).get("teens"));
		for(int i = teens; i > 0; i--)
		{
			teensCounter.click();
		}
		
		WebElement childrenCounter = driver.findElement(By.xpath("//fsw-passengers-picker/ry-counter[3]/div[2]/div[3]"));
		int children = Integer.parseInt(rows.get(0).get("children"));
		for(int i = children; i > 0; i--)
		{
			childrenCounter.click();
		}
		
		WebElement infantsCounter = driver.findElement(By.xpath("//fsw-passengers-picker/ry-counter[4]/div[2]/div[3]"));
		int infants = Integer.parseInt(rows.get(0).get("infants"));
		for(int i = infants; i > 0; i--)
		{
			infantsCounter.click();
		}
		
		// Close the passengers input
		WebElement doneButton = driver.findElement(By.xpath("//button[contains(text(),'Done')]"));
		doneButton.click();
		
		// Accept Terms of Use
		WebElement termsButton = driver.findElement(By.xpath("//ry-checkbox"));
		termsButton.click();
		
		// Click on the search button
		WebElement searchButton = driver.findElement(By.xpath("//button[@data-ref='flight-search-widget__cta']"));
		searchButton.click();
	}
	
	@When("/.* changes the depart date for \"(.*)\" with \"(.*)\" fare/")
	public void changes_the_depart_date_for_with_fare(String new_depart, String fare) {
		// Find the depart date to change
		WebElement nextOutboundButton = driver.findElement(By.xpath("//journey-container[@data-ref='outbound']/journey/div/div[2]/div/carousel-container/carousel/div/button[@data-e2e='carousel-next']"));
		
		WebElement departureDay = null;
		while(departureDay == null)
		{
			// Tries to find the date by navigating to the following dates until the desired day loads,
			// even if the new date is in the same page than the current selection
			try {
				departureDay = driver.findElement(By.xpath("//button[@data-ref='" + new_depart + "']"));			
			}
			catch (Exception e) {
				nextOutboundButton.click();
			}			
		}
		
		// Select the flight in the new depart date
		// In here I had to add an additional explicit wait because the element was not immediately clickable after it was loaded
		wait.until(ExpectedConditions.elementToBeClickable(departureDay));
		departureDay.click();
		
		WebElement outboundFlightCard = driver.findElement(By.xpath("//flight-card[@data-e2e='flight-card--outbound']/div"));
		outboundFlightCard.click();
		
		// Select the desired fare
		if(fare.equals("Value"))
		{
			fare = "standard";
		}
		WebElement fareCard = driver.findElement(By.xpath("//div[@data-e2e='fare-card--" + fare + "']"));
		// Another explicit wait because the element was not immediately clickable after it was loaded
		wait.until(ExpectedConditions.elementToBeClickable(fareCard));
		fareCard.click();
	}
	
	@When("/.* changes the return date for \"(.*)\" with \"(.*)\" fare/")
	public void changes_the_return_date_for_with_fare(String new_return, String fare) {
		// Find the return date to change
		WebElement previousInboundButton = driver.findElement(By.xpath("//journey-container[@data-ref='inbound']/journey/div/div[2]/div/carousel-container/carousel/div/button[@data-e2e='carousel-prev']"));
		
		WebElement returnDay = null;
		while(returnDay == null)
		{
			// Tries to find the date by navigating to the previous dates until the desired day loads,
			// even if the new date is in the same page than the current selection
			try {
				returnDay = driver.findElement(By.xpath("//button[@data-ref='" + new_return + "']"));			
			}
			catch (Exception e) {
				previousInboundButton.click();
			}			
		}
		
		// Select the flight in the new return date
		// In here I had to add an additional explicit wait because the element was not immediately clickable after it was loaded
		wait.until(ExpectedConditions.elementToBeClickable(returnDay));
		returnDay.click();
		
		WebElement inboundFlightCard = driver.findElement(By.xpath("//flight-card[@data-e2e='flight-card--inbound']/div"));
		inboundFlightCard.click();
		
		// Select the desired fare
		if(fare.equals("Value"))
		{
			fare = "standard";
		}
		WebElement fareCard = driver.findElement(By.xpath("//div[@data-e2e='fare-card--" + fare + "']"));
		// Another explicit wait because the element was not immediately clickable after it was loaded
		wait.until(ExpectedConditions.elementToBeClickable(fareCard));
		fareCard.click();
	}
	
	@When("/.* fills the passengers information with:/")
	public void fills_the_passengers_information(DataTable passengersData) {
	    // Click on Log in later option
		WebElement logInLaterButton = driver.findElement(By.className("login-touchpoint__chevron-container"));
		logInLaterButton.click();
		
		List<Map<String, String>> rows = passengersData.asMaps(String.class, String.class);
		// Counter for the form slots
		int counter = 1;
		// Xpath for the form slots
		
		// Filling adults information
		String adults = rows.get(0).get("adults");
		if(adults != null)
		{
			String[] adultsList = rows.get(0).get("adults").split(",");
			for(int i = 0; i < adultsList.length; i++)
			{
				// Find and select the title for this adult
				String path = "//form/pax-passenger-container[" + counter + 
						"]/pax-passenger/div/pax-passenger-details-container/pax-passenger-details/pax-passenger-details-form-container/pax-details-form/ry-dropdown/div[2]/";
				WebElement titleDropdown = driver.findElement(By.xpath(path + "button"));
				titleDropdown.click();
				
				String title = adultsList[i].split(" ")[0];
				int titleIndex = title.equals("Ms") ? 3 : 1;
				WebElement titleItem = driver.findElement(By.xpath(path + "div/div/ry-dropdown-item[" + titleIndex + "]"));
				titleItem.click();
				
				// Fill the full name
				WebElement nameField = driver.findElement(By.id("formState.passengers.ADT-" + i + ".name"));
				WebElement surnameField = driver.findElement(By.id("formState.passengers.ADT-" + i + ".surname"));
				// Use the name from the data table
				nameField.sendKeys(adultsList[i].split(" ")[1]);
				surnameField.sendKeys(adultsList[i].split(" ")[2]);
				
				counter++;
			}		
		}
		
		// Filling teens information
		String teens = rows.get(0).get("teens");
		if(teens != null)
		{
			String[] teensList = rows.get(0).get("teens").split(",");
			for(int i = 0; i < teensList.length; i++)
			{
				// Find and select the title for this adult
				String path = "//form/pax-passenger-container[" + counter + 
						"]/pax-passenger/div/pax-passenger-details-container/pax-passenger-details/pax-passenger-details-form-container/pax-details-form/ry-dropdown/div[2]/";
				WebElement titleDropdown = driver.findElement(By.xpath(path + "button"));
				titleDropdown.click();
				
				String title = teensList[i].split(" ")[0];
				int titleIndex = title.equals("Ms") ? 2 : 1;
				WebElement titleItem = driver.findElement(By.xpath(path + "div/div/ry-dropdown-item[" + titleIndex + "]"));
				titleItem.click();
				
				// Fill the full name
				WebElement nameField = driver.findElement(By.id("formState.passengers.TEEN-" + i + ".name"));
				WebElement surnameField = driver.findElement(By.id("formState.passengers.TEEN-" + i + ".surname"));
				// Use the name from the data table
				nameField.sendKeys(teensList[i].split(" ")[1]);
				surnameField.sendKeys(teensList[i].split(" ")[2]);
				
				counter++;
			}		
		}
		
		// Filling children information
		String children = rows.get(0).get("children");
		if(children != null)
		{
			String[] childrenList = rows.get(0).get("children").split(",");
			for(int i = 0; i < childrenList.length; i++)
			{
				// Fill the full name
				WebElement nameField = driver.findElement(By.id("formState.passengers.CHD-" + i + ".name"));
				WebElement surnameField = driver.findElement(By.id("formState.passengers.CHD-" + i + ".surname"));
				// Use the name from the data table
				nameField.sendKeys(childrenList[i].split(" ")[0]);
				surnameField.sendKeys(childrenList[i].split(" ")[1]);
			}		
		}
		
		// Filling infants information
		String infants = rows.get(0).get("infants");
		if(infants != null)
		{
			String[] infantsList = rows.get(0).get("infants").split(",");
			for(int i = 0; i < infantsList.length; i++)
			{
				// Fill the full name
				WebElement nameField = driver.findElement(By.id("formState.passengers.INF-" + i + ".name"));
				WebElement surnameField = driver.findElement(By.id("formState.passengers.INF-" + i + ".surname"));
				// Use the name from the data table
				nameField.sendKeys(infantsList[i].split(" ")[0]);
				surnameField.sendKeys(infantsList[i].split(" ")[1]);
			}		
		}
		
		// Click on continue
		WebElement continueButton = driver.findElement(By.xpath("//button[contains(text(),' Continue')]"));
		continueButton.click();
	}
	
	@When("/.* selects the same seats for both flights/")
	public void selects_the_same_seats_for_both_flights() {
		// Click accepting the modal
		WebElement okayButton = driver.findElement(By.xpath("//button[contains(text(),'Okay, got it.')]"));
		okayButton.click();
		
		// Select seats for the outbound flight in the 25th row
		for(int i = 1; i < 4; i++)
		{
			WebElement seatButton = driver.findElement(By.xpath("//div[@data-ref='seat-map__seat-row-25']/div/button[" + i + "]"));
			JavascriptExecutor jse2 = (JavascriptExecutor)driver;
			jse2.executeScript("arguments[0].click();", seatButton); 
			
		}
		
		// Go to the inbound flight
		WebElement nextFlightButton = driver.findElement(By.xpath("//button[contains(text(),'Next flight')]"));
		nextFlightButton.click();
		
		// Select same seats for the inbound flight
		WebElement sameSeats = driver.findElement(By.xpath("//button[contains(text(),'Pick these seats')]"));
		wait.until(ExpectedConditions.elementToBeClickable(sameSeats));
		sameSeats.click();
	}
	
	@When("/.* selects 1 Small Bag only for all passengers/")
	public void selects_small_bag_only_for_all_passengers() {
	    // Reject Fast Track
		WebElement noFastTrackButton = driver.findElement(By.xpath("//div[@class='enhanced-takeover-beta__product-actions']/div[1]/button"));
		wait.until(ExpectedConditions.elementToBeClickable(noFastTrackButton));
		noFastTrackButton.click();
		
		// Select 1 Small Bag only option
		WebElement smallBagOption = driver.findElement(By.className("ry-radio-circle-button__label"));
		smallBagOption.click();
		
		// Click on continue
		WebElement continueButton = driver.findElement(By.xpath("//button[contains(text(),' Continue')]"));
		continueButton.click();
		
		// Continue from the "Anything else" screen
		WebElement headerTitle = driver.findElement(By.xpath("//div[contains(text(), 'Anything else for your trip?')]"));
		wait.until(ExpectedConditions.elementToBeClickable(headerTitle));
		
		WebElement continueAnythingButton = driver.findElement(By.xpath("//button[contains(text(),' Continue')]"));
		continueAnythingButton.click();
	}
	
	
	@Then("/.* should be able to see the overview screen/")
	public void should_be_able_to_see_the_overview_screen() {
	    // Header available
		WebElement headerTitle = driver.findElement(By.xpath("//h3[contains(text(), 'Plan your whole trip')]"));
		wait.until(ExpectedConditions.elementToBeClickable(headerTitle));
		
		// Flights added
		WebElement flightsAdded = driver.findElement(By.xpath("//div[contains(text(), 'Added to basket')]"));
		wait.until(ExpectedConditions.elementToBeClickable(flightsAdded));
	}
	
	@Before
	public void beforeMethod()
	{
		// Setting path to Gecko driver
		System.setProperty("webdriver.gecko.driver", "utils/geckodriver.exe");
		
		// New instance of the Firefox driver and it's implicit wait setting
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		
		wait = new WebDriverWait(driver, 15);
	}
	
	@After
	public void afterClass()
	{
		driver.quit();		
	}
	
	/**
	 * Helper method to obtain the name of a month from a UTC date
	 * @param unformattedMonth is a date in YYYY-MM-DD format
	 * @return month a month in the format MMM
	 */
	public String getMonth(String unformattedMonth)
	{
		String month = "wrong";
		try {
			Date departureMonthDate = new SimpleDateFormat("yyyy-MM-dd").parse(unformattedMonth);
			month = new SimpleDateFormat("MMM").format(departureMonthDate);
		} 
		catch (Exception e) {
			System.out.println("Date format in Feature file is incorrect and coudln't be parsed.");
		}
		return month;
	}
}
