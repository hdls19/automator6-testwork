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
		LOGGER.info("Title test");
		
		String expectedTitle = "prestashop-automation";
		String actualTitle = mainPage.getTitle();
		LOGGER.info("Actual title: " + actualTitle);
		LOGGER.info("Expected title: " + expectedTitle);
		LOGGER.info("Is match: " + expectedTitle.equals(actualTitle));
		
		//Currency test
		LOGGER.info("Currency test");
		
		String expectedCurrency = "UAH ₴";
		String actualCurrency = mainPage.getCurrency();
		LOGGER.info("Actual currency: " + actualCurrency);
		LOGGER.info("Expected currency: " + expectedCurrency);
		LOGGER.info("Is match: " + expectedCurrency.equals(actualCurrency));
		
		//Prices test
		LOGGER.info("Prices test");
		
		String expectedCurrencySymbol = expectedCurrency.split(" ")[1];
		List<String> prices = mainPage.getPrices();
		prices.forEach(s -> {
			try {
				String actualCurrencySymbol = s.split(" ")[1];
				boolean isMatch = expectedCurrencySymbol.equals(actualCurrencySymbol);
				LOGGER.info("Price: " + s + ", currency symbol is match: " + isMatch);
			}
			catch (ArrayIndexOutOfBoundsException e) {
				LOGGER.warn("There are no currency symbol in price: " + s, e);
			}
		});
		
		//Close web driver
		LOGGER.info("Close web driver");
		driver.close();
		driver.quit();
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
