package ua.com.qatestlab.prestashop.automation.automator6;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPage extends MainPage {

	private By findCountLocator = By.xpath("//*[@id=\"js-product-list-top\"]/div[1]/p");
	
	public SearchPage(WebDriver driver) {
		super(driver);
	}
	
	public int getFindCount() {
		String findMessage = driver.findElement(findCountLocator).getText();
		return Integer.parseInt(StringParser.parseString(findMessage, ".", " "));
	}
}
