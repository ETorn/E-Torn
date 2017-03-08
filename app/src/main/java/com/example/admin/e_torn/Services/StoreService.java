package com.example.admin.e_torn.Services;

import com.example.admin.e_torn.Store;
import com.example.admin.e_torn.Super;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Patango on 08/03/2017.
 */

public interface StoreService {

    @GET("/stores/{id}")
    Call<Store> getStoreById(@Path("id") String id);
}
