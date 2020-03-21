package ua.com.qatestlab.prestashop.automation.automator6;

import org.openqa.selenium.WebDriver;

public class MainPage {

	private final WebDriver driver;
	
	public MainPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getTitle() {
		return driver.getTitle();
	}
}
