package com.example.admin.e_torn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.admin.e_torn.Listeners.PushUpdateListener;
import com.google.firebase.messaging.RemoteMessage;

public class StoreInfoActivity extends AppCompatActivity {

    AppCompatActivity self;

    String storeId;
    int storeTurn;
    int usersTurn;

    TopicSubscription storeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        self = this;

        storeId = getIntent().getStringExtra("id");
        storeTurn = getIntent().getIntExtra("storeTurn", 1);
        usersTurn = getIntent().getIntExtra("usersTurn", 1);

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
