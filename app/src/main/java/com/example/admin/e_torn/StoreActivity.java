package com.example.admin.e_torn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.admin.e_torn.adapters.StoreAdapter;
import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.example.admin.e_torn.listeners.RecyclerItemClickListener;
import com.example.admin.e_torn.models.Store;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    // Referencia a la classe global Application
    ETornApplication app;

    boolean activityRestarted;

    private static final String TAG = "StoreActivity";
    private List<Store> stores;
    private RecyclerView recyclerView;
    private Context context;

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

    public boolean storeInTurn (int index) {
        if (app.getUserInfo().get(stores.get(index).getId()) != null){
            Log.d(TAG, "inTurn " + app.getUserInfo().get(stores.get(index).getId()).toString());
            return true;
        }
        return false;
    }

    public void updateUI (){
        /*stores = new ArrayList<>();
        stores.add(new Store("Carniceria01", 1, 1, R.drawable.capraboicon));
        stores.add(new Store("Carniceria02", 1, 1, R.drawable.capraboicon));
        stores.add(new Store("Peixateria01", 1, 1, R.drawable.capraboicon));
        stores.add(new Store("Peixateria02", 1, 1, R.drawable.capraboicon));*/

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
                    int storeIndex = getTopicStoreIndex(remoteMessage.getFrom());
                    if (remoteMessage.getData().get("storeTurn") != null)
                        stores.get(storeIndex).setStoreTurn(Integer.parseInt(remoteMessage.getData().get("storeTurn")));
                    if (remoteMessage.getData().get("storeQueue") != null)
                        stores.get(storeIndex).setQueue(Integer.parseInt(remoteMessage.getData().get("storeQueue")));
                    if (!storeInTurn(storeIndex)) {
                        if (remoteMessage.getData().get("usersTurn") != null)
                            stores.get(storeIndex).setUsersTurn(Integer.parseInt(remoteMessage.getData().get("usersTurn")));
                    }
                    /*else {
                        stores.get(storeIndex).setUsersTurn(app.getUserInfo().get(stores.get(storeIndex).getId()).getTurn());
                    }*/
                    //updateUI();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRestarted = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        storeSubscriptions.clear();
        for (int i = 0; i < stores.size(); i++) {
            Log.d("store", stores.get(i).toString());
            storeSubscriptions.add(new TopicSubscription(this, "store." + stores.get(i).getId()));
            if (storeInTurn(i)){
                stores.get(i).setUsersTurn(app.getUserInfo().get(stores.get(i).getId()).getTurn());
                stores.get(i).setInTurn(true);
            }
        }

        adapter.notifyDataSetChanged();

        storeSubscriptionsListener();
    }

    public int getTopicStoreIndex (String topic) {
        String storeId = topic.split("\\.")[1];
        for (int i = 0; i < stores.size(); i++) {
            if (stores.get(i).getId().equals(storeId))
                return i;
        }
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unsuscribeAllStores();
    }

    public void unsuscribeAllStores () {
        Log.d(TAG, storeSubscriptions.toString());
        for( TopicSubscription storeSubscription: storeSubscriptions) {
            Log.d(TAG, "storeSubscription" + storeSubscription.getTopic());
            storeSubscription.unsubscribe();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
        moveTaskToBack(true);
    }
}
