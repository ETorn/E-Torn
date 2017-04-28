package com.example.admin.e_torn.services;

import com.example.admin.e_torn.models.User;
import com.example.admin.e_torn.response.PostUserResponse;

import org.json.JSONObject;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


/**
 * Created by Patango on 09/03/2017.
 */

public interface UserService {
    @POST("/users")
    Call<PostUserResponse> getUserId(@Body PostUserResponse postUserResponse);

    @PUT("/users/{userId}")
    Call<JSONObject> updateUserPref(@Path("userId") String userId, @Body User user );

    @GET("/users/firebase/{firebaseId}")
    Call<User> getExistingUser(@Path("firebaseId") String firebase);
}
