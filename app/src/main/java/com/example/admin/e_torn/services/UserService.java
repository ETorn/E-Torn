package com.example.admin.e_torn.services;

import com.example.admin.e_torn.response.PostUserResponse;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * Created by Patango on 09/03/2017.
 */

public interface UserService {
    @POST("/users")
    Call<PostUserResponse> getUserId(@Body PostUserResponse postUserResponse);
}
