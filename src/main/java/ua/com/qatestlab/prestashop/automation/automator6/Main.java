package ua.com.qatestlab.prestashop.automation.automator6;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import ua.com.qatestlab.prestashop.automation.automator6.pages.MainPage;
import ua.com.qatestlab.prestashop.automation.automator6.pages.SearchPage;


public class Main {

	private static final Logger LOGGER = LogManager.getLogger(Main.class);
	
	private static EventFiringWebDriver eventDriver;
	
	public static void main(String[] args) throws IOException {
		try {
			LOGGER.info("=======================================================");
			
			//Selenium web driver initialization
			LOGGER.info("Selenium web driver initialization");
			System.setProperty("webdriver.chrome.driver","chromedriver.exe");
			WebDriver driver = new ChromeDriver();
			
			eventDriver = new EventFiringWebDriver(driver);
			eventDriver.register(new LoggerEventHandler(LOGGER));
			
			//Go to main URL
			eventDriver.get("http://prestashop-automation.qatestlab.com.ua/ru/");
			MainPage mainPage = new MainPage(eventDriver);
			
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
			
			//Advanced search count test
			LOGGER.info("-------------------------------------------------------");
			LOGGER.info("Advanced search count test");
			LOGGER.info("-------------------------------------------------------");
			
			int pagesCount = searchPage.getPagesCount();
			int perPageCount = searchPage.getFindCountPerPage();
			int currentPageCount = searchPage.getPrices().size();
			int realCount = (pagesCount - 1) * perPageCount + currentPageCount;
			
			LOGGER.info("Actual count: " + actualCount);
			LOGGER.info("Real count: " + realCount);
			LOGGER.info("Is equals: " + (realCount == actualCount));
			
			//Search page currency test
			LOGGER.info("-------------------------------------------------------");
			LOGGER.info("Search page currency test");
			LOGGER.info("-------------------------------------------------------");
			
			expectedCurrencySymbol = "$";
			prices = searchPage.getPrices();
			checkPricesCurrency(prices, expectedCurrencySymbol);
			
			//Sort test
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
			}
			
			//Discount test
			LOGGER.info("-------------------------------------------------------");
			LOGGER.info("Discount test");
			LOGGER.info("-------------------------------------------------------");
			
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
					LOGGER.info("-------------------------------------------------------");
				}
 			}
		}
		catch (Exception e) {
			LOGGER.error("ERROR", e);
		}
		finally {
			//Close web driver
			LOGGER.info("-------------------------------------------------------");
			LOGGER.info("Close web driver");
			if (eventDriver != null) {
				eventDriver.close();
				eventDriver.quit();
			}
		}
	}
	
	private static void checkPricesCurrency(List<String> prices, String expectedCurrencySymbol) {
		for (String s: prices) {
			try {
				String actualCurrencySymbol = s.split(" ")[1];
				boolean isMatch = expectedCurrencySymbol.equals(actualCurrencySymbol);
				LOGGER.info(String.format("Price: %s, expected currency: %s, currency is match: %b", s,
						expectedCurrencySymbol, isMatch));
			}
			catch (ArrayIndexOutOfBoundsException e) {
				LOGGER.warn("There are no currency symbol in price: " + s, e);
			}
		};
	}
	
//	private static void initLogger() throws IOException {
//		ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
//    	
//		builder.setStatusLevel(Level.ERROR);
//		builder.setConfigurationName("RollingBuilder");
//		
//		//Create console appender
//		AppenderComponentBuilder appenderBuilder = builder.newAppender("stdout", "CONSOLE").addAttribute("target",
//				ConsoleAppender.Target.SYSTEM_OUT);
//		appenderBuilder.add(builder.newLayout("PatternLayout").addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable"));
//		
//		builder.add(appenderBuilder);
//		
//		//Create rolling file appender
//		LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
//				.addAttribute("pattern", "%d [%t] %-5level: %msg%n");
//		ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
//				.addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
//				.addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "100M"));
//		appenderBuilder = builder.newAppender("rolling", "RollingFile")
//				.addAttribute("fileName", "logs/rolling.log")
//				.addAttribute("filePattern", "logs/rolling-%d{MM-dd-yy}.log")
//				.add(layoutBuilder)
//				.addComponent(triggeringPolicy);
//		builder.add(appenderBuilder);
//		
//		builder.add(builder.newLogger("TestLogger", Level.DEBUG)
//				.add(builder.newAppenderRef("rolling"))
//				.addAttribute("additivity", false));
//		
//		builder.add(builder.newRootLogger(Level.DEBUG)
//				.add(builder.newAppenderRef("rolling")));
//		
//		Configurator.initialize(builder.build());
//   	}
}
