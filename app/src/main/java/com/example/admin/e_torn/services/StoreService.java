package com.example.admin.e_torn.services;

import com.example.admin.e_torn.models.Store;
import com.example.admin.e_torn.response.PostUserAddResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Patango on 08/03/2017.
 */

public interface StoreService {

    @GET("/stores/{id}")
    Call<Store> getStoreById(@Path("id") String id);

    @GET("/averageTime/{id}")
    Call<Integer> getStoreAverageTime(@Path("id") String id);

    @PUT("/stores/{storeId}/users/{userId}")
    Call<PostUserAddResponse> addUserToStore(@Path("storeId") String storeId, @Path("userId") String userId);
}
