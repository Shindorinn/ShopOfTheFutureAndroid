package nl.futureworks.shopofthefuture.domain;

public class ShoppingListItem {
	private final String barcode;
	private String name;
	private double price;
	
	public ShoppingListItem(String barcode, String name, double price) {
		this.barcode = barcode;
		this.name = name;
		this.price = price;
	}
	
	/**
	 * Sets a new name for this ShoppingItem
	 * @param name, the new name for this ShoppingItem
	 */
	public void setName(String name) {
		if(name != null && !name.equals("")){
			this.name = name;
		}
	}
	
	/**
	 * Sets a new price for this ShoppingItem
	 * @param price, the new price for this ShoppingItem
	 */
	public void setPrice(double price) {
		if(price >= 0) {
			this.price = price;
		}
	}
	
	/**
	 * Returns the barcode of this ShoppingItem
	 * @return String, barcode
	 */
	public String getBarcode() {
		return barcode;
	}
	
	/**
	 * Returns the name of this ShoppingItem
	 * @return String, name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the price of this ShoppingItem
	 * @return Double, price
	 */
	public Double getPrice() {
		return price;
	}
}
