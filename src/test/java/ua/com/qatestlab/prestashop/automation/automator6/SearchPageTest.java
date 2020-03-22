package ua.com.qatestlab.prestashop.automation.automator6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SearchPageTest {

private static final Logger LOGGER = LogManager.getLogger(MainPageTest.class);
	
	private static WebDriver driver;
	
	private SearchPage searchPage;
	
	@BeforeClass
	public static void init() {
		LOGGER.info("=======================================================");
		
		//Selenium web driver initialization
		LOGGER.info("Selenium web driver initialization");
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		driver = new ChromeDriver();
	}
	
	@Before
	public void openSearchPage() throws InterruptedException {
		driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
		searchPage = new MainPage(driver).setUSDCurrency().searchFor("dress");
	}
	
	@Test
	public void titleTest() {
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Search test");
		LOGGER.info("-------------------------------------------------------");
		
		String expectedTitle = "Поиск";
		String actualTitle = searchPage.getTitle();
		
		LOGGER.info("Actual title: " + actualTitle);
		LOGGER.info("Expected title: " + expectedTitle);
		LOGGER.info("Is match: " + expectedTitle.equals(actualTitle));
		assertEquals(expectedTitle, actualTitle);
	}
	
	@Test
	public void searchCountTest() {
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Search count test");
		LOGGER.info("-------------------------------------------------------");
		
		int expectedCount = 7;
		int actualCount = searchPage.getFindCount();
		LOGGER.info("Actual count: " + actualCount);
		LOGGER.info("Expected count: " + expectedCount);
		LOGGER.info("Is equals: " + (expectedCount == actualCount));
		assertTrue(expectedCount == actualCount);
	}
	
	@Test
	public void advancedSearchCountTest() {
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Advanced search count test");
		LOGGER.info("-------------------------------------------------------");
		
		int pagesCount = searchPage.getPagesCount();
		int perPageCount = searchPage.getFindCountPerPage();
		int currentPageCount = searchPage.getPrices().size();
		int realCount = (pagesCount - 1) * perPageCount + currentPageCount;
		int actualCount = searchPage.getFindCount();
		
		LOGGER.info("Actual count: " + actualCount);
		LOGGER.info("Real count: " + realCount);
		LOGGER.info("Is equals: " + (realCount == actualCount));
		assertTrue(realCount == actualCount);
	}
	
	@Test
	public void searchPageCurrencyTest() {
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Search page currency test");
		LOGGER.info("-------------------------------------------------------");
		
		String expectedCurrency = searchPage.getCurrency();
		String expectedCurrencySymbol = expectedCurrency.split(" ")[1];
		List<String> prices = searchPage.getPrices();
		checkPricesCurrency(prices, expectedCurrencySymbol);
	}
	
	@Test
	public void sortTest() {
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Sort test");
		LOGGER.info("-------------------------------------------------------");
		
		List<Product> products = searchPage.sortPriceFromHighToLow().getProducts();
		for (int i = 0; i < products.size() - 1; i++) {
			Product product = products.get(i);
			Product nextProduct = products.get(i + 1);
			
			float price = product.hasDiscount() ? product.getOldPrice() : product.getPrice();
			float nextPrice = nextProduct.hasDiscount() ? nextProduct.getOldPrice() : nextProduct.getPrice();
			String currency = product.getCurrency();
			String nextCurrency = nextProduct.getCurrency();
			
			LOGGER.info(String.format("Price: %.2f %s, next price: %.2f %s, current price is bigger (or equals): %b",
					price, currency, nextPrice, nextCurrency, price >= nextPrice));
			assertTrue(price >= nextPrice);
			assertEquals(currency, nextCurrency);
		}
	}
	
	@Test
	public void discountTest() {
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Discount test");
		LOGGER.info("-------------------------------------------------------");
		
		List<Product> products = searchPage.getProducts();
		for (Product product: products) {
			if (product.hasDiscount()) {
				float calculatedPrice = product.getOldPrice() - product.getOldPrice() * product.getDiscount() / 100;
				float accuracy = 0.01f;
				boolean isEquals = Math.abs(product.getPrice() - calculatedPrice) <= accuracy;
				LOGGER.info("Regular price: " + product.getOldPrice());
				LOGGER.info("Discount: " + product.getDiscount() + " %");
				LOGGER.info("Actual price: " + product.getPrice());
				LOGGER.info("Calculated price: " + calculatedPrice);
				LOGGER.info(String.format("Is equals (accuracy = %.2f): %b", accuracy, isEquals));
				assertTrue(isEquals);
				LOGGER.info("-------------------------------------------------------");
			}
		}
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
