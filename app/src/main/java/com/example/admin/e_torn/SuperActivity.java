package com.example.admin.e_torn;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.example.admin.e_torn.Adapters.SuperAdapter;
import com.example.admin.e_torn.Listeners.RecyclerItemClickListener;
import com.example.admin.e_torn.Services.RetrofitManager;
import com.example.admin.e_torn.Services.SuperService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SuperActivity extends AppCompatActivity {

    private List<Super> supers;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);
        (findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
        this.context = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true); Per a quan sabem que el tamany del recyclerView no canviara
        inicialitzeData();
       // initializeAdapter();
    }


    public void inicialitzeData (){

        supers = new ArrayList<>();


        SuperService superService = RetrofitManager.retrofit.create(SuperService.class);
        final Call<List<Super>> call = superService.getSupers();

        call.enqueue(new Callback<List<Super>>() {
            @Override
            public void onResponse(Call<List<Super>> call, Response<List<Super>> response) {
                (findViewById(R.id.progressBar)).setVisibility(View.GONE);
                for (Super superM: response.body()) {
                    supers.add(new Super(superM.getId(), superM.getName(), superM.getAddress(), superM.getPhone(), superM.getFax(), R.drawable.capraboicon, superM.getStores()));
                /*supers.add(new Super("Caprabo3", "Caprabo2 address", "111111", "22222", R.drawable.capraboicon));
                supers.add(new Super("Caprabo4", "Caprabo3 address", "111111", "22222", R.drawable.capraboicon));
                supers.add(new Super("Caprabo5", "Caprabo4 address", "111111", "22222", R.drawable.capraboicon));*/
                }
                SuperAdapter adapter = new SuperAdapter(context, supers);
                recyclerView.setAdapter(adapter);
                recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener(){

                        @Override
                        public void onItemClick(View view, int position) {
                            List<Store> stores = supers.get(position).getStores();
                            Intent intent = new Intent(context, StoreActivity.class);
                            intent.putParcelableArrayListExtra("stores", (ArrayList<? extends Parcelable>) stores); // Pasem a StoreActivity la array de Stores a carregar
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    })
                );

            }

            @Override
            public void onFailure(Call<List<Super>> call, Throwable t) {
                Log.d("call",call.toString());
                Log.d("ee", t.toString());
            }
        });

    }
}
