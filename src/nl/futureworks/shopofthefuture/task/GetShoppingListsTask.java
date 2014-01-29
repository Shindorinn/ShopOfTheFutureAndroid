package nl.futureworks.shopofthefuture.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import nl.futureworks.shopofthefuture.android.widget.PullToRefreshListView;
import nl.futureworks.shopofthefuture.domain.ShoppingList;
import nl.futureworks.shopofthefuture.domain.ShoppingListItem;
import nl.futureworks.shopofthefuture.sqlite.DatabaseHandler;
import android.content.Context;
import android.os.AsyncTask;

public class GetShoppingListsTask extends AsyncTask<Void, Void, ArrayList<ShoppingList>> {
	
	private PullToRefreshListView browserListView;
	private ArrayList<ShoppingList> shoppingListArray;
	private DatabaseHandler db;
	private boolean useApi;
	
	public GetShoppingListsTask(Context context, PullToRefreshListView browserListView, ArrayList<ShoppingList> shoppingListArray, boolean useApi) {
		this.useApi = useApi;
		this.shoppingListArray = shoppingListArray;
		this.browserListView = browserListView;
		db = DatabaseHandler.getInstance(context);
	}
	
	@Override
	protected ArrayList<ShoppingList> doInBackground(Void...params) {
		if (useApi){
			getFromApi();
		}
		
		//Get latest data from database
		return getFromDatabase();
	}
		
		
	@Override
	protected void onPostExecute(ArrayList<ShoppingList> result) {	
		browserListView.onRefreshComplete();
		super.onPostExecute(result);	
	}
	
	private void getFromApi() {
		//TODO Get new lists from api and insert in database
	}
	
	private ArrayList<ShoppingList> getFromDatabase() {
		List<HashMap<String, String>> shoppingLists = db.sendQuery("shoppinglist", null, null, null, null, null, null, null);
		for (int i=0; i<shoppingLists.size(); i++) {
			HashMap<String, String> listMap = shoppingLists.get(i);
			String listId = shoppingLists.get(i).get("id");
			
			//Get each item
			ConcurrentHashMap<ShoppingListItem, Integer> items = new ConcurrentHashMap<ShoppingListItem, Integer>();
			List<HashMap<String, String>> itemList = db.sendQuery("shoppinglistitem", null, "shoppinglist_id = '" + listId + "'", null, null, null, null, null);
			for (int j=0; j<itemList.size(); j++) {
				HashMap<String, String> itemMap = itemList.get(j);
				HashMap<String, String> itemDataMap = db.sendQuery("item", null, "barcode = '" + itemMap.get("barcode") + "'", null, null, null, null, null).get(j);
				
				items.put(new ShoppingListItem(itemMap.get("barcode"), itemDataMap.get("name"), Double.parseDouble(itemDataMap.get("price"))), Integer.parseInt(itemMap.get("quantity")));
			}
			
			ShoppingList list = new ShoppingList(Integer.parseInt(listMap.get("id")), Integer.parseInt(listMap.get("user_id")), listMap.get("name"), items);
			
			if (!shoppingListArray.contains(list)) {
				shoppingListArray.add(list);
			}
		}
		return shoppingListArray;
	}
}
