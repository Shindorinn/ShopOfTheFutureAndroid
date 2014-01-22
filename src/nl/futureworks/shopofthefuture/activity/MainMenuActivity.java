package nl.futureworks.shopofthefuture.activity;

import nl.futureworks.shopofthefuture.registry.Registry;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.futureworks.shopofthefuture.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.initializeActivity();
	}
	
	public void startShopping(View view){
		Intent startShoppingIntent = new Intent(this, Registry.SHOPPING_ACTIVITY);
		this.startActivity(startShoppingIntent); // TODO : StartActivityForResult + requestCode ?
	}
	
	/*
	public void browseShoppingLists(View view){
		Intent shoppingListBrowserIntent = new Intent(this, Registry.SHOPPING_LIST_BROWSER_ACTIVITY);
		this.startActivity(shoppingListBrowserIntent);
	}
	*/
	
	
	
	
	
	private void initializeActivity(){
		this.setContentView(R.layout.activity_main_menu);
	}

   

}
