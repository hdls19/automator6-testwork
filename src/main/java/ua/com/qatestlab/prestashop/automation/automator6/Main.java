package ua.com.qatestlab.prestashop.automation.automator6;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.WriterAppender;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);
	
	public static void main(String[] args) throws IOException {
		initLogger();
		LOGGER.info("=======================================================");
		
		//Selenium web driver initialization
		LOGGER.info("Selenium web driver initialization");
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		//Go to main URL
		driver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
		MainPage mainPage = new MainPage(driver);
		
		//Title test
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Title test");
		LOGGER.info("-------------------------------------------------------");
		
		String expectedTitle = "prestashop-automation";
		String actualTitle = mainPage.getTitle();
		LOGGER.info("Actual title: " + actualTitle);
		LOGGER.info("Expected title: " + expectedTitle);
		LOGGER.info("Is match: " + expectedTitle.equals(actualTitle));
		
		//Currency test
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Currency test");
		LOGGER.info("-------------------------------------------------------");
		
		String expectedCurrency = "UAH ₴";
		String actualCurrency = mainPage.getCurrency();
		LOGGER.info("Actual currency: " + actualCurrency);
		LOGGER.info("Expected currency: " + expectedCurrency);
		LOGGER.info("Is match: " + expectedCurrency.equals(actualCurrency));
		
		//Prices test
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Prices test");
		LOGGER.info("-------------------------------------------------------");
		
		String expectedCurrencySymbol = expectedCurrency.split(" ")[1];
		List<String> prices = mainPage.getPrices();
		checkPricesCurrency(prices, expectedCurrencySymbol);
		
		//Change currency test
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Change currency test");
		LOGGER.info("-------------------------------------------------------");
		
		expectedCurrency = "USD $";
		actualCurrency = mainPage.setUSDCurrency().getCurrency();
		LOGGER.info("Actual currency: " + actualCurrency);
		LOGGER.info("Expected currency: " + expectedCurrency);
		LOGGER.info("Is match: " + expectedCurrency.equals(actualCurrency));
		
		//Prices test after change currency
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Prices test after change currency");
		LOGGER.info("-------------------------------------------------------");
		
		expectedCurrencySymbol = expectedCurrency.split(" ")[1];
		prices = mainPage.getPrices();
		checkPricesCurrency(prices, expectedCurrencySymbol);
		
		//Search test
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Search test");
		LOGGER.info("-------------------------------------------------------");
		
		String searchQuery = "dress";
		SearchPage searchPage = mainPage.searchFor(searchQuery);
		
		expectedTitle = "Поиск";
		actualTitle = searchPage.getTitle();
		
		LOGGER.info("Actual title: " + actualTitle);
		LOGGER.info("Expected title: " + expectedTitle);
		LOGGER.info("Is match: " + expectedTitle.equals(actualTitle));
		
		//Close web driver
		LOGGER.info("-------------------------------------------------------");
		LOGGER.info("Close web driver");
		driver.close();
		driver.quit();
	}
	
	private static void checkPricesCurrency(List<String> prices, String expectedCurrencySymbol) {
		for (String s: prices) {
			try {
				String actualCurrencySymbol = s.split(" ")[1];
				boolean isMatch = expectedCurrencySymbol.equals(actualCurrencySymbol);
				LOGGER.info("Price: " + s + ", currency symbol is match: " + isMatch);
			}
			catch (ArrayIndexOutOfBoundsException e) {
				LOGGER.warn("There are no currency symbol in price: " + s, e);
			}
		};
	}
	
	private static void initLogger() throws IOException {
    	PatternLayout pattern = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%t - %m%n");

    	WriterAppender consoleAppender = new WriterAppender(pattern, System.out);

    	consoleAppender.setThreshold(Level.INFO);

    	RollingFileAppender fileAppender = new RollingFileAppender(pattern, 
    			"logs/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log", true);

    	fileAppender.setMaxFileSize("5MB");
    	fileAppender.setMaxBackupIndex(0);

    	fileAppender.setThreshold(Level.INFO);

   		Logger.getRootLogger().addAppender(consoleAppender);
   		Logger.getRootLogger().addAppender(fileAppender);
   	}
}
