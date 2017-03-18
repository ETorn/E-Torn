package com.example.admin.e_torn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.google.firebase.messaging.RemoteMessage;

//Classe no funcional de moment
public class UserTurnInfo extends AppCompatActivity {

    AppCompatActivity self;
    TopicSubscription storeSubscription;
    String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_turn_info);

        storeId = getIntent().getStringExtra("id");
        storeSubscription = new TopicSubscription(this, "store." + storeId);
        storeSubscription.setListener(new PushUpdateListener() {
            @Override
            public void onPushUpdate(RemoteMessage remoteMessage) {
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
