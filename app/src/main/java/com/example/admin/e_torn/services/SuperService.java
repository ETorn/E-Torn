package com.example.admin.e_torn.services;

import com.example.admin.e_torn.models.Super;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Patango on 04/03/2017.
 */

public interface SuperService {

    @GET("/supers")
    Call<List<Super>> getSupers (@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("distance") double distance);

    @GET("/supers/{id}")
    Call<Super> getSuperById (@Path("id") String id);
}
