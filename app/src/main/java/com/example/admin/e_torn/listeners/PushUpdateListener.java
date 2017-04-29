package com.example.admin.e_torn.listeners;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface PushUpdateListener {
    void onPushUpdate(MqttMessage remoteMessage);
}
