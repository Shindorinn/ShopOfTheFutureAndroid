package nl.futureworks.shopofthefuture.activity;

import nl.futureworks.shopofthefuture.registry.Registry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	protected SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.initializeActivity(savedInstanceState);
	}

    @Override
    protected void onResume(){
        super.onResume();
        if(!this.loggedIn()){
            this.login();
        }
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Registry.RESULT_SHUTDOWN_APP){
			this.setResult(Registry.RESULT_SHUTDOWN_APP);
			this.finish();
		}
		
		switch(requestCode){
		
		case Registry.LOGIN_ACTIVITY_REQUEST_CODE :
			if(resultCode == Registry.LOGIN_ACTIVITY_FAILED){
				this.login();
			}else if(resultCode == Registry.RESULT_CANCELLED){
				this.finish();
			}
		}
	}
	
    public void login(){
        Intent loginIntent = new Intent(this, Registry.LOGIN_ACTIVITY);
        //loginIntent.putExtra(Registry.DEFAULT_USERNAME, String()); TODO : If standard login is neccessary.
        this.startActivityForResult(loginIntent, Registry.LOGIN_ACTIVITY_REQUEST_CODE);
    }


    public boolean loggedIn(){
    	return this.preferences.getBoolean(Registry.APP_LOGIN, Registry.APP_LOGIN_DEFAULT);
    }
	
	private void initializeActivity(Bundle savedInstanceState){

        // Create SharedPreferences file
		this.preferences = getSharedPreferences(Registry.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        // Check if this is a resume
        if(savedInstanceState != null){
            Editor editor = this.preferences.edit();

            // Re-initialize the sharedPreferences
            editor.putBoolean(Registry.APP_LOGIN, savedInstanceState.getBoolean(Registry.APP_LOGIN));
            editor.commit();
        }

	}
}
