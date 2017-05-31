package com.example.admin.e_torn.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface CaesarService {
    @GET("/averageTime/{id}")
    Call<Float> getStoreAverageTime(@Path("id") String id);
}
