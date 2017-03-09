package com.example.admin.e_torn.Services;

import com.example.admin.e_torn.Response.PostUserAddResponse;
import com.example.admin.e_torn.Store;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Patango on 08/03/2017.
 */

public interface StoreService {

    @GET("/stores/{id}")
    Call<Store> getStoreById(@Path("id") String id);

    @POST("/stores/{storeId}/users/{userId}")
    Call<PostUserAddResponse> addUserToStore(@Path("storeId") String storeId, @Path("userId") String userId);
}
