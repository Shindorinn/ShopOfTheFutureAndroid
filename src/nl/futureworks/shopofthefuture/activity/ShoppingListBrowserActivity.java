package nl.futureworks.shopofthefuture.activity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import nl.futureworks.shopofthefuture.android.widget.PullToRefreshListView;
import nl.futureworks.shopofthefuture.android.widget.PullToRefreshListView.OnRefreshListener;
import nl.futureworks.shopofthefuture.domain.ShoppingList;
import nl.futureworks.shopofthefuture.registry.Registry;
import nl.futureworks.shopofthefuture.sqlite.DatabaseHandler;
import nl.futureworks.shopofthefuture.task.GetShoppingListsTask;
import nl.futureworks.shopofthefuture.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class ShoppingListBrowserActivity extends BaseActivity {
	
	//Custom pull refresh ListView
	private PullToRefreshListView browserListView;
	
	private ArrayList<ShoppingList> shoppingListArray;
	private ArrayAdapter<ShoppingList> adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list_browser);
		
		//Database Test
		DatabaseHandler db = DatabaseHandler.getInstance(this);
		db.sendQuery("user", null, null, null, null, null, null, null);
		
		initializeListView(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping_list_browser, menu);
		return true;
	}
	
	/**
	 * Initializes the shopping list browser and sets the adapter
	 * @param savedInstanceState, restores the ListView when available
	 */
	@SuppressWarnings("unchecked")
	private void initializeListView(Bundle savedInstanceState){
		browserListView = (PullToRefreshListView) findViewById(R.id.shoppinglistbrowser_listview);
		
		//Check if savedInstanceState is set and restore ListView when possible
		if (savedInstanceState != null) {
			ArrayList<ShoppingList> shoppingLists = (ArrayList<ShoppingList>) savedInstanceState.getSerializable("ShoppingLists");
	        if (shoppingLists != null) {
	            shoppingListArray = shoppingLists;
	        }
	       
	    } else {
	        	shoppingListArray = new ArrayList<ShoppingList>();
	    }
		
		//Initialize Adapter
		adapter = new ArrayAdapter<ShoppingList>(this, android.R.layout.simple_list_item_1, shoppingListArray);
		browserListView.setAdapter(adapter);
				
		//Set OnRefreshListener
		browserListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					shoppingListArray = new GetShoppingListsTask(ShoppingListBrowserActivity.this, browserListView, shoppingListArray).execute().get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
		
		//Set OnClickListener
		browserListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ShoppingList selectedList = (ShoppingList) (browserListView.getItemAtPosition(arg2));
				Intent intent = new Intent(ShoppingListBrowserActivity.this, Registry.ITEM_BROWSER_ACTIVITY);
				intent.putExtra("SelectedList", selectedList);
				startActivity(intent);
			}	
		});
	}
	
	/**
	 * Save instance state to restore the ListView after onStop() call 
	 */
	public void onSaveInstanceState(Bundle savedState) {
	    super.onSaveInstanceState(savedState);
	    
	    savedState.putSerializable("ShoppingLists", shoppingListArray);
	}
}

