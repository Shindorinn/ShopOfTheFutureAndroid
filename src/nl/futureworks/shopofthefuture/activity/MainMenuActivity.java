package nl.futureworks.shopofthefuture.activity;

import nl.futureworks.shopofthefuture.registry.Registry;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.futureworks.shopofthefuture.R;

public class MainMenuActivity extends Activity {

	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.initializeActivity();
		
		// Create SharedPreferences file
		preferences = this.getSharedPreferences(Registry.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
		
		// Only start userLogin, if the login hasen't occurred yet.
		if(!preferences.getBoolean(Registry.APP_LOGIN, Registry.APP_LOGIN_DEFAULT)){
			login();		
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
		
		case Registry.LOGIN_ACTIVITY_REQUEST_CODE :
			if(resultCode == Registry.LOGIN_ACTIVITY_SUCCESS){
				
			}else if(resultCode == Registry.LOGIN_ACTIVITY_FAILED){
				// TODO: Lock the account
			}else if(resultCode == Registry.RESULT_CANCELED){
				// TODO: Try logging in..  or?
			}
		}
	}
	public void startShopping(View view){
		Intent startShoppingIntent = new Intent(this, Registry.SHOPPING_ACTIVITY);
		this.startActivity(startShoppingIntent); // TODO : StartActivityForResult + requestCode ?
	}
	
	
	public void browseShoppingLists(View view){
		Intent shoppingListBrowserIntent = new Intent(this, Registry.SHOPPING_LIST_BROWSER_ACTIVITY);
		this.startActivity(shoppingListBrowserIntent);
	}

    public void login(){
        Intent loginIntent = new Intent(this, Registry.LOGIN_ACTIVITY);
        //loginIntent.putExtra(Registry.DEFAULT_USERNAME, String()); TODO : If standard login is neccessary.
        this.startActivityForResult(loginIntent, Registry.LOGIN_ACTIVITY_REQUEST_CODE);
    }
	
    
	
	private void initializeActivity(){
		this.setContentView(R.layout.activity_main_menu);
	}

   

}
