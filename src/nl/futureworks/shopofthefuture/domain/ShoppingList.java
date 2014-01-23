package nl.futureworks.shopofthefuture.domain;

import java.util.ArrayList;

public class ShoppingList {
	
	private int id;
	private int userID;
	private String name;
	private ArrayList<ShoppingListItem> items;
	
	public ShoppingList(int id, int userID, String name, ArrayList<ShoppingListItem> items) {
		this.id = id;
		this.userID = userID;
		this.name = name;
		this.items = items;
	}
	
	/**
	 * Add an item to the shopping list 
	 * @param item, the item to be added
	 */
	public void addItem(ShoppingListItem item) {
		items.add(item);
	}
	
	/**
	 * Removes the provided item from the shopping list
	 * @param item, the item to be removed
	 */
	public void removeItem(ShoppingListItem item) {
		for(int i=0 ; i<items.size(); i++){
			if(item.equals(items.get(i))){
				items.remove(i);
			}
		}
	}
	
	/**
	 * Replaces the old shopping list with a new one
	 * Old shopping list will be lost
	 * @param items
	 */
	public void replaceItemList(ArrayList<ShoppingListItem> items) {
		this.items = items;
	}
	
	/**
	 * Getter for id
	 * @return
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Getter for userID
	 * @return
	 */
	public int getUserID() {
		return userID;
	}
	
	/**
	 * Getter for name
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for itemList
	 * @return
	 */
	public ArrayList<ShoppingListItem> getItems() {
		return items;
	}
}
