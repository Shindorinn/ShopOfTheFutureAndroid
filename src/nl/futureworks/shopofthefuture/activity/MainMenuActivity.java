package nl.futureworks.shopofthefuture.activity;

import nl.futureworks.shopofthefuture.R;
import nl.futureworks.shopofthefuture.registry.Registry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends BaseActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.initializeActivity(savedInstanceState);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode){
		}
	}

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

    }
	
	public void browseShoppingLists(View view){
		Intent shoppingListBrowserIntent = new Intent(this, Registry.SHOPPING_LIST_BROWSER_ACTIVITY);
		this.startActivity(shoppingListBrowserIntent);
	}

    public void apiMagics(View view){
        Intent apiIntent = new Intent(this, ApiActivity.class);
        this.startActivity(apiIntent);
    }

    public void logout(View view){
        loggedIn=false;
        loginToken=null;
        super.login();
    }

	private void initializeActivity(Bundle savedInstanceState){
		this.setContentView(R.layout.activity_main_menu);

	}

   

}
