package nl.futureworks.shopofthefuture.activity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import nl.futureworks.shopofthefuture.R;
import nl.futureworks.shopofthefuture.domain.ShoppingCart;
import nl.futureworks.shopofthefuture.domain.ShoppingList;
import nl.futureworks.shopofthefuture.domain.ShoppingListItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ItemBrowserActivity extends Activity {
	private ShoppingList selectedShoppingList; 
	private HashMap<ShoppingListItem, Integer> items;
	
	private SimpleAdapter adapter;
	private LinkedList<HashMap<String, String>> shoppingListItemArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_browser);
		
		selectedShoppingList = getIntent().getParcelableExtra("SelectedList");
		
		items = selectedShoppingList.getItems();
		setTitle(selectedShoppingList.getName());
		
		if (items != null && items.size() > 0) {
			displayItems(savedInstanceState);
		} /* TODO NOT WORKING YET
		
		else {
			displayNoItemMessage();
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_browser, menu);
		return true;
	}
	
	/**
	 * Save instance state to restore the ListView after onStop() call 
	 */
	public void onSaveInstanceState(Bundle savedState) {
	    super.onSaveInstanceState(savedState);
	    
	    savedState.putSerializable("ItemArray", shoppingListItemArray);
	}
	
	/* TODO NOT USED YET
	 * private void displayNoItemMessage() {
		LinearLayout lView = (LinearLayout)findViewById(R.id.item_browser_linear);

	    TextView tempText = new TextView(this);
	    tempText.setText(getString(R.string.no_item_text));

	    lView.addView(tempText);
	}*/
	
	/**
	 * Displays all the ShoppingListItems in a table-like ListView
	 * @param savedInstanceState
	 */
	private void displayItems(Bundle savedInstanceState) {
		ListView listView = (ListView) findViewById(R.id.item_browser);
		
		//Check if savedInstanceState is set and restore ListView when possible
		if (savedInstanceState != null) {
			@SuppressWarnings("unchecked")
			LinkedList<HashMap<String, String>> shoppingListItemArray = (LinkedList<HashMap<String, String>>) savedInstanceState.getSerializable("ItemArray");
	        if (items != null) {
	            this.shoppingListItemArray = shoppingListItemArray;
	        }
	       
	    } 
		
		else {
	        	this.shoppingListItemArray = itemsToStringArray(); 	
	    }
		
		//Initialize Adapter
		if (ShoppingCart.shoppingCartExists()) {
			
		} else {
			adapter = new SimpleAdapter(this, shoppingListItemArray, R.layout.item_browser_row,
			      new String[] {"name", "price", "amount"}, new int[] {R.id.name_cell, R.id.price_cell, R.id.amount_cell});
		}
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * Creates a LinkedList representation of the ShoppingListItem for the ListView 
	 * @return
	 */
	private LinkedList<HashMap<String, String>> itemsToStringArray() {
		LinkedList<HashMap<String, String>> itemArray = new LinkedList<HashMap<String, String>>();
		for(Entry<ShoppingListItem, Integer> en: items.entrySet()){
			ShoppingListItem item = en.getKey();
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", item.getName());
			map.put("price", "€ " + item.getPrice().toString());
			map.put("amount", en.getValue().toString());
			itemArray.add(map);
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
