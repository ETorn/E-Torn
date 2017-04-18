package com.example.admin.e_torn.services;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.admin.e_torn.Constants;
import com.example.admin.e_torn.ETornApplication;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "FID Service";

    public MyFirebaseInstanceIdService() {
    }

    //Es crida només la primera vegada que s'inicia la app
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        ETornApplication app = (ETornApplication) getApplication();
        SharedPreferences.Editor editor = app.getSharedPreferences().edit();
        editor.putString(Constants.FCE_TOKEN_NAME, refreshedToken);
        editor.commit();

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
}
