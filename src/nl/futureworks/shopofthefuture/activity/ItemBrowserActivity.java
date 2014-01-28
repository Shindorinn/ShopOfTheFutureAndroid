package nl.futureworks.shopofthefuture.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import nl.futureworks.shopofthefuture.R;
import nl.futureworks.shopofthefuture.domain.ShoppingCart;
import nl.futureworks.shopofthefuture.domain.ShoppingList;
import nl.futureworks.shopofthefuture.domain.ShoppingListItem;
import nl.futureworks.shopofthefuture.exception.ShoppingListModificationException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
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

public class ItemBrowserActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {
	private ShoppingList selectedShoppingList;
	private ShoppingCart cart;
	private int selectedId;
	private ConcurrentHashMap<ShoppingListItem, Integer> items;
	private SparseArray<ShoppingListItem> idToObject;
	
	private SimpleAdapter adapter;
	private LinkedList<HashMap<String, String>> shoppingListItemArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_browser);
		
		selectedShoppingList = getIntent().getParcelableExtra("SelectedList");
		idToObject = new SparseArray<ShoppingListItem>();
		
		items = selectedShoppingList.getItems();
		setTitle(selectedShoppingList.getName());
		
		if (items != null && items.size() > 0) {
			displayItems(savedInstanceState);
		} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_browser, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch(item.getItemId())
	    {
	    case R.id.convert_to_cart:
	        cart = selectedShoppingList.convertToCart();
	        displayItems(null);
	    }
	    return true;
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		final ShoppingListItem cartItem = idToObject.get(selectedId);
		
		switch(item.getItemId())
	    {
			case R.id.delete_item:
				cart.removeItem(cartItem);
				idToObject.remove(selectedId);
				
				displayItems(null);
				break;
	        
			case R.id.change_amount:
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
					
				break;
	    }
		return true;
	}
	
	/**
	 * Save instance state to restore the ListView after onStop() call 
	 */
	public void onSaveInstanceState(Bundle savedState) {
	    super.onSaveInstanceState(savedState);
	    
	    savedState.putSerializable("ItemArray", shoppingListItemArray);
	    savedState.putSparseParcelableArray("IdToObject", idToObject);
	    savedState.putParcelable("ShoppingCart", cart);
	    savedState.putInt("SelectedId", selectedId);
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
			this.cart = (ShoppingCart) savedInstanceState.getParcelable("ShoppingCart");
			this.shoppingListItemArray = (LinkedList<HashMap<String, String>>) savedInstanceState.getSerializable("ItemArray");
			this.idToObject = (SparseArray) savedInstanceState.getSparseParcelableArray("IdToObject");
			this.selectedId = savedInstanceState.getInt("SelectedId");
	       
	    } 
		
		//Check if the shopping cart is activated
		else if (ShoppingCart.shoppingCartExists()) {
				setTitle("Shopping Cart");
        		cart = selectedShoppingList.convertToCart();
        		
	        	this.shoppingListItemArray = itemsToStringArray(cart.getItems());
	        	this.selectedId = -1;
	    }
		
		else {
			this.shoppingListItemArray = itemsToStringArray(items);
        	this.selectedId = -1;
		}
		
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
}
