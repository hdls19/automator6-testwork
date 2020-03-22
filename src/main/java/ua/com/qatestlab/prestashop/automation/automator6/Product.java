package ua.com.qatestlab.prestashop.automation.automator6;

public class Product {

	private int discount;
	private float oldPrice;
	private float price;
	private String currency;
	
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
