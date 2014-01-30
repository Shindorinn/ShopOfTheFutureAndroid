package nl.futureworks.shopofthefuture.api;

import android.content.Context;
import android.util.Log;
import nl.futureworks.shopofthefuture.activity.BaseActivity;
import nl.futureworks.shopofthefuture.domain.ApiShoppingList;
import nl.futureworks.shopofthefuture.domain.ApiShoppingListItem;
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
    private static AhaApiService aas;
    public static List<ApiShoppingList> shoppingListList = null;

    private ApiHelper() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                //TODO: get API URL from config file?
                .setServer("http://145.37.64.30/phpAPI")
                .build();
        aas = restAdapter.create(AhaApiService.class);
    }

    public static ApiHelper getInstance() {
        if(instance==null) {
            instance=new ApiHelper();
        }
        return instance;
    }

    public List<ApiShoppingList> getShoppingLists(BaseActivity b, String userId) {
        aas.shoppingCarts(userId, new Callback<HashMap<String, List<ApiShoppingList>>>() {

            @Override
            public void failure(RetrofitError error) {
                Log.d("AHAPI", "Epic fail!" + error.toString());
            }

            @Override
            public void success(HashMap<String, List<ApiShoppingList>> shoppingListListMap, Response response) {
                shoppingListList = shoppingListListMap.get("lists");


                for (ApiShoppingList shoppingList : shoppingListList) {
                    Log.d("AHAPI", shoppingList.name + " - " + shoppingList.id);// + " " + contributor.telnr);

                    for(ApiShoppingListItem item : shoppingList.items)
                    {
                        Log.d("AHAPI", " : "+item.barcode+" : "+item.name+" : "+item.price+" : "+item.amount);
                    }
                }
            }

        });
        b.onApiResult(shoppingListList);
        return shoppingListList;
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
