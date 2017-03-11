package com.example.admin.e_torn;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;


public class ETornApplication extends Application {

    private static final String TAG = "ETornApplication";
    TopicSubscription allSubscription;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "APP STARTED");

        allSubscription = new TopicSubscription(this, "everyone");
        allSubscription.subscribe();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.d(TAG, "New configuration");
    }
}
