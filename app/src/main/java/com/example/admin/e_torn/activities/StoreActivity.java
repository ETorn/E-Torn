package com.example.admin.e_torn.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.admin.e_torn.Constants;
import com.example.admin.e_torn.ETornApplication;
import com.example.admin.e_torn.R;
import com.example.admin.e_torn.TopicSubscription;
import com.example.admin.e_torn.adapters.StoreAdapter;
import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.example.admin.e_torn.listeners.RecyclerItemClickListener;
import com.example.admin.e_torn.models.Store;
import com.example.admin.e_torn.models.Super;
import com.example.admin.e_torn.services.CaesarService;
import com.example.admin.e_torn.services.RetrofitManager;
import com.example.admin.e_torn.services.SuperService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends BaseActivity {

    // Referencia a la classe global Application
    ETornApplication app;

    boolean activityRestarted;

    private static final String TAG = "StoreActivity";
    private List<Store> stores;
    private RecyclerView recyclerView;
    private Context context;

    private Super superMrkt;

    // Subscripció al topic de les store disponibles
    List<TopicSubscription> storeSubscriptions;
    private StoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_stores);

        activityRestarted = false;

        app = (ETornApplication) getApplication();

        storeSubscriptions = new ArrayList<>();
        this.context = this;
        this.stores = getIntent().getParcelableArrayListExtra("stores");
        this.superMrkt = getIntent().getParcelableExtra("super");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        updateUI();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true); //Per a quan sabem que el tamany del recyclerView no canviara
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                Intent i = new Intent(this, MyPreferencesActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean storeInTurn (Store s) {
        if (app.getUserInfo().get(s.getId()) != null){
            Log.d(TAG, "inTurn " + app.getUserInfo().get(s.getId()).toString());
            return true;
        }
        return false;
    }

    public void updateUI (){

        adapter = new StoreAdapter(stores);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        unsuscribeAllStores();
                        Store store = stores.get(position);
                        Intent intent = new Intent(context, StoreInfoActivity.class);
                        // Pasem a StoreInfoActivity la id necessaria per fer la peticio al servidor
                        intent.putExtra("id", store.getId());
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                })
        );
    }

    public void storeSubscriptionsListener() {
        for( TopicSubscription storeSubscription: storeSubscriptions) {
            storeSubscription.subscribe();
            storeSubscription.setListener(new PushUpdateListener() {
                @Override
                public void onPushUpdate(RemoteMessage remoteMessage) {
                    Log.d(TAG + " From ",remoteMessage.getFrom());
                    Store s = getStoreFromTopic(remoteMessage.getFrom());
                    if (!storeInTurn(s)) {
                        if (remoteMessage.getData().get("storeQueue") != null)
                            s.setQueue(Integer.parseInt(remoteMessage.getData().get("storeQueue")));
                        if (remoteMessage.getData().get("aproxTime") != null) {
                            Log.d(TAG, "aproxTimeStoreActivity: " + Math.round(Float.parseFloat(remoteMessage.getData().get("aproxTime"))));
                            s.setAproxTime(Float.parseFloat(remoteMessage.getData().get("aproxTime")));
                        }
                        if (remoteMessage.getData().get("usersTurn") != null)
                            s.setUsersTurn(Integer.parseInt(remoteMessage.getData().get("usersTurn")));
                    /*else {
                        stores.get(storeIndex).setUsersTurn(app.getUserInfo().get(stores.get(storeIndex).getId()).getTurn());
                    }*/
                        //updateUI();
                    }
                    else {
                        s.setAproxTime(app.getUserInfo().get(s.get_id()).getAproxTime());
                        Log.d(TAG, "usuari ja te torn en la botiga " + s.getName() + " amb temps aprox: " + s.getAproxTime() * app.getUserInfo().get(s.get_id()).getQueue());
                    }

                    if (remoteMessage.getData().get("storeTurn") != null)
                        s.setStoreTurn(Integer.parseInt(remoteMessage.getData().get("storeTurn")));

                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");

        activityRestarted = true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop");

        unsuscribeAllStores();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        final SuperService superService = RetrofitManager.getInstance(Constants.serverURL).create(SuperService.class);
        final Call<Super> call = superService.getSuperById(this.superMrkt.get_id());
        call.enqueue(new Callback<Super>() {
            @Override
            public void onResponse(Call<Super> call, Response<Super> response) {
                Log.d(TAG, "Retrofit 'GetSuperById' response: " + response.body().toString());
                //Actualitzem les dades de les paradas, si no te torn, actualitza el torn disponible
                for (Store s : response.body().getStores()) {

                    Store oldStore = null;
                    for (Store os : stores) {
                        if (os.get_id().equals(s.get_id()))
                            oldStore = os;
                    }

                    if (oldStore == null) {
                        Log.d(TAG, "OLDSTORE IS NULL");
                        return;
                    }


                    oldStore.setStoreTurn(s.getStoreTurn());
                    oldStore.setQueue(s.getQueue());

                    if (!storeInTurn(oldStore)) {
                        oldStore.setUsersTurn(s.getUsersTurn());

                        final Store finalOldStore = oldStore;

                        Log.d(TAG, "Demanant temps aproximat a caesar per store" + oldStore);

                        final CaesarService caesarService = RetrofitManager.getInstance(Constants.caesarURL).create(CaesarService.class);
                        final Call<Float> call1 = caesarService.getStoreAverageTime(oldStore.getId());
                        call1.enqueue(new Callback<Float>() {
                            @Override
                            public void onResponse(Call<Float> call, Response<Float> response) {
                                Log.d(TAG, "Resposta temps aproximat per store " + finalOldStore + ": " + response.body());
                                finalOldStore.setAproxTime(response.body() * finalOldStore.getQueue());

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<Float> call, Throwable t) {
                                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
                            }
                        });

                    } else {
                        Log.d(TAG, "Usuari te torn en la store: " + oldStore.get_id() + " amb temps aproximat: "
                                + app.getUserInfo().get(oldStore.get_id()).getAproxTime());

                        oldStore.setAproxTime(app.getUserInfo().get(oldStore.get_id()).getAproxTime());
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Super> call, Throwable t) {
                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
            }
        });

        storeSubscriptions.clear();
        for (Store s : stores) {
            Log.d("store", s.toString());
            storeSubscriptions.add(app.getTopicSubscriptionFor("store." + s.getId()));
            if (storeInTurn(s)){
                s.setUsersTurn(app.getUserInfo().get(s.getId()).getTurn());
                s.setInTurn(true);
            } else {
                s.setInTurn(false);
            }
        }

        storeSubscriptionsListener();
        Log.d(TAG, "userInfo: " + app.getUserInfo().toString());
    }

    public Store getStoreFromTopic(String topic) {
        String storeId = topic.split("\\.")[1];
        for (Store s : stores) {
            if (s.getId().equals(storeId))
                return s;
        }
        return null;
    }

    public void unsuscribeAllStores () {
        Log.d(TAG, "unsuscribeAllStores");
        Log.d(TAG, "storeSubscriptions: " + storeSubscriptions.toString());

        for( TopicSubscription storeSubscription: storeSubscriptions) {
            Log.d(TAG, "storeSubscription" + storeSubscription.getTopic());
            storeSubscription.unsubscribe();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
        moveTaskToBack(true); // impedim que l'usuari accedeixi a SuperActivity
    }
}
