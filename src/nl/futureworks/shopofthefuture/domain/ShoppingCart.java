package nl.futureworks.shopofthefuture.domain;

import java.util.HashMap;


import nl.futureworks.shopofthefuture.exception.ShoppingListModificationException;


public class ShoppingCart extends ShoppingList{
	private static ShoppingCart cart;

	private ShoppingCart(int id, int userID, String name,HashMap<ShoppingListItem, Integer> items) {
		super(id, userID, name, items);
	}
	
	protected static ShoppingCart getInstance(int id, int userID, String name, HashMap<ShoppingListItem, Integer> items) {
		if (cart == null){
			cart = new ShoppingCart(id, userID, name, items);
			return cart;
		}
		else{
			return cart;
		}
	}
	
	public static ShoppingCart getInstance(int id, int userID) {
		if (cart == null){
			cart = new ShoppingCart(id, userID, "Shopping Cart", null);
			return cart;
		}
		else{
			return cart;
		}
		
	}
	
	/**
	 * Returns if the ShoppingCart exists yet
	 */
	public static boolean shoppingCartExists() {
		if (cart == null){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Add an item to the shopping cart 
	 * @param item, the item to be added
	 * 		  amount, the amount of this item
	 * @throws ShoppingListModificationException 
	 */
	public void addItem(ShoppingListItem item, int amount) throws ShoppingListModificationException {
		if((item != null && amount > 0) && !items.containsKey(item)) {
			items.put(item, amount);
		}
		else {
			throw new ShoppingListModificationException("Could not add item");
		}
	}
	
	/**
	 * Changes the amount of a single ShoppingItem
	 * @param item, the ShoppingItem in question
	 * @param amount, the new amount for this ShoppingItem
	 * @throws ShoppingListModificationException 
	 */
	public void changeAmount(ShoppingListItem item, int amount) throws ShoppingListModificationException {
		if((item != null && amount > 0) && items.containsKey(item)) {
			items.put(item, amount);
		} else {
			throw new ShoppingListModificationException("Could not change amount");
		}
	}
	
	/**
	 * Removes the provided item from the shopping cart
	 * @param item, the item to be removed
	 */
	public void removeItem(ShoppingListItem item) {
		if(item == null) {
			return;
		}
		
		for (ShoppingListItem key : items.keySet()) {
			if(item.equals(key)) {
				items.remove(key);
			}
		}
	}
	
	/**
	 * Get the amount of the ShoppingItem specified
	 * @param item, the ShoppingItem
	 * @return int, the amount of the specified ShoppingItem
	 */
	public int getItemAmount(ShoppingListItem item) {
		return items.get(item);
	}
	
	/**
	 * Clears the ShoppingCart
	 */
	public void clearCart() {
		cart = null;
	}

}
