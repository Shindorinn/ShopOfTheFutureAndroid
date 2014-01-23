package nl.futureworks.shopofthefuture.activity;

import nl.futureworks.shopofthefuture.registry.Registry;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

import com.futureworks.shopofthefuture.R;

public class MainMenuActivity extends Activity {

	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		initializeActivity();
		
		// Check if this is a resume
		if(savedInstanceState != null){
			Editor editor = preferences.edit();
			
			// Re-initialize the sharedPreferences
			editor.putBoolean(Registry.APP_LOGIN, savedInstanceState.getBoolean(Registry.APP_LOGIN));
			editor.commit();
		}
		
		// Create SharedPreferences file
		preferences = getSharedPreferences(Registry.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
	}
    @Override
    protected void onResume(){
        super.onResume();
        if(!loggedIn()){
            login();
        }
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
		
		case Registry.LOGIN_ACTIVITY_REQUEST_CODE :
			if(resultCode == Registry.LOGIN_ACTIVITY_FAILED){
				login();
			}else if(resultCode == Registry.RESULT_CANCELLED){
				finish();
			}
		}
	}

    public boolean loggedIn()
    {
     return preferences.getBoolean(Registry.APP_LOGIN, Registry.APP_LOGIN_DEFAULT);
    }

	public void startShopping(View view){
		Intent startShoppingIntent = new Intent(this, Registry.SHOPPING_ACTIVITY);
		startActivity(startShoppingIntent); // TODO : StartActivityForResult + requestCode ?
	}
	
	
	public void browseShoppingLists(View view){
		Intent shoppingListBrowserIntent = new Intent(this, Registry.SHOPPING_LIST_BROWSER_ACTIVITY);
		startActivity(shoppingListBrowserIntent);
	}

    public void logout(View view){
        Editor editor = preferences.edit();
        editor.putString(Registry.LOGIN_TOKEN, null);
        editor.putBoolean(Registry.APP_LOGIN, false);
        editor.commit();
        login();
    }

    public void login(){
        Intent loginIntent = new Intent(this, Registry.LOGIN_ACTIVITY);
        //loginIntent.putExtra(Registry.DEFAULT_USERNAME, String()); TODO : If standard login is neccessary.
        startActivityForResult(loginIntent, Registry.LOGIN_ACTIVITY_REQUEST_CODE);
    }

	private void initializeActivity(){
		setContentView(R.layout.activity_main_menu);
	}

   

}
