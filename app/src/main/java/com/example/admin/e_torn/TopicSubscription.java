package com.example.admin.e_torn;

import android.content.Context;
import android.util.Log;

import com.example.admin.e_torn.listeners.PushUpdateListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class TopicSubscription implements PushUpdateListener {

    private static final String TAG = "TopicSubscription";

   // private static Map<String, Integer> topicMap;

    String topic;
    PushUpdateListener listener;

    MqttAndroidClient mqttClient;
    Context ctx;

    public TopicSubscription(Context ctx, MqttAndroidClient mqttClient, String topic) {
        this.topic = topic;
        this.ctx = ctx;
        this.mqttClient = mqttClient;
        listener = this;

        //if (topicMap == null)
          //  topicMap = new HashMap<>();
    }

    public void setListener(PushUpdateListener listener) {
        this.listener = listener;
    }

    public String getTopic() {
        return topic;
    }

    public void subscribe() {
        Log.d(TAG, "Subscribing to mqtt topic '" + topic + "'");
        try {
            mqttClient.subscribe(topic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d(TAG, "Received push notification from topic: " + topic);

                    listener.onPushUpdate(message);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        //int subscriptions = topicMap.getOrDefault(topic, 0);

        //topicMap.put(topic, subscriptions + 1);
    }

    public void unsubscribe() {
        Log.d(TAG, "Unsubscribing from mqtt topic '" + topic + "'");

       // int subscriptions = topicMap.get(topic);

        //topicMap.put(topic, subscriptions - 1);

        //if (topicMap.get(topic) == 0) {
        try {
            mqttClient.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        //}
    }

    @Override
    public void onPushUpdate(MqttMessage message) {
        Log.w(TAG, "Default listener called. Set your custom listener.");
    }
}
