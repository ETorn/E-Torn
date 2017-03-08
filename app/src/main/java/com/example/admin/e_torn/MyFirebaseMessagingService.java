package com.example.admin.e_torn;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

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

        Notification n = new Notification.Builder(this)
                .setContentTitle(notificationTitle)
                .setContentText("Data: " + remoteMessage.getData())
                .setSmallIcon(R.drawable.capraboicon)
                .build();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, n);
    }
}
