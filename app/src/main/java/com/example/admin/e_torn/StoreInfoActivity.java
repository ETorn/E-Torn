package com.example.admin.e_torn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.admin.e_torn.Services.RetrofitManager;
import com.example.admin.e_torn.Services.StoreService;
import com.example.admin.e_torn.Services.SuperService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreInfoActivity extends AppCompatActivity {

    String storeId;
    int storeTurn;
    int usersTurn;
    int queue;

    TextView actualTurnText;
    TextView disponibleTurnText;
    TextView queueText;
    TextView aproxTimeText;
    Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        store = new Store();
        actualTurnText = (TextView) findViewById(R.id.actualTurn);
        disponibleTurnText = (TextView) findViewById(R.id.disponibleTurn);
        queueText = (TextView) findViewById(R.id.queue);
        aproxTimeText = (TextView) findViewById(R.id.aproxTime);

        storeId = getIntent().getStringExtra("id");
        store.setId(storeId);

        StoreService storeService = RetrofitManager.retrofit.create(StoreService.class);
        final Call<Store> call = storeService.getStoreById(storeId);
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


}
