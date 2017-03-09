package com.example.admin.e_torn.Services;

import com.example.admin.e_torn.Response.PostUserResponse;
import retrofit2.Call;

import retrofit2.http.POST;


/**
 * Created by Patango on 09/03/2017.
 */

public interface UserService {
    @POST("/users")
    Call<PostUserResponse> getUserId();
}
