package com.example.admin.e_torn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.admin.e_torn.Adapters.StoreAdapter;
import com.example.admin.e_torn.Adapters.SuperAdapter;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private List<Store> stores;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true); Per a quan sabem que el tamany del recyclerView no canviara
        inicialitzeData();
        initializeAdapter();
    }

    public void inicialitzeData (){
        stores = new ArrayList<>();
        stores.add(new Store("Carniceria01", 1, 1, R.drawable.capraboicon));
        stores.add(new Store("Carniceria02", 1, 1, R.drawable.capraboicon));
        stores.add(new Store("Peixateria01", 1, 1, R.drawable.capraboicon));
        stores.add(new Store("Peixateria02", 1, 1, R.drawable.capraboicon));
        //Afegir supers a la llista retornades de la crida GET supers al servidor
    }

    private void initializeAdapter(){
        StoreAdapter adapter = new StoreAdapter(stores);
        recyclerView.setAdapter(adapter);
    }
}
