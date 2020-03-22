package ua.com.qatestlab.prestashop.automation.automator6;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

public class MainPageTest {

	private static final Logger LOGGER = LogManager.getLogger(MainPageTest.class);
	
	private static WebDriver driver;
	
	private MainPage mainPage;
	
	@BeforeClass
	public static void init() {
		LOGGER.info("=======================================================");
		
		//Selenium web driver initialization
		LOGGER.info("Selenium web driver initialization");
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		driver = new ChromeDriver();
	}
	
	@Before
	public void openMainPage() throws InterruptedException {
		driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
		mainPage = new MainPage(driver);
	}
	
	@Test
	public void titleTest() {
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Title test");
		LOGGER.info("-------------------------------------------------------");
		
		String expectedTitle = "prestashop-automation";
		String actualTitle = mainPage.getTitle();
		LOGGER.info("Actual title: " + actualTitle);
		LOGGER.info("Expected title: " + expectedTitle);
		LOGGER.info("Is match: " + expectedTitle.equals(actualTitle));
		assertEquals(expectedTitle, actualTitle);
	}
	
	@Test
	public void pricesCurrencyTest() {
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Prices currency test");
		LOGGER.info("-------------------------------------------------------");
		
		String expectedCurrency = mainPage.getCurrency();
		String expectedCurrencySymbol = expectedCurrency.split(" ")[1];
		List<String> prices = mainPage.getPrices();
		checkPricesCurrency(prices, expectedCurrencySymbol);
	}
	
	@Test
	public void pricesTestAfterChangeCurrency() {
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Prices test after change currency");
		LOGGER.info("-------------------------------------------------------");
		
		String expectedCurrency = mainPage.setUSDCurrency().getCurrency();
		String expectedCurrencySymbol = expectedCurrency.split(" ")[1];
		List<String> prices = mainPage.getPrices();
		checkPricesCurrency(prices, expectedCurrencySymbol);
	}
	
	@AfterClass
	public static void close() {
		//Close web driver
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Close web driver");
		if (driver != null) {
			driver.close();
			driver.quit();
		}
	}
	
	private static void checkPricesCurrency(List<String> prices, String expectedCurrencySymbol) {
		for (String s: prices) {
			String actualCurrencySymbol = s.split(" ")[1];
			boolean isMatch = expectedCurrencySymbol.equals(actualCurrencySymbol);
			LOGGER.info(String.format("Price: %s, expected currency: %s, currency is match: %b", s,
					expectedCurrencySymbol, isMatch));
			assertTrue(isMatch);
		};
	}
}
