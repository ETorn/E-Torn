package com.example.admin.e_torn.Services;

import com.example.admin.e_torn.Super;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Patango on 04/03/2017.
 */

public interface SuperService {

    @GET("/supers")
    Call<List<Super>> getSupers ();
}
