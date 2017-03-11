package com.example.admin.e_torn.listeners;

import com.google.firebase.messaging.RemoteMessage;


public interface PushUpdateListener {
    void onPushUpdate(RemoteMessage remoteMessage);
}
