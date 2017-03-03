package com.example.admin.e_torn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.admin.e_torn.Adapters.SuperAdapter;

import java.util.ArrayList;
import java.util.List;


public class SuperActivity extends AppCompatActivity {

    private List<Super> supers;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true); Per a quan sabem que el tamany del recyclerView no canviara
        inicialitzeData();
        initializeAdapter();
    }


    public void inicialitzeData (){
        supers = new ArrayList<>();
        supers.add(new Super("Caprabo1", "Caprabo1 address", "111111", "22222", R.drawable.capraboicon));
        supers.add(new Super("Caprabo3", "Caprabo2 address", "111111", "22222", R.drawable.capraboicon));
        supers.add(new Super("Caprabo4", "Caprabo3 address", "111111", "22222", R.drawable.capraboicon));
        supers.add(new Super("Caprabo5", "Caprabo4 address", "111111", "22222", R.drawable.capraboicon));
        //Afegir supers a la llista retornades de la crida GET supers al servidor
    }

    private void initializeAdapter(){
        SuperAdapter adapter = new SuperAdapter(supers);
        recyclerView.setAdapter(adapter);
    }
}
