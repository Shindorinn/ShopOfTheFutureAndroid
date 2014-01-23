package nl.futureworks.shopofthefuture.task;

import java.util.ArrayList;

import nl.futureworks.shopofthefuture.android.widget.PullToRefreshListView;
import android.os.AsyncTask;

public class GetShoppingListsTask extends AsyncTask<PullToRefreshListView, Void, ArrayList<String>> {
	
	PullToRefreshListView browserListView;
	
	@Override
	protected ArrayList<String> doInBackground(PullToRefreshListView... params) {
		//Simulate load of data
		this.browserListView = params[0];
		
		try{
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return null;
	}		
	
	@Override
	protected void onPostExecute(ArrayList<String> result) {
		browserListView.onRefreshComplete();
		
		super.onPostExecute(result);
		
	}
}
