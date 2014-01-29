package nl.futureworks.shopofthefuture.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import nl.futureworks.shopofthefuture.R;
import nl.futureworks.shopofthefuture.domain.ShoppingCart;
import nl.futureworks.shopofthefuture.domain.ShoppingList;
import nl.futureworks.shopofthefuture.domain.ShoppingListItem;
import nl.futureworks.shopofthefuture.exception.CheckoutException;
import nl.futureworks.shopofthefuture.exception.ShoppingListModificationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ItemBrowserActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {
	private ShoppingList selectedShoppingList;
	private ShoppingCart cart;
	private int selectedId;
	private String title;
	private ConcurrentHashMap<ShoppingListItem, Integer> items;
	private SparseArray<ShoppingListItem> idToObject;
	
	private SimpleAdapter adapter;
	private LinkedList<HashMap<String, String>> shoppingListItemArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initializeLayout(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (ShoppingCart.shoppingCartExists()) {
			getMenuInflater().inflate(R.menu.item_browser_cart, menu);
		}
		else {
			getMenuInflater().inflate(R.menu.item_browser_list, menu);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId())
	    {
	    case R.id.convert_to_cart:
	        cart = selectedShoppingList.convertToCart();
	        title = getString(R.string.title_shopping_cart);
	        this.recreate();
	        break;
	        
	    case R.id.clear_cart:
	    	cart.clearCart();
	    	this.finish();
	    	break;
	    	
	    case R.id.scan_barcode:
	    	scanBarcode();
	    	break;
	    
	    case R.id.checkout_cart:
			try {
				checkoutCart();
			} catch (CheckoutException e) {
				e.printStackTrace();
			}
	    	break;
	    }
	    	
	    return true;
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		final ShoppingListItem cartItem = idToObject.get(selectedId);
		
		switch(item.getItemId())
	    {
			case R.id.delete_item:
				deleteItem(cartItem);
				break;
	        
			case R.id.change_amount:
				changeAmount(cartItem);	
				break;
	    }
		return true;
	}
	
	/**
	 * Save instance state to restore the ListView after onStop() call 
	 */
	public void onSaveInstanceState(Bundle savedState) {
	    super.onSaveInstanceState(savedState);
	    
	    savedState.putString("Title", title);
	    savedState.putSerializable("ItemArray", shoppingListItemArray);
	    savedState.putSparseParcelableArray("IdToObject", idToObject);
	    savedState.putParcelable("ShoppingCart", cart);
	    savedState.putInt("SelectedId", selectedId);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            
            String barcode = scanResult.getContents();
            List<HashMap<String, String>> result = db.sendQuery("item", null, "barcode = '" + barcode + "'", null, null, null, null, "1");
            
            if (result == null) {
            	// TODO : Check api if products exists there
            	Toast toast = Toast.makeText(this, R.string.no_product_found, Toast.LENGTH_LONG);
	            toast.show();
            	return;
            }
            
            HashMap<String, String> row = result.get(0);
            String name = row.get("name");
            double price = Double.parseDouble(row.get("price"));
            
            ShoppingListItem item = new ShoppingListItem(barcode, name, price);
            
            if (!cart.contains(item)) {
            	try {
					cart.addItem(item, 1);
					displayItems(null);
				} catch (ShoppingListModificationException e) {
					e.printStackTrace();
				}
	            Toast toast = Toast.makeText(this, scanResult.getContents(), Toast.LENGTH_LONG);
	            toast.show();
            }
	            
	        else {
	        	ShoppingListItem memoryCorrectItem = cart.getDuplicate(item); 
	        	String[] options = new String[] {getString(R.string.delete_item), getString(R.string.change_amount)};
	        	createOptionDialog(R.string.amount_title, R.string.item_exists, memoryCorrectItem, options).show();
	        }
        }
    }
	
	public void scanBarcode() {
		IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
	}
	
	/**
	 * Displays all the ShoppingListItems in a table-like ListView
	 * @param savedInstanceState
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void displayItems(Bundle savedInstanceState) {
		ListView listView = (ListView) findViewById(R.id.item_browser);
		
		//Check if savedInstanceState is set and restore ListView when possible
		if (savedInstanceState != null) {
			this.title = savedInstanceState.getString("Title");
			this.cart = (ShoppingCart) savedInstanceState.getParcelable("ShoppingCart");
			this.shoppingListItemArray = (LinkedList<HashMap<String, String>>) savedInstanceState.getSerializable("ItemArray");
			this.idToObject = (SparseArray) savedInstanceState.getSparseParcelableArray("IdToObject");
			this.selectedId = savedInstanceState.getInt("SelectedId");
	       
	    } 
		
		//Check if the shopping cart is activated
		else if (ShoppingCart.shoppingCartExists()) {
    		cart = selectedShoppingList.convertToCart();
    		
        	this.shoppingListItemArray = itemsToStringArray(cart.getItems());
        	this.selectedId = -1;
	    }
		
		else {
			this.shoppingListItemArray = itemsToStringArray(items);
        	this.selectedId = -1;
		}
		
		//Set the title
		setTitle(this.title);
		
		//Initialize Adapter
		adapter = new SimpleAdapter(this, shoppingListItemArray, R.layout.item_browser_row,
			      new String[] {"name", "price", "amount"}, new int[] {R.id.name_cell, R.id.price_cell, R.id.amount_cell});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0 || !ShoppingCart.shoppingCartExists()){
					return false;
				}
				selectedId = arg2;
				showPopup(arg1);
				
				return true;
			}	
		});
	}
	
	/**
	 * Shows a popup menu
	 * @param v
	 */
	private void showPopup(View v) {
	    PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.item_popup_menu, popup.getMenu());
	    popup.setOnMenuItemClickListener(this);
	    popup.show();
	}
	
	/**
	 * Return an custom AlertDialog.Builder
	 * @param input
	 * @param title
	 * @param message
	 * @return
	 */
	private AlertDialog.Builder getAlertDialog(EditText input, int title, int message) {
		//Request user input
		return new AlertDialog.Builder(this)
	    .setTitle(getString(title))
	    .setMessage(getString(message))
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        }
	    });
	}
	
	/**
	 * Create an option dialog
	 * @param choiceList
	 * @return
	 */
	private Dialog createOptionDialog(int title, int message, ShoppingListItem item, final String[] choiceList) {
	    final ShoppingListItem cartItem = item;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(getString(title));
	    //builder.setMessage(getString(message));
	    builder.setSingleChoiceItems(choiceList, -1, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {   

	        }
	    });
	   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
	        	
	        	if (choiceList[selectedPosition].equals(getString(R.string.delete_item))) {
	        		deleteItem(cartItem);
	        	}
	        	else if (choiceList[selectedPosition].equals(getString(R.string.change_amount))) {
	        		changeAmount(cartItem);
	        	}
	        }
	    });
	    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        }
	    });
	    return builder.create();
	}
	
	/**
	 * Creates a LinkedList representation of the ShoppingListItem for the ListView 
	 * @return
	 */
	private LinkedList<HashMap<String, String>> itemsToStringArray(ConcurrentHashMap<ShoppingListItem, Integer> items) {
		int i = 1;
		LinkedList<HashMap<String, String>> itemArray = new LinkedList<HashMap<String, String>>();
		for(Entry<ShoppingListItem, Integer> en: items.entrySet()){
			ShoppingListItem item = en.getKey();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", item.getName());
			map.put("price", "€ " + item.getPrice().toString());
			map.put("amount", en.getValue().toString());
			itemArray.add(map);
			idToObject.append(i, item);
			i++;
		}
		
		//Add colomn names
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", getString(R.string.item_colomn_name));
		map.put("price", getString(R.string.item_colomn_price));
		map.put("amount", getString(R.string.item_colomn_amount));
		itemArray.addFirst(map);
		
		return itemArray;
	}
	
	/**
	 * Deletes an item from the ShoppingCart
	 * @param cartItem
	 */
	private void deleteItem(ShoppingListItem cartItem) {
		cart.removeItem(cartItem);
		idToObject.remove(selectedId);
		
		displayItems(null);
	}
	
	/**
	 * Changes the amount of an ShoppingCartItem
	 * @param item
	 */
	private void changeAmount(ShoppingListItem item) {
		final ShoppingListItem cartItem = item;
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		AlertDialog.Builder builder = getAlertDialog(input, R.string.amount_title, R.string.amount_message);
		final AlertDialog dialog = builder.create();
		dialog.show();
		
		Button okButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = input.getText().toString();
	        	
	        	//Check input
	        	if (text.equals("")) {
	            	input.setError(getString(R.string.error_no_text));
	            	return;
	            }
	        	else if (text.length() > 5) {
	        		input.setError(getString(R.string.error_invalid_int));
	            	return;
	        	}
	        	
	        	int value = Integer.parseInt(text);
	        	if (value <= 0) {
	        		input.setError(getString(R.string.error_invalid_amount));
	            	return;
	        	}
	        	
	            try {
					cart.changeAmount(cartItem, value);
					displayItems(null);
					dialog.dismiss();
				} catch (ShoppingListModificationException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Sends the ShoppingCart to the register 
	 * @throws CheckoutException 
	 */
	@SuppressWarnings("unchecked")
	private void checkoutCart() throws CheckoutException {
		ConcurrentHashMap<ShoppingListItem, Integer> cartItems = cart.getItems();
		
		if (cartItems != null && !cartItems.isEmpty()) {
			int userId = cart.getUserID();
			ArrayList<?>[] convertedCartItems = convertCartItems(cartItems);
			
			if (convertedCartItems != null && convertedCartItems.length == 2) {
				ArrayList<ShoppingListItem> shoppingListItems = (ArrayList<ShoppingListItem>) convertedCartItems[0]; 
				ArrayList<Integer> amountList = (ArrayList<Integer>) convertedCartItems[1];
				
				try {
					JSONObject jsonObject = createCheckoutJSON(shoppingListItems, amountList, userId);
					Log.d("JSONCheckout", jsonObject.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}			
			}
			else {
				throw new CheckoutException("HashMap converting failed");
			}
		}
		
		else {
			Toast toast = Toast.makeText(this, R.string.no_items_found, Toast.LENGTH_LONG);
            toast.show();
		}
		
	}
	
	/**
	 * Converts Hashmap to dual array 
	 * @return
	 */
	private ArrayList<?>[] convertCartItems(ConcurrentHashMap<ShoppingListItem, Integer> cartItems) {
		ArrayList<ShoppingListItem> items = new ArrayList<ShoppingListItem>();
		ArrayList<Integer> amountList = new ArrayList<Integer>();
		
		for (Entry<ShoppingListItem, Integer> entry : cartItems.entrySet()) {
		    ShoppingListItem item = entry.getKey();
		    items.add(item);
		    Integer amount = entry.getValue();
		    amountList.add(amount);
		}
		
		return new ArrayList<?>[] {items, amountList};
	}
	
	private JSONObject createCheckoutJSON(ArrayList<ShoppingListItem> items, ArrayList<Integer> amounts, int userid) throws JSONException{
//		JSONStringer builder = new JSONStringer();
//		builder.object()
//		.value(userid)
//		.array().value(items).endArray()
//		.array().value(amounts).endArray()
//		.endObject();
//		Log.d("JSONBUILDER", builder.toString());
//		return new JSONObject(builder.toString());
		
		JSONObject toReturn = new JSONObject();
		toReturn.put("userid", userid);
		ArrayList<JSONObject> tempList = new ArrayList<JSONObject>();
		for(int index = 0; index < items.size(); index++){
			tempList.add(items.get(index).toJSON().put("amount", amounts.get(index)));
		}
		JSONArray jsonArray = new JSONArray(tempList);
		
		toReturn.put("items", jsonArray);
		
		return toReturn;
	}
	
	
	/**
	 * Initialize the layout
	 * @param savedInstanceState
	 */
	private void initializeLayout(Bundle savedInstanceState) {
		setContentView(R.layout.activity_item_browser);
		
		selectedShoppingList = getIntent().getParcelableExtra("SelectedList");
		idToObject = new SparseArray<ShoppingListItem>();
		
		title = selectedShoppingList.getName();
		
		items = selectedShoppingList.getItems();
		
		if (items != null && items.size() > 0) {
			displayItems(savedInstanceState);
		} 
	}
}
