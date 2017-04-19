package com.example.admin.e_torn.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.admin.e_torn.Constants;
import com.example.admin.e_torn.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    public MyFirebaseMessagingService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        Log.d(TAG, "PUSH Message recieved");

        Log.d(TAG, "From: " + remoteMessage.getFrom().replace("/topics/", ""));

        String notificationTitle;
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Has notification: true");
            Log.d(TAG, "Notification title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Notification body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle() + ": " + remoteMessage.getNotification().getBody();
        } else {
            Log.d(TAG, "Has notification: false");
            notificationTitle = "Empty";
        }

        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Has data: true");
            Log.d(TAG, "Data: " + remoteMessage.getData());
        } else {
            Log.d(TAG, "Has data: false");

        }

        Intent intent = new Intent();
        intent.putExtra("remoteMessage", remoteMessage);
        intent.setAction(Constants.packageName + "." + remoteMessage.getFrom().replace("/topics/", ""));
        sendBroadcast(intent); //envia el missatge de la notificacio als topicSubscription que estan escoltant en el metode onPushUpdate

        Notification n = new Notification.Builder(this)
                .setContentTitle("New FCM push notification")
                .setContentText("Notification: " + notificationTitle + ", Data: " + remoteMessage.getData())
                .setSmallIcon(R.drawable.capraboicon)
                .build();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, n);
    }
}
