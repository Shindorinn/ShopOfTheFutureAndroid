package nl.futureworks.shopofthefuture.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import nl.futureworks.shopofthefuture.R;
import nl.futureworks.shopofthefuture.api.ApiHelper;

public class ApiActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
    }


    public void getShoppingLists(View view)
    {
        ApiHelper.getInstance().getShoppingLists("37");
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