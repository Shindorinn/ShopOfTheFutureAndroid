package nl.futureworks.shopofthefuture.api;

import nl.futureworks.shopofthefuture.domain.ApiShoppingList;
import nl.futureworks.shopofthefuture.domain.User;
import retrofit.http.GET;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Query;


public interface AhaApiService {

    @GET("/shoppingCarts")
    public void shoppingCarts(
        @Query("userId") String id,
        Callback<HashMap<String, List<ApiShoppingList>>> cb
    );

    @GET("/user")
    public void user(
        @Query("id") String id,
        Callback<User> cb
    );

}