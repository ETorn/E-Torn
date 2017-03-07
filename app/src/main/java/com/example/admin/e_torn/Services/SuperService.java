package com.example.admin.e_torn.Services;

import com.example.admin.e_torn.Constants;
import com.example.admin.e_torn.Super;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Patango on 04/03/2017.
 */

public interface SuperService {

    @GET("/supers")
    Call<List<Super>> getSupers ();
}
