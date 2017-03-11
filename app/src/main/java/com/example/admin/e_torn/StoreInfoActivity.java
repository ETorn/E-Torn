package com.example.admin.e_torn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.example.admin.e_torn.response.PostUserAddResponse;
import com.example.admin.e_torn.services.RetrofitManager;
import com.example.admin.e_torn.services.StoreService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreInfoActivity extends AppCompatActivity implements View.OnClickListener{

    AppCompatActivity self;

    private static final String TAG = "StoreInforActivity" ;
    int storeTurn;
    int usersTurn;
    int queue;

    String storeId;
    String userId;
    Integer userTurn;

    TextView actualTurnText;
    TextView disponibleTurnText;
    TextView queueText;
    TextView aproxTimeText;
    Button getTurnBtn;

    Store store;

    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    TopicSubscription storeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        store = new Store();

        //Extreure ID de FIREBASE
        //prefs = getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE);
        //userId = prefs.getString("userId", null);

        //CANVIAR PER ID FIREBASE!!!!
        userId = "58c15c99051e1529b8be52a4";

        self = this;

        storeId = getIntent().getStringExtra("id");

        store.setId(storeId);

        actualTurnText = (TextView) findViewById(R.id.actualTurn);
        disponibleTurnText = (TextView) findViewById(R.id.disponibleTurn);
        queueText = (TextView) findViewById(R.id.queue);
        aproxTimeText = (TextView) findViewById(R.id.aproxTime);
        getTurnBtn = (Button) findViewById(R.id.getTurnBtn);
        getTurnBtn.setOnClickListener(this);

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

        StoreService storeService = RetrofitManager.retrofit.create(StoreService.class);
        final Call<Store> call = storeService.getStoreById(store.getId());
        call.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                Log.d("Response", response.body().toString());

                storeTurn = response.body().getStoreTurn();
                usersTurn = response.body().getUsersTurn();
                store.setStoreTurn(storeTurn);
                store.setUsersTurn(usersTurn);
                queue = store.getReloadedQueue();

                actualTurnText.setText(String.valueOf(storeTurn));
                disponibleTurnText.setText(String.valueOf(usersTurn));
                queueText.setText(String.valueOf(queue));
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        storeSubscription.unsubscribe();


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.getTurnBtn) {
            StoreService storeService = RetrofitManager.retrofit.create(StoreService.class);
            final Call<PostUserAddResponse> call = storeService.addUserToStore(store.getId(), userId);
            call.enqueue(new Callback<PostUserAddResponse>() {
                @Override
                public void onResponse(Call<PostUserAddResponse> call, Response<PostUserAddResponse> response) {
                    Log.d(TAG, "ResponseTurn: " + response.body().getTurn());
                    userTurn = response.body().getTurn();
                    putIdInPref(usersTurn);
                    if(userTurn != null) {
                        Context context = getApplicationContext();
                        Intent intent = new Intent(context, UserTurnInfo.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        finish();
                    }
                    else {
                        //L'usuari ja ha demanat torn
                        Log.d(TAG, "Usuari ja ha demanat torn");
                    }
                }

                @Override
                public void onFailure(Call<PostUserAddResponse> call, Throwable t) {
                    Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
                }
            });
        }
    }

    public void putIdInPref (Integer turn) {
        editor = getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE).edit();
        editor.putInt("userTurn", turn);
        editor.commit();
    }
}
