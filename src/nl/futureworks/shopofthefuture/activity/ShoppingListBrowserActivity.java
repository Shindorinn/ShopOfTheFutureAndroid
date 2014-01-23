package nl.futureworks.shopofthefuture.activity;

import java.util.ArrayList;

import nl.futureworks.shopofthefuture.android.widget.PullToRefreshListView;
import nl.futureworks.shopofthefuture.android.widget.PullToRefreshListView.OnRefreshListener;

import com.futureworks.shopofthefuture.R;

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
	private ArrayList<String> mockNameArray;
	private ArrayAdapter<String> adapter;
	
	//Dummycounter
	private int mockCounter = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_list_browser);
		
		initializeListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping_list_browser, menu);
		return true;
	}
	
	/**
	 * Initializes the shopping list browser and sets the adapter
	 */
	private void initializeListView(){
		browserListView = (PullToRefreshListView) findViewById(R.id.shoppinglistbrowser_listview);
		
		//Initialize itemArray and Adapter
				mockNameArray = new ArrayList<String>();
				adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mockNameArray);
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
				String selectedItem = (String) (browserListView.getItemAtPosition(arg2));
				Log.d("Browser", selectedItem);
			}
			
		});
	}
	
	private class GetShoppingListsTask extends AsyncTask<Void, Void, ArrayList<String>> {
		
		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			//Simulate load of data
			try{
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mockNameArray.add("List " + mockCounter);
			mockCounter++;
			return mockNameArray;
		}		
		
		@Override
		protected void onPostExecute(ArrayList<String> result) {
			browserListView.onRefreshComplete();
			
			super.onPostExecute(result);
			
		}
	}

}

