package com.example.admin.e_torn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;


import com.example.admin.e_torn.Adapters.SuperAdapter;
import com.example.admin.e_torn.Services.SuperService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


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
       // initializeAdapter();
    }


    public void inicialitzeData (){

        supers = new ArrayList<>();

        SuperService superService = SuperService.retrofit.create(SuperService.class);
        final Call<List<Super>> call = superService.getSupers();
        call.enqueue(new Callback<List<Super>>() {
            @Override
            public void onResponse(Call<List<Super>> call, Response<List<Super>> response) {
                for (Super superM: response.body()) {
                    supers.add(new Super(superM.getName(), superM.getAddress(), superM.getPhone(), superM.getFax(), R.drawable.capraboicon));
                    /*supers.add(new Super("Caprabo3", "Caprabo2 address", "111111", "22222", R.drawable.capraboicon));
                    supers.add(new Super("Caprabo4", "Caprabo3 address", "111111", "22222", R.drawable.capraboicon));
                    supers.add(new Super("Caprabo5", "Caprabo4 address", "111111", "22222", R.drawable.capraboicon));*/
                    //Afegir supers a la llista retornades de la crida GET supers al servidor
                }
                SuperAdapter adapter = new SuperAdapter(supers);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Super>> call, Throwable t) {
                Log.d("call",call.toString());
                Log.d("ee", t.toString());
            }
        });

    }

    private void initializeAdapter(){
        Log.d("ee", "ee");
        SuperAdapter adapter = new SuperAdapter(supers);
        recyclerView.setAdapter(adapter);
    }
}
