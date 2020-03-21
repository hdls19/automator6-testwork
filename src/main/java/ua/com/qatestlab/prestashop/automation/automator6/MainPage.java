package ua.com.qatestlab.prestashop.automation.automator6;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MainPage {

	private static final String CURRENCY_SELECTOR_XPATH = "//*[@id=\"_desktop_currency_selector\"]/div/span[2]";
	private static final String PRICES_SELECTOR_XPATH = "//span[@itemprop=\"price\"]";
	
	private final WebDriver driver;
	
	public MainPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getTitle() {
		return driver.getTitle();
	}
	
	public String getCurrency() {
		return driver.findElement(By.xpath(CURRENCY_SELECTOR_XPATH)).getText();
	}
	
	public List<String> getPrices() {
		List<String> prices = new ArrayList<String>();
		List<WebElement> elements = driver.findElements(By.xpath(PRICES_SELECTOR_XPATH));
		elements.forEach(e -> prices.add(e.getText()));
		return prices;
	}
}
