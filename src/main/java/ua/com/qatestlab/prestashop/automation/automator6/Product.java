package ua.com.qatestlab.prestashop.automation.automator6;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Product {

	private static final Logger LOGGER = LogManager.getLogger(Product.class);
	
	private int discount;
	private float oldPrice = -1;
	private float price;
	private String currency;
	
	public static Product parseProduct(String s) throws Exception {
		int discount = 0;
		try {
			discount = Integer.parseInt(StringParser.parseString(s, "%", "discount-percentage", ">", "-"));
		}
		catch (Exception e) {
			//LOGGER.warn("Failed to parse discount");
		}
		
		float oldPrice = -1;
		try {
			String oldPriceText = StringParser.parseString(s, "<", "regular-price", ">");
			String[] split = oldPriceText.split("&nbsp;");
			oldPrice = Float.parseFloat(split[0].replace(",", "."));
		}
		catch (Exception e) {
			//LOGGER.warn("Failed to parse old price");
		}
		
		String priceText = StringParser.parseString(s, "<", "class=\"price\"", ">");
		String[] split = priceText.split("&nbsp;");
		float price = Float.parseFloat(split[0].replace(",", "."));
		String currency = split[1];
		
		Product product = new Product();
		product.setDiscount(discount);
		product.setCurrency(currency);
		product.setOldPrice(oldPrice);
		product.setPrice(price);
		return product;
	}
	
	public boolean hasDiscount() {
		return discount > 0;
	}
	
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public float getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(float oldPrice) {
		this.oldPrice = oldPrice;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
