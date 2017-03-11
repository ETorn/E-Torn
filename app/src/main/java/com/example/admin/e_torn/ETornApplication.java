package com.example.admin.e_torn;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.google.firebase.messaging.RemoteMessage;


public class ETornApplication extends Application implements PushUpdateListener {

    private static final String TAG = "ETornApplication";
    TopicSubscription allSubscription;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "APP STARTED");

        allSubscription = new TopicSubscription(this, "everyone");
        allSubscription.setListener(this);
        allSubscription.subscribe();
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
}
