package nl.futureworks.shopofthefuture.activity;

import nl.futureworks.shopofthefuture.registry.Registry;
import nl.futureworks.shopofthefuture.sqlite.DatabaseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	protected DatabaseHandler db;
	public static Boolean loggedIn = false;
	public static String loginToken = null;
    public static String userName = null;
    public static String userId = null;
	
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
    	return loggedIn;
    }
	
	private void initializeActivity(Bundle savedInstanceState){
		
		//Initialize database
		this.db = DatabaseHandler.getInstance(this);
		

	}

    public void onApiResult(Object obj){
        //implement in subclasses
    }
}
