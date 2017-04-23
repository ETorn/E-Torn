package com.example.admin.e_torn.services;

import com.example.admin.e_torn.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Patango on 07/03/2017.
 */

public class RetrofitManager {
    public static Retrofit getInstance(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    /*public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.serverURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();*/
}
