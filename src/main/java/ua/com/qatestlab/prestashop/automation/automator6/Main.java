package ua.com.qatestlab.prestashop.automation.automator6;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class Main {

	private static final Logger LOGGER = LogManager.getLogger(Main.class);
	
	private static WebDriver driver;
	
	public static void main(String[] args) throws IOException {
		try {
			LOGGER.info("=======================================================");
			
			//Selenium web driver initialization
			LOGGER.info("Selenium web driver initialization");
			System.setProperty("webdriver.chrome.driver","chromedriver.exe");
			driver = new ChromeDriver();
			
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
			
			//Search count test
			LOGGER.info("-------------------------------------------------------");
			LOGGER.info("Search count test");
			LOGGER.info("-------------------------------------------------------");
			
			int expectedCount = 7;
			int actualCount = searchPage.getFindCount();
			LOGGER.info("Actual count: " + actualCount);
			LOGGER.info("Expected count: " + expectedCount);
			LOGGER.info("Is equals: " + (expectedCount == actualCount));
		}
		catch (Exception e) {
			LOGGER.error("ERROR", e);
		}
		finally {
			//Close web driver
			LOGGER.info("-------------------------------------------------------");
			LOGGER.info("Close web driver");
			if (driver != null) {
				driver.close();
				driver.quit();
			}
		}
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
		ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
    	
		builder.setStatusLevel(Level.ERROR);
		builder.setConfigurationName("RollingBuilder");
		
		//Create console appender
		AppenderComponentBuilder appenderBuilder = builder.newAppender("stdout", "CONSOLE").addAttribute("target",
				ConsoleAppender.Target.SYSTEM_OUT);
		appenderBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));
		
		builder.add(appenderBuilder);
		
		//Create rolling file appender
		LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
				.addAttribute("pattern", "%d [%t] %-5level: %msg%n");
		ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
				.addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
				.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"));
		appenderBuilder = builder.newAppender("rolling", "RollingFile")
				.addAttribute("fileName", "logs/rolling.log")
				.addAttribute("filePattern", "logs/rolling-%d{MM-dd-yy}.log")
				.add(layoutBuilder)
				.addComponent(triggeringPolicy);
		builder.add(appenderBuilder);
		
		builder.add(builder.newLogger("TestLogger", Level.DEBUG)
				.add(builder.newAppenderRef("rolling"))
				.addAttribute("additivity", false));
		
		builder.add(builder.newRootLogger(Level.DEBUG)
				.add(builder.newAppenderRef("rolling")));
		
		Configurator.initialize(builder.build());
   	}
}
