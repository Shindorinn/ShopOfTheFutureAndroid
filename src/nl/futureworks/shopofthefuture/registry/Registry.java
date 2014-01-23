package nl.futureworks.shopofthefuture.registry;

import android.app.Activity;
import nl.futureworks.shopofthefuture.activity.LoginActivity;
import nl.futureworks.shopofthefuture.activity.ShoppingActivity;
import nl.futureworks.shopofthefuture.activity.ShoppingListBrowserActivity;

public class Registry {

	public static final Class<?> SHOPPING_ACTIVITY = ShoppingActivity.class;
	public static final Class<?> SHOPPING_LIST_BROWSER_ACTIVITY = ShoppingListBrowserActivity.class;
	public static final Class<?> LOGIN_ACTIVITY = LoginActivity.class;
	
	// API Info
	public static final String API_URL = "";
	public static final String CHARSET = "UTF-8";
	
	// API Requests
	public static final String API_LOGIN_REQUEST = "/login";
	public static final String API_RETRIEVE_SHOPPING_LISTS = "/retrieve_shopping_lists";
	public static final String API_DELETE_SHOPPING_LIST = "/delete_shopping_list";
	
	// General App keys
	public static final String APP_LOGIN = "nl.futureworks.shopofthefuture.APP_LOGIN";
	public static final boolean APP_LOGIN_DEFAULT = false;
	public static final String SHARED_PREFERENCES_FILE_NAME = "nl.futureworks.shopofthefuture.sharedPreferences";
	
	// General App Request & Result codes
	public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
	public static final int RESULT_OK = Activity.RESULT_OK;
	
	// Login Activity Request & Result codes
	public static final int LOGIN_ACTIVITY_REQUEST_CODE = 1001;
	public static final int LOGIN_ACTIVITY_FAILED = 1002;
	public static final int LOGIN_ACTIVITY_SUCCESS = 1003;
	
	private Registry(){}
}
