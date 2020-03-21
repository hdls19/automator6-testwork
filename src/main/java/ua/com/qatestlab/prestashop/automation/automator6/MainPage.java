package ua.com.qatestlab.prestashop.automation.automator6;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MainPage {

	private static final int DEFAULT_WAITING_TIME = 10; //seconds
	
	private final WebDriver driver;
	private final WebDriverWait driverWait;
	
	private By currentCurrencyLocator = By.xpath("//*[@id=\"_desktop_currency_selector\"]/div/span[2]");
	private By usdCurrencyLocator = By.xpath("//a[text()=\"USD $\"]");
	private By pricesLocator = By.xpath("//span[@itemprop=\"price\"]");
	
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
	
	public String getTitle() {
		return driver.getTitle();
	}
	
	public String getCurrency() {
		driverWait.until(ExpectedConditions.presenceOfElementLocated(currentCurrencyLocator));
		return driver.findElement(currentCurrencyLocator).getText();
	}
	
	public List<String> getPrices() {
		List<String> prices = new ArrayList<String>();
		List<WebElement> elements = driver.findElements(pricesLocator);
		elements.forEach(e -> prices.add(e.getText()));
		return prices;
	}
}
