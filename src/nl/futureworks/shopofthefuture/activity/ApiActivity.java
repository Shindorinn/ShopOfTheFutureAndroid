package nl.futureworks.shopofthefuture.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import nl.futureworks.shopofthefuture.R;
import nl.futureworks.shopofthefuture.api.ApiHelper;
import nl.futureworks.shopofthefuture.domain.ApiShoppingList;
import nl.futureworks.shopofthefuture.domain.ShoppingList;

import java.util.List;

public class ApiActivity extends BaseActivity {

    List<ApiShoppingList> shoppingListList=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
    }

    @Override
    public void onApiResult(Object obj) {
        if(obj instanceof List)
        {
            List<ApiShoppingList> test = (List<ApiShoppingList>) obj;
            Log.d("AHAPI", "Here is the list list! :D"+test.toString());
        }
    }


    public void getShoppingLists(View view)
    {
        shoppingListList = ApiHelper.getInstance().getShoppingLists(this, "37");
        //Log.d("AHAPI", shoppingListList.toString());
        //do stuff with list of shopping lists
    }

    //TODO: temp, remove when not required anymore
    public void getUserInfo(View view)
    {
        ApiHelper.getInstance().getUserInfo("37");
    }

    public void barcodeToProduct(View view) {
        throw new NullPointerException("Told you not to press that button!");
    }
}