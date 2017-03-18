package com.example.admin.e_torn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.admin.e_torn.adapters.StoreAdapter;
import com.example.admin.e_torn.listeners.RecyclerItemClickListener;
import com.example.admin.e_torn.models.Store;

import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private List<Store> stores;
    private RecyclerView recyclerView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);
        this.context = getApplicationContext();
        this.stores = getIntent().getParcelableArrayListExtra("stores");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        inicialitzeData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true); //Per a quan sabem que el tamany del recyclerView no canviara
    }

    public void inicialitzeData (){
        /*stores = new ArrayList<>();
        stores.add(new Store("Carniceria01", 1, 1, R.drawable.capraboicon));
        stores.add(new Store("Carniceria02", 1, 1, R.drawable.capraboicon));
        stores.add(new Store("Peixateria01", 1, 1, R.drawable.capraboicon));
        stores.add(new Store("Peixateria02", 1, 1, R.drawable.capraboicon));*/
        for (Store store: stores) {
            Log.d("store", store.toString());
        }

        StoreAdapter adapter = new StoreAdapter(stores);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Store store = stores.get(position);
                        Intent intent = new Intent(context, StoreInfoActivity.class);
                        // Pasem a StoreInfoActivity la id necessaria per fer la peticio al servidor
                        intent.putExtra("id", store.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                })
        );
    }
}
