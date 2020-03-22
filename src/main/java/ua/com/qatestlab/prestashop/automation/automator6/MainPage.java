package ua.com.qatestlab.prestashop.automation.automator6;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {

	private static final Logger LOGGER = LogManager.getLogger(MainPage.class);
	
	private static final int DEFAULT_WAITING_TIME = 10; //seconds
	
	protected final WebDriver driver;
	protected final WebDriverWait driverWait;
	
	protected By currentCurrencyLocator = By.xpath("//*[@id=\"_desktop_currency_selector\"]/div/span[2]");
	protected By usdCurrencyLocator = By.xpath("//a[text()=\"USD $\"]");
	protected By pricesLocator = By.xpath("//span[@itemprop=\"price\"]");
	protected By searchLocator = By.xpath("//*[@id=\"search_widget\"]/form/input[2]");
	
	public MainPage(WebDriver driver) {
		this.driver = driver;
		this.driverWait = new WebDriverWait(driver, DEFAULT_WAITING_TIME);
	}
	
	public MainPage setUSDCurrency() {
		driverWait.until(ExpectedConditions.elementToBeClickable(currentCurrencyLocator));
		driver.findElement(currentCurrencyLocator).click();
		
		driverWait.until(ExpectedConditions.elementToBeClickable(usdCurrencyLocator));
		driver.findElement(usdCurrencyLocator).click();
		
		return new MainPage(driver);
	}
	
	public SearchPage searchFor(String search) {
		WebElement searchElement = driver.findElement(searchLocator);
		searchElement.sendKeys(search);
		searchElement.submit();
		return new SearchPage(driver);
	}
	
	public List<String> getPrices() {
		List<String> prices = new ArrayList<String>();
		List<WebElement> elements = driver.findElements(pricesLocator);
		elements.forEach(e -> prices.add(e.getText()));
		return prices;
	}
	
	public String getCurrency() {
		try {
			driverWait.until(ExpectedConditions.presenceOfElementLocated(currentCurrencyLocator));
		}
		catch (TimeoutException e) {
			LOGGER.warn("Failed to get currency. Refresh page and try again", e);
			driver.navigate().refresh();
			driverWait.until(ExpectedConditions.presenceOfElementLocated(currentCurrencyLocator));
		}
		return driver.findElement(currentCurrencyLocator).getText();
	}
	
	public String getTitle() {
		return driver.getTitle();
	}
	
}
