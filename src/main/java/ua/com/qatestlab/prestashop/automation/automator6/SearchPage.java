package ua.com.qatestlab.prestashop.automation.automator6;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPage extends MainPage {

	private By findCountLocator = By.xpath("//*[@id=\"js-product-list-top\"]/div[1]/p");
	private By showsLabelLocator = By.xpath("//*[@id=\"js-product-list\"]/nav/div[1]");
	
	public SearchPage(WebDriver driver) {
		super(driver);
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
