package ua.com.qatestlab.prestashop.automation.automator6;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage {

	private static final String CURRENCY_SELECTOR_XPATH = "//*[@id=\"_desktop_currency_selector\"]/div/span[2]";
	
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
}
