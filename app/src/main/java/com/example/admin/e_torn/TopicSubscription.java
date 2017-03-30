package com.example.admin.e_torn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;


public class TopicSubscription extends BroadcastReceiver implements PushUpdateListener {

    private static final String TAG = "TopicSubscription";

    private static Map<String, Integer> topicMap;

    String topic;
    PushUpdateListener listener;

    Context ctx;

    boolean subscribed;

    public TopicSubscription(Context ctx, String topic) {
        this.topic = topic;
        this.ctx = ctx;
        listener = this;

        subscribed = false;

        if (topicMap == null)
            topicMap = new HashMap<>();
    }

    public void setListener(PushUpdateListener listener) {
        this.listener = listener;
    }

    public String getTopic() {
        return topic;
    }

    public void subscribe() {
        Log.d(TAG, "Subcribing to firebase topic '" + topic + "'");
        FirebaseMessaging.getInstance().subscribeToTopic(topic);

        int subscriptions = topicMap.getOrDefault(topic, 0);

        topicMap.put(topic, subscriptions + 1);

        subscribed = true;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.packageName + "." + topic);
        ctx.registerReceiver(this, intentFilter);
    }

    public void unsubscribe() {
        Log.d(TAG, "Unsubscribing from firebase topic '" + topic + "'");

        int subscriptions = topicMap.get(topic);

        topicMap.put(topic, subscriptions - 1);

        if (topicMap.get(topic) == 0) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        }

        if (subscribed)
            ctx.unregisterReceiver(this);

        subscribed = false;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onRecieve called");
        listener.onPushUpdate((RemoteMessage) intent.getParcelableExtra("remoteMessage"));
    }

    @Override
    public void onPushUpdate(RemoteMessage remoteMessage) {
        Log.w(TAG, "Default listener called. Set your custom listener.");
    }
}
