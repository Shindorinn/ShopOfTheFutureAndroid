package nl.futureworks.shopofthefuture.api;

import android.util.Log;
import nl.futureworks.shopofthefuture.activity.BaseActivity;
import nl.futureworks.shopofthefuture.domain.ApiShoppingList;
import nl.futureworks.shopofthefuture.domain.ShoppingListItem;
import nl.futureworks.shopofthefuture.domain.User;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.HashMap;
import java.util.List;

public class ApiHelper {
    private static ApiHelper instance = null;
    private AhaApiService aas;
    private ApiHelper() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                //TODO: get API URL from config file?
                .setServer("http://192.168.1.53/phpAPI")
                .build();
        aas = restAdapter.create(AhaApiService.class);
    }

    public static ApiHelper getInstance() {
        if(instance==null) {
            instance=new ApiHelper();
        }
        return instance;
    }

    public void getShoppingLists(String userId) {
        aas.shoppingCarts(userId, new Callback<HashMap<String, List<ApiShoppingList>>>() {

            @Override
            public void failure(RetrofitError error) {
                Log.d("AHAPI", "Epic fail!" + error.toString());
            }

            @Override
            public void success(HashMap<String, List<ApiShoppingList>> shoppingListListMap, Response response) {
                List<ApiShoppingList> contributorList = shoppingListListMap.get("lists");
                for (ApiShoppingList contributor : contributorList) {
                    Log.d("AHAPI", contributor.name + " - " + contributor.id);// + " " + contributor.telnr);

                    for(ShoppingListItem item : contributor.items)
                    {
                        Log.d("AHAPI", " : "+item.getBarcode()+" : "+item.getName()+" : "+item.getPrice());
                    }
                }
            }

        });
    }

    public void getUserInfo(String userId) {
        aas.user(userId, new Callback<User>() {

            @Override
            public void failure(RetrofitError error) {
                Log.d("AHAPI", "Epic fail! " + error.toString());
            }

            @Override
            public void success(User user, Response response) {
                BaseActivity.userName = user.name;
                BaseActivity.userId = user.id;
                Log.d("AHAPI", BaseActivity.userName + "/" + BaseActivity.userId);
            }

        });
    }
}
