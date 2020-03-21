package ua.com.qatestlab.prestashop.automation.automator6;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {

	public static void main(String[] args) {
		//Selenium web driver initialization
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		//Simple test
		String baseURL = "https://google.com";
		String expectedTitle = "Google";
		
		driver.get(baseURL);
		String actualTitle = driver.getTitle();
		
		System.out.println("Expected title: " + expectedTitle);
		System.out.println("Actual title: " + actualTitle);
		
		//Close web driver
		driver.close();
	}
}
