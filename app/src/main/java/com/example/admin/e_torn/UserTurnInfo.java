package com.example.admin.e_torn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.admin.e_torn.listeners.PushUpdateListener;

import org.eclipse.paho.client.mqttv3.MqttMessage;

//Classe no funcional de moment
public class UserTurnInfo extends AppCompatActivity {

    ETornApplication app;

    AppCompatActivity self;
    TopicSubscription storeSubscription;
    String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_turn_info);

        app = (ETornApplication) getApplication();

        storeId = getIntent().getStringExtra("id");
        storeSubscription = new TopicSubscription(app, app.getMqttClient(), "store." + storeId + "/#");
        storeSubscription.setListener(new PushUpdateListener() {
            @Override
            public void onPushUpdate(MqttMessage remoteMessage) {
                Toast.makeText(self, "PUSH", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        storeSubscription.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storeSubscription.unsubscribe();
    }
}
