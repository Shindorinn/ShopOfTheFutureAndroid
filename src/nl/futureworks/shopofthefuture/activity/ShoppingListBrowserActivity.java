package nl.futureworks.shopofthefuture.activity;

import java.util.ArrayList;

import nl.futureworks.shopofthefuture.android.widget.PullToRefreshListView;
import nl.futureworks.shopofthefuture.android.widget.PullToRefreshListView.OnRefreshListener;
import nl.futureworks.shopofthefuture.domain.ShoppingList;
import nl.futureworks.shopofthefuture.sqlite.DatabaseHandler;
import nl.futureworks.shopofthefuture.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class ShoppingListBrowserActivity extends Activity {
	
	//Custom pull refresh ListView
	private PullToRefreshListView browserListView;
	
	//Mocks shopping list names
	private ArrayList<ShoppingList> shoppingListArray;
	private ArrayAdapter<ShoppingList> adapter;
	
	//Dummycounter
	private int mockCounter = 0;

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
				new GetShoppingListsTask().execute();
			}
		});
		
		//Set OnClickListener
		browserListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ShoppingList selectedList = (ShoppingList) (browserListView.getItemAtPosition(arg2));
				Log.d("Browser", selectedList.toString());
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
	
	//TODO : Replace with nl.futureworks.shopofthefuture.task
	private class GetShoppingListsTask extends AsyncTask<Void, Void, ArrayList<ShoppingList>> {
		
		@Override
		protected ArrayList<ShoppingList> doInBackground(Void... params) {
			//Simulate load of data
			try{
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			shoppingListArray.add(new ShoppingList(mockCounter, 1, "List " + mockCounter, null));
			mockCounter++;
			return shoppingListArray;
		}		
		
		@Override
		protected void onPostExecute(ArrayList<ShoppingList> result) {
			browserListView.onRefreshComplete();
			
			super.onPostExecute(result);
			
		}
	}

}

