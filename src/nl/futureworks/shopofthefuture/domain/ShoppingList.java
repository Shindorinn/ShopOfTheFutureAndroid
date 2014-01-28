package nl.futureworks.shopofthefuture.domain;

import java.util.concurrent.ConcurrentHashMap;

import android.os.Parcel;
import android.os.Parcelable;

public class ShoppingList implements Parcelable {
	
	private int id;
	private int userID;
	private String name;
	
	protected ConcurrentHashMap<ShoppingListItem, Integer> items;
	
	public ShoppingList(int id, int userID, String name, ConcurrentHashMap<ShoppingListItem, Integer> items) {
		this.id = id;
		this.userID = userID;
		this.name = name;
		this.items = items;
	}
	
	private ShoppingList(Parcel in) {
		this.id = in.readInt();
		this.userID = in.readInt();
		this.name = in.readString();
		items = new ConcurrentHashMap<ShoppingListItem, Integer>();
		in.readMap(items, ShoppingListItem.class.getClassLoader());
    }
	
	// this is used to regenerate the object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ShoppingList> CREATOR = new Parcelable.Creator<ShoppingList>() {
        public ShoppingList createFromParcel(Parcel in) {
            return new ShoppingList(in);
        }

        public ShoppingList[] newArray(int size) {
            return new ShoppingList[size];
        }
    };
	
	/**
	 * Replaces the old shopping list with a new one
	 * Old shopping list will be lost
	 * @param items, the new HashMap with items and their amount
	 */
	public void replaceItemList(ConcurrentHashMap<ShoppingListItem, Integer> items) {
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
	 * Converts the current Shopping List as Shopping Cart and returns the ShoppingCart
	 * 
	 */
	public ShoppingCart convertToCart() {
		return ShoppingCart.getInstance(id, userID, name, items);
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
	 * Getter for itemList
	 * @return HashMap<ShoppingListItem, Integer>, List of items ShoppingItem -> amount
	 */
	public ConcurrentHashMap<ShoppingListItem, Integer> getItems() {
		return items;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(userID);
		dest.writeString(name);
		dest.writeMap(items);
	}
}
