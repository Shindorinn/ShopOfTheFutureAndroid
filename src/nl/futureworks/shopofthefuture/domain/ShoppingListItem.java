package nl.futureworks.shopofthefuture.domain;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ShoppingListItem implements Parcelable{
	private final String barcode;
	private String name;
	private double price;
	
	public ShoppingListItem(String barcode, String name, double price) {
		this.barcode = barcode;
		this.name = name;
		this.price = price;
	}
	
	private ShoppingListItem(Parcel in) {
		this.barcode = in.readString();
		this.name = in.readString();
		this.price = in.readDouble();
	}

	// this is used to regenerate the object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ShoppingListItem> CREATOR = new Parcelable.Creator<ShoppingListItem>() {
        public ShoppingListItem createFromParcel(Parcel in) {
            return new ShoppingListItem(in);
        }

        public ShoppingListItem[] newArray(int size) {
            return new ShoppingListItem[size];
        }
    };
	
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(barcode);
		dest.writeString(name);
		dest.writeDouble(price);	
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof ShoppingListItem){
		  ShoppingListItem toCompare = (ShoppingListItem) o;
		  return this.barcode.equals(toCompare.barcode);
		}
		return false;
	}
	
	/**
	 * Turn this obj into a JSONObject
	 * 
	 * @return JSONObject
	 */
	public JSONObject toJSON(){
		JSONObject toReturn = new JSONObject();
		try {
			toReturn.put("barcode", this.barcode);
			toReturn.put("name", this.name);
			toReturn.put("price", this.price);
		} catch (JSONException e) {
			// nop
		}
		
		return toReturn;
	}
	
}
