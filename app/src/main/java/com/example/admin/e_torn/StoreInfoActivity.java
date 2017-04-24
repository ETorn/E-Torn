package com.example.admin.e_torn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.example.admin.e_torn.models.Store;
import com.example.admin.e_torn.models.Turn;
import com.example.admin.e_torn.response.PostUserAddResponse;
import com.example.admin.e_torn.services.RetrofitManager;
import com.example.admin.e_torn.services.StoreService;
import com.google.firebase.messaging.RemoteMessage;

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

    // UI
    TextView actualTurn;
    TextView disponibleTurn;
    TextView turnText;
    TextView queueText;
    TextView aproxTime;
    ImageView timeIcon;

    FloatingActionButton getTurnBtn;
    Animation in;
    Animation out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1000);

        out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(1000);

        Log.d(TAG, "onCreate()");

        app = (ETornApplication) getApplication();

        store = new Store();

        userId = app.user.get_id();
        self = this;

        storeId = getIntent().getStringExtra("id");

        store.setId(getIntent().getStringExtra("id"));

        turnText = (TextView) findViewById(R.id.disponibleTurnText);
        actualTurn = (TextView) findViewById(R.id.actualTurn);
        disponibleTurn = (TextView) findViewById(R.id.disponibleTurn);
        queueText = (TextView) findViewById(R.id.queue);
        aproxTime = (TextView) findViewById(R.id.time);
        timeIcon = (ImageView) findViewById(R.id.timeIcon);
        getTurnBtn = (FloatingActionButton) findViewById(R.id.getTurnBtn);
        getTurnBtn.setOnClickListener(this);

        if (inTurn()) {
            getTurnBtn.setVisibility(View.GONE);
            turnText.setText(R.string.your_turn);
        }
        storeSubscription = new TopicSubscription(this, "store." + store.getId());
        storeSubscription.setListener(new PushUpdateListener() {
            @Override
            public void onPushUpdate(RemoteMessage remoteMessage) {
            Log.d(TAG, "push recieved");

            if (remoteMessage.getData().get("storeTurn") != null)
                store.setStoreTurn(Integer.parseInt(remoteMessage.getData().get("storeTurn")));
            if (remoteMessage.getData().get("storeQueue") != null)
                store.setQueue(Integer.parseInt(remoteMessage.getData().get("storeQueue")));
            if (remoteMessage.getData().get("aproxTime") != null)
                store.setAproxTime(Integer.parseInt(remoteMessage.getData().get("aproxTime")));
            // Si ja te un torn demanat, no actualitzarem usersTurn (que es el disponible quan no ha demanat torn i es el torn del usuari quan l'ha demanat)
            if (remoteMessage.getData().get("usersTurn") != null && !inTurn())
                store.setUsersTurn(Integer.parseInt(remoteMessage.getData().get("usersTurn")));
            updateUI();
            }
        });
    }

    public boolean inTurn () {
        if (app.getUserInfo().get(store.getId()) != null){
            Log.d(TAG, "inTurn " + app.getUserInfo().get(store.getId()).toString());
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Subscribim al topic de la store per rebre updates s'estat
        storeSubscription.subscribe();

        // Crida inicial a retrofit per omplir la variable store
        final StoreService storeService = RetrofitManager.getInstance(Constants.serverURL).create(StoreService.class);
        final Call<Store> call = storeService.getStoreById(store.getId());
        call.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                Log.d("Response", response.body().toString());

                store = response.body();

                StoreService caesarStoreService = RetrofitManager.getInstance(Constants.caesarURL).create(StoreService.class);
                Call<Integer> caesarCall = caesarStoreService.getStoreAverageTime(store.getId());
                caesarCall.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.body() != null) {
                            Log.d(TAG, "CaesarResponse: " + response.body());
                            store.setAproxTime(response.body());
                        }
                        updateUI();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();

        // Ja no estem a l'activitat, ens desubscribim
        storeSubscription.unsubscribe();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.getTurnBtn) {
            StoreService storeService = RetrofitManager.getInstance(Constants.serverURL).create(StoreService.class);
            final Call<PostUserAddResponse> call = storeService.addUserToStore(store.getId(), userId);
            call.enqueue(new Callback<PostUserAddResponse>() {
                @Override
                public void onResponse(Call<PostUserAddResponse> call, Response<PostUserAddResponse> response) {
                    Log.d(TAG, "ResponseTurn: " + response.body().getTurn());
                    userTurn = response.body().getTurn();

                    //putUserTurnInPref(userTurn);
                    if(userTurn != null) {
                        app.getUserInfo().put(store.getId(), new Turn(app.getUser().get_id(), store.getId(), userTurn));
                        Log.d(TAG, "UsersTurns  " + app.getUserInfo().toString());

                        turnText.startAnimation(out);
                        getTurnBtn.startAnimation(out);
                        out.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                getTurnBtn.setVisibility(View.GONE);
                                getTurnBtn.setEnabled(false);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                turnText.setText(R.string.your_turn);
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
        if (inTurn())
            disponibleTurn.setText(String.valueOf(app.getUserInfo().get(store.getId()).getTurn()));
        else
            disponibleTurn.setText(String.valueOf(store.getUsersTurn()));
        //queueText.setText(String.valueOf(store.getReloadedQueue()) + " torns");
        queueText.setText(String.format("%s%s", String.valueOf(store.getQueue()), getString(R.string.turns)));
        if (store.getAproxTime() == 0) {
            timeIcon.setVisibility(View.GONE);
            aproxTime.setVisibility(View.GONE);
        }

        else {
            timeIcon.setVisibility(View.VISIBLE);
            aproxTime.setVisibility(View.VISIBLE);
            aproxTime.setText(String.valueOf(store.getAproxTime()) + " " + getString(R.string.minutes));
        }
    }

    /*public void putUserTurnInPref(Integer turn) {
        SharedPreferences.Editor editor = ((ETornApplication) getApplication()).getSharedPreferences().edit();
        editor.putInt("userTurn", turn);
        editor.commit();
    }*/
}
