package ua.com.qatestlab.prestashop.automation.automator6;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SearchPage extends MainPage {

	private By findCountLocator = By.xpath("//*[@id=\"js-product-list-top\"]/div[1]/p");
	private By showsLabelLocator = By.xpath("//*[@id=\"js-product-list\"]/nav/div[1]");
	private By sortDropdownLocator = By.xpath("//div[contains(@class, \"products-sort-order\")]");
	private By sortPriceFromHighToLowLocator = By.xpath("//div[contains(@class, \"products-sort-order\")]/div/a[5]");
	
	public SearchPage(WebDriver driver) {
		super(driver);
	}
	
	public SearchPage sortPriceFromHighToLow() {
		driver.findElement(sortDropdownLocator).click();
		driverWait.until(ExpectedConditions.elementToBeClickable(sortPriceFromHighToLowLocator));
		driver.findElement(sortPriceFromHighToLowLocator).click();
		
		try {
			Thread.sleep(1500);
		}
		catch (Exception e) {
		}
		
		return new SearchPage(driver);
	}
	
	public int getFindCount() {
		String findMessage = driver.findElement(findCountLocator).getText();
		return Integer.parseInt(StringParser.parseString(findMessage, ".", " "));
	}
	
	public int getPagesCount() {
		//TODO in current page max pages count is 1
		return 1;
	}
	
	public int getFindCountPerPage() {
		String showLabelText = driver.findElement(showsLabelLocator).getText();
		String[] split = showLabelText.split(" ")[1].split("-");
		int from = Integer.parseInt(split[0]);
		int to = Integer.parseInt(split[1]);
		return to - from + 1;
	}
	
}
