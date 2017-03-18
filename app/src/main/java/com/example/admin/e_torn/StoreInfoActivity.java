package com.example.admin.e_torn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.example.admin.e_torn.response.PostUserAddResponse;
import com.example.admin.e_torn.services.RetrofitManager;
import com.example.admin.e_torn.services.StoreService;
import com.google.firebase.messaging.RemoteMessage;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "StoreInfoActivity";

    // Referencia a aquesta activitat per us a callbacks
    AppCompatActivity self;

    // Referencia a la classe global Application
    ETornApplication app;

    // Model de dades de store
    Store store;

    // Subscripció al topic d'aquesta store
    TopicSubscription storeSubscription;

    // Id de store rebuda per Intent
    String storeId;

    // Id de l'usuari que demana el torn (token fcm)
    String userId;

    //Torn que ha agafat l'usuari
    Integer userTurn;

    boolean inTurn;

    // UI
    TextView actualTurn;
    TextView disponibleTurn;
    TextView turnText;
    TextView queueText;
    TextView aproxTime;
    FloatingActionButton getTurnBtn;
    Animation in;
    Animation out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(2000);

        out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(2000);

        Log.d(TAG, "onCreate()");

        app = (ETornApplication) getApplication();

        store = new Store();

        userId = app.user.getId();
        self = this;

        storeId = getIntent().getStringExtra("id");

        store.setId(storeId);

        turnText = (TextView) findViewById(R.id.disponibleTurnText);
        actualTurn = (TextView) findViewById(R.id.actualTurn);
        disponibleTurn = (TextView) findViewById(R.id.disponibleTurn);
        queueText = (TextView) findViewById(R.id.queue);
        aproxTime = (TextView) findViewById(R.id.time);
        getTurnBtn = (FloatingActionButton) findViewById(R.id.getTurnBtn);
        getTurnBtn.setOnClickListener(this);

        storeSubscription = new TopicSubscription(this, "store." + storeId);
        storeSubscription.setListener(new PushUpdateListener() {
            @Override
            public void onPushUpdate(RemoteMessage remoteMessage) {
                Log.d(TAG, "push recieved");

                if (remoteMessage.getData().get("storeTurn") != null)
                    store.setStoreTurn(Integer.parseInt(remoteMessage.getData().get("storeTurn")));
                if (remoteMessage.getData().get("storeQueue") != null)
                    store.setQueue(Integer.parseInt(remoteMessage.getData().get("storeQueue")));
                if (remoteMessage.getData().get("usersTurn") != null)
                    store.setUsersTurn(Integer.parseInt(remoteMessage.getData().get("usersTurn")));
                updateUI();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Subscribim al topic de la store per rebre updates s'estat
        storeSubscription.subscribe();

        // Crida inicial a retrofit per omplir la variable store
        StoreService storeService = RetrofitManager.retrofit.create(StoreService.class);
        final Call<Store> call = storeService.getStoreById(store.getId());
        call.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                Log.d("Response", response.body().toString());

                store = response.body();

                updateUI();
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

        // Ja no estem a l'activitat, ens desubscribim
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
                    Log.d(TAG, "ResponseTurn: " + response.body().toString());

                    //putUserTurnInPref(userTurn);
                    if(userTurn == null) {
                        // El torn que demana el usuari es el actual disponible de la store
                        // Ja que el torn que retorna retrofit es el seguent disponible
                        userTurn = store.getUsersTurn();
                        inTurn = true;

                        turnText.startAnimation(out);

                        out.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                turnText.setText("EL TEU TORN");
                                turnText.startAnimation(in);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        /*Context context = getApplicationContext();
                        Intent intent = new Intent(context, UserTurnInfo.class);
                        intent.putExtra("id", storeId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        finish();*/
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

    private void updateUI() {
        actualTurn.setText(String.valueOf(store.getStoreTurn()));
        disponibleTurn.setText(String.valueOf(store.getUsersTurn()));
        //queueText.setText(String.valueOf(store.getReloadedQueue()) + " torns");
        queueText.setText(String.valueOf(store.getQueue()) + " torns");
    }

    /*public void putUserTurnInPref(Integer turn) {
        SharedPreferences.Editor editor = ((ETornApplication) getApplication()).getSharedPreferences().edit();
        editor.putInt("userTurn", turn);
        editor.commit();
    }*/
}
