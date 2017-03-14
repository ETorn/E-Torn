package com.example.admin.e_torn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.admin.e_torn.adapters.SuperAdapter;
import com.example.admin.e_torn.listeners.RecyclerItemClickListener;
import com.example.admin.e_torn.response.PostUserResponse;
import com.example.admin.e_torn.services.RetrofitManager;
import com.example.admin.e_torn.services.SuperService;
import com.example.admin.e_torn.services.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SuperActivity extends AppCompatActivity {

    private List<Super> supers;
    private RecyclerView recyclerView;
    private Context context;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);

        //Efectuem crida a post /users per a obtenir una ID per a l'usuari
        UserService userService = RetrofitManager.retrofit.create(UserService.class);
        final Call<PostUserResponse> call = userService.getUserId();
        call.enqueue(new Callback<PostUserResponse>() {
            @Override
            public void onResponse(Call<PostUserResponse> call, Response<PostUserResponse> response) {
                userId = response.body().getUserId();
            }

            @Override
            public void onFailure(Call<PostUserResponse> call, Throwable t) {
                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
            }
        });

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
                Log.d("Response", response.body().toString());
                for (Super superM: response.body()) {
                    supers.add(new Super(superM.getId(), superM.getCity(), superM.getAddress(), superM.getPhone(), superM.getFax(), superM.getStores(), superM.getCoords()));
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
                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
            }
        });

    }
}
