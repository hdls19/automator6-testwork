package ua.com.qatestlab.prestashop.automation.automator6;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class Main {

	public static void main(String[] args) throws IOException {
		//Selenium web driver initialization
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		//Title test
		String mainURL = "http://prestashop-automation.qatestlab.com.ua/ru/";
		String expectedTitle = "prestashop-automation";
		
		driver.get(mainURL);
		
		MainPage mainPage = new MainPage(driver);
		
		String actualTitle = mainPage.getTitle();
		System.out.println("Actual title: " + actualTitle);
		System.out.println("Expected title: " + expectedTitle);
		
		//Close web driver
		driver.close();
		driver.quit();
	}
}
