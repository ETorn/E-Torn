package com.example.admin.e_torn;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.example.admin.e_torn.models.User;
import com.example.admin.e_torn.response.PostUserResponse;
import com.example.admin.e_torn.services.RetrofitManager;
import com.example.admin.e_torn.services.UserService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ETornApplication extends Application implements PushUpdateListener {

    private static final String TAG = "ETornApplication";

    TopicSubscription allSubscription;

    SharedPreferences sharedPreferences;

    //Map per a identificar en quina store ha demanat torn el usuari
    HashMap<String, InfoTurn> userInfo;

    User user;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "APP STARTED");

        user = new User();

        userInfo = new HashMap<>();

        sharedPreferences = getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE);

        //Efectuem crida a post /users per a obtenir una ID per a l'usuari
        UserService userService = RetrofitManager.retrofit.create(UserService.class);
        PostUserResponse postUserResponse = new PostUserResponse(getFCMToken());
        final Call<PostUserResponse> call = userService.getUserId(postUserResponse);
        call.enqueue(new Callback<PostUserResponse>() {
            @Override
            public void onResponse(Call<PostUserResponse> call, Response<PostUserResponse> response) {
                user.setId(response.body().getUserId());
            }

            @Override
            public void onFailure(Call<PostUserResponse> call, Throwable t) {
                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
            }
        });

        allSubscription = new TopicSubscription(this, "everyone");
        allSubscription.setListener(this);
        allSubscription.subscribe();


    }



    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public String getFCMToken() {
        return getSharedPreferences().getString(Constants.FCE_TOKEN_NAME, null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.d(TAG, "New configuration");
    }

    @Override
    public void onPushUpdate(RemoteMessage remoteMessage) {
        Log.d(TAG, "New push notification for everyone");
    }

    public HashMap<String, InfoTurn> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(HashMap<String, InfoTurn> userInfo) {
        this.userInfo = userInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
