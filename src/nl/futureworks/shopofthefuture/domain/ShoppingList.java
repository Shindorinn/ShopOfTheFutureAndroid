package nl.futureworks.shopofthefuture.domain;

import java.util.HashMap;

public class ShoppingList {
	
	private int id;
	private int userID;
	private String name;
	
	private HashMap<ShoppingListItem, Integer> items;
	
	public ShoppingList(int id, int userID, String name, HashMap<ShoppingListItem, Integer> items) {
		this.id = id;
		this.userID = userID;
		this.name = name;
		this.items = items;
	}
	
	/**
	 * Add an item to the shopping list 
	 * @param item, the item to be added
	 * 		  amount, the amount of this item
	 */
	public void addItem(ShoppingListItem item, int amount) {
		if((item != null && amount > 0) && !items.containsKey(item)) {
			items.put(item, amount);
		}
	}
	
	/**
	 * Changes the amount of a single ShoppingItem
	 * @param item, the ShoppingItem in question
	 * @param amount, the new amount for this ShoppingItem
	 */
	public void changeAmount(ShoppingListItem item, int amount) {
		if((item != null && amount > 0) && items.containsKey(item)) {
			items.put(item, amount);
		}
	}
	
	/**
	 * Removes the provided item from the shopping list
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
	 * Replaces the old shopping list with a new one
	 * Old shopping list will be lost
	 * @param items, the new HashMap with items and their amount
	 */
	public void replaceItemList(HashMap<ShoppingListItem, Integer> items) {
		this.items = items;
	}
	
	/**
	 * Returns whether the current ShoppingList contains the specified item
	 * @param item, the item to be checked
	 * @return boolean, itemExists
	 */
	public boolean itemExists(ShoppingListItem item) {
		return items.containsKey(item);	
	}
	
	/**
	 * Override of the toString() method for displaying ShoppingList 
	 * correctly in the ListView
	 */
	@Override
	public String toString(){
		return name;
	}
	
	/**
	 * Getter for id
	 * @return int, id
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Getter for userID
	 * @return int, userId
	 */
	public int getUserID() {
		return userID;
	}
	
	/**
	 * Getter for name
	 * @return String, name
	 */
	public String getName() {
		return name;
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
	 * Getter for itemList
	 * @return HashMap<ShoppingListItem, Integer>, List of items ShoppingItem -> amount
	 */
	public HashMap<ShoppingListItem, Integer> getItems() {
		return items;
	}
}
