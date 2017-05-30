package com.example.admin.e_torn;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.example.admin.e_torn.models.Store;
import com.example.admin.e_torn.models.Turn;
import com.example.admin.e_torn.response.PostUserAddResponse;
import com.example.admin.e_torn.services.RetrofitManager;
import com.example.admin.e_torn.services.StoreService;
import com.example.admin.e_torn.services.UserService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreInfoActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "StoreInfoActivity";

    // Referencia a aquesta activitat per us a callbacks
    AppCompatActivity self;

    // Referencia a la classe global Application
    ETornApplication app;

    // Model de dades de store
    Store store;

    // Subscripció al topic d'aquesta store
    TopicSubscription storeSubscription;

    TopicSubscription userSubscription;

    // Id de store rebuda per Intent
    String storeId;

    // Id de l'usuari que demana el torn (token fcm)
    String userId;

    //Torn que ha agafat l'usuari
    Integer userTurnNumber;

    Turn userTurn;

    // UI
    TextView actualTurn;
    TextView disponibleTurn;
    TextView turnText;
    TextView queueTextNumber;
    TextView queueText;
    TextView aproxTime;
    ImageView timeIcon;

    FloatingActionButton getTurnBtn;
    Animation in;
    Animation out;
    private NotificationManager nm;

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

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        store = new Store();

        userId = app.user.get_id();
        self = this;

        storeId = getIntent().getStringExtra("id");

        store.setId(getIntent().getStringExtra("id"));

        //userTurn = app.getUserInfo().get(store.getId());

        turnText = (TextView) findViewById(R.id.disponibleTurnText);
        actualTurn = (TextView) findViewById(R.id.actualTurn);
        disponibleTurn = (TextView) findViewById(R.id.disponibleTurn);
        queueTextNumber = (TextView) findViewById(R.id.queue);
        queueText = (TextView) findViewById(R.id.queueText);
        aproxTime = (TextView) findViewById(R.id.time);
        timeIcon = (ImageView) findViewById(R.id.timeIcon);
        getTurnBtn = (FloatingActionButton) findViewById(R.id.getTurnBtn);
        getTurnBtn.setOnClickListener(this);

        if (inTurn()) {
            getTurnBtn.setVisibility(View.GONE);
            turnText.setText(R.string.your_turn);
        }
        storeSubscription = app.getTopicSubscriptionFor("store." + store.getId());
        storeSubscription.setListener(new PushUpdateListener() {
            @Override
            public void onPushUpdate(RemoteMessage remoteMessage) {
                if (inTurn())
                    return;

                Log.d(TAG, "push recieved");
                Log.d(TAG, "Data: " + remoteMessage.getData().toString());


                if (remoteMessage.getData().get("storeTurn") != null)
                    store.setStoreTurn(Integer.parseInt(remoteMessage.getData().get("storeTurn")));
                if (remoteMessage.getData().get("storeQueue") != null)
                    store.setQueue(Integer.parseInt(remoteMessage.getData().get("storeQueue")));
                if (remoteMessage.getData().get("aproxTime") != null) {
                    store.setAproxTime(Math.round(Float.parseFloat(remoteMessage.getData().get("aproxTime"))));
                }

                // Si ja te un torn demanat, no actualitzarem usersTurn (que es el disponible quan no ha demanat torn i es el torn del usuari quan l'ha demanat)
                if (remoteMessage.getData().get("usersTurn") != null)
                    store.setUsersTurn(Integer.parseInt(remoteMessage.getData().get("usersTurn")));
                updateUI();
            }
        });

        userSubscription = app.getTopicSubscriptionFor("store." + store.getId() + ".user." + app.getUser().get_id());
        userSubscription.setListener(new PushUpdateListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPushUpdate(RemoteMessage remoteMessage) {
                Log.d(TAG, "push recieved");
                Log.d(TAG, "FROM: " + remoteMessage.getFrom());
                String storeID = remoteMessage.getFrom().split("\\.")[1];

                if (app.getUserInfo().get(storeID) != null) {
                    if (remoteMessage.getData().get("storeTurn") != null) {
                        Log.d(TAG, "StoreTurn: " + remoteMessage.getData().get("storeTurn"));
                        store.setStoreTurn(Integer.parseInt(remoteMessage.getData().get("storeTurn")));
                        //Es el torn del usuari
                        if (store.getStoreTurn() == app.getUserInfo().get(store.get_id()).getTurn()) {
                            Toast.makeText(self, getString(R.string.is_your_turn), Toast.LENGTH_SHORT).show();
                            app.getUserInfo().remove(store.get_id());
                            queueText.setText(getString(R.string.is_your_turn));
                            queueTextNumber.setVisibility(View.GONE);
                            aproxTime.setVisibility(View.GONE);
                            timeIcon.setVisibility(View.GONE);
                            sendNotify(getString(R.string.notificationTitle), getString(R.string.is_your_turn) + " en la " + store.getName());
                            return;
                            //updateUI();
                            //StoreInfoActivity.super.onBackPressed();
                        }
                    }
                    if (remoteMessage.getData().get("queue") != null) {
                        if (Integer.parseInt(remoteMessage.getData().get("queue")) == 1) {
                            sendNotify(getString(R.string.notificationTitle), getString(R.string.nextInQueue) + " en la " + store.getName());
                        }
                        store.setQueue(Integer.parseInt(remoteMessage.getData().get("queue")));
                        app.getUserInfo().get(store.get_id()).setQueue(Integer.parseInt(remoteMessage.getData().get("queue")));
                    }

                    if (remoteMessage.getData().get("notification") != null) {
                        // if (Integer.parseInt(remoteMessage.getData().get("notification")) == 0) {

                        int userQueue = Integer.parseInt(remoteMessage.getData().get("queue"));

                        int turnsBefore = app.getUser().getNotificationTurns();

                        if (userQueue <= turnsBefore) {
                            sendNotify(getString(R.string.notificationTitle), "Hi ha " + userQueue + " persones davant teu a la " + store.getName());
                        }
                        // }
                    }

                    if (remoteMessage.getData().get("aproxTime") != null) {
                        app.getUserInfo().get(store.get_id()).setAproxTime(Math.round(Float.parseFloat(remoteMessage.getData().get("aproxTime"))));
                        //Log.d(TAG, "User aproxTime received: " + Float.parseFloat(remoteMessage.getData().get("aproxTime")));
                        Log.d(TAG, "User aproxTimeEEEEEEEEEEEEEEEEEEEEE: " + app.getUserInfo().get(store.get_id()).getAproxTime());

                    }
                    updateUI();
                }

            }
        });
    }

    public void sendNotify (String title, String content) {
        Notification n = new Notification.Builder(app)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.capraboicon)
                .build();

        nm.notify(0, n);
    }

    public boolean inTurn () {
        if (app.getUserInfo().get(store.get_id()) != null){
            Log.d(TAG, "inTurn " + app.getUserInfo().get(store.get_id()).toString());
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "UserSubscription active? " + userSubscription.isSubscribed());

        // Subscribim al topic de la store per rebre updates s'estat
        if (!inTurn() && !storeSubscription.isSubscribed())
            storeSubscription.subscribe();

        if (inTurn() && !userSubscription.isSubscribed()) {
            userSubscription.subscribe();
        }

        // Crida inicial a retrofit per omplir la variable store
        final StoreService storeService = RetrofitManager.getInstance(Constants.serverURL).create(StoreService.class);
        final Call<Store> call = storeService.getStoreById(store.getId());
        call.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                Log.d("Response", response.body().toString());

                if (!inTurn()) {
                    store = response.body();
                    store.setAproxTime(store.getAproxTime() * store.getQueue());
                }
                else {
                    store.setStoreTurn(response.body().getStoreTurn());
                    store.setName(response.body().getName());
                    store.setQueue(app.getUserInfo().get(store.get_id()).getQueue());
                }

                updateUI();

                /*StoreService caesarStoreService = RetrofitManager.getInstance(Constants.caesarURL).create(StoreService.class);
                Call<Float> caesarCall = caesarStoreService.getStoreAverageTime(store.getId());
                caesarCall.enqueue(new Callback<Float>() {
                    @Override
                    public void onResponse(Call<Float> call, Response<Float> response) {
                        if (response.body() != null) {
                            Log.d(TAG, "CaesarResponse: " + response.body());
                            store.setAproxTime(response.body());
                        }
                        updateUI();
                    }

                    @Override
                    public void onFailure(Call<Float> call, Throwable t) {
                        Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
                    }
                });*/
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
        if (!inTurn() && storeSubscription.isSubscribed())
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
                    storeSubscription.unsubscribe(); // Ens desubscribim de la store per escoltar al topic del usuari
                    userSubscription.subscribe(); // Escoltem al topic del usuari

                    userTurnNumber = response.body().getTurn();

                    //putUserTurnInPref(userTurnNumber);
                    if(userTurnNumber != null) {
                        //El temps aproximat en el moment de demanar torn es el mateix que el de la store
                        Turn turn = new Turn(app.getUser().get_id(), store.getId(), userTurnNumber, store.getQueue());
                        turn.setAproxTime(store.getAproxTime());

                        app.getUserInfo().put(store.getId(), turn);
                        //userTurn = app.getUserInfo().get(store.get_id());

                        Log.d(TAG, "UsersTurns  " + app.getUserInfo().toString());

                        updateUI();

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

                        //Modifiquem les preferencies del usuari de la BDD
                        UserService userService = RetrofitManager.getInstance(Constants.serverURL).create(UserService.class);
                        Call<JSONObject> userCall = userService.updateUserPref(app.getUser().get_id(), app.getUser());

                        userCall.enqueue(new Callback<JSONObject>() {
                            @Override
                            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                                Log.d(TAG, "UserResponse: " + response.body().toString());
                            }

                            @Override
                            public void onFailure(Call<JSONObject> call, Throwable t) {
                                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
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
        Log.d(TAG, "UpdateUI executed");
        actualTurn.setText(String.valueOf(store.getStoreTurn()));
        if (inTurn()) {
            disponibleTurn.setText(String.valueOf(app.getUserInfo().get(store.get_id()).getTurn()));
            queueTextNumber.setText(String.format("%s%s", String.valueOf(app.getUserInfo().get(store.get_id()).getQueue()), getString(R.string.turns)));
            Log.d(TAG, "UserTurnQueue: " + app.getUserInfo().get(store.get_id()).getQueue());
        }
        else {
            disponibleTurn.setText(String.valueOf(store.getUsersTurn()));
            queueTextNumber.setText(String.format("%s%s", String.valueOf(store.getQueue()), getString(R.string.turns)));
        }
        //queueTextNumber.setText(String.valueOf(store.getReloadedQueue()) + " torns");


        if (!inTurn()) {
            if (Math.round(store.getAproxTime()) < 1)
               setTimeLabelsVisibility(false);

            else {
                setTimeLabelsVisibility(true);
                float aproxTimeAux = store.getAproxTime();
                aproxTime.setText(String.valueOf(Math.round(aproxTimeAux)) + " " + getString(R.string.minutes)); // arrodonim al int mes proper al numero decimal que rebem del servidor
            }
        }

        else {
            if (Math.round(app.getUserInfo().get(store.get_id()).getAproxTime()) < 1)
                setTimeLabelsVisibility(false);
            else {
                setTimeLabelsVisibility(true);
                float aproxTimeAux = app.getUserInfo().get(store.get_id()).getAproxTime();
                aproxTime.setText(String.valueOf(Math.round(aproxTimeAux)) + " " + getString(R.string.minutes)); // arrodonim al int mes proper al numero decimal que rebem del servidor
            }
        }

        /*if ((store.getAproxTime() == 0 && !inTurn()) || (inTurn() && Math.round(app.getUserInfo().get(store.get_id()).getAproxTime()) < 1)) {
            timeIcon.setVisibility(View.GONE);
            aproxTime.setVisibility(View.GONE);
            Log.d(TAG, "StoreAproxTime: " + String.valueOf(Math.round(store.getAproxTime())));
            Log.d(TAG, "UserAproxTimeRound: " + String.valueOf(Math.round(app.getUserInfo().get(store.get_id()).getAproxTime())));
        }

        else {
            timeIcon.setVisibility(View.VISIBLE);
            aproxTime.setVisibility(View.VISIBLE);
            float aproxTimeAux = inTurn() ? app.getUserInfo().get(store.get_id()).getAproxTime() : store.getAproxTime();
            Log.d(TAG, "StoreAproxTime: " + String.valueOf(Math.round(store.getAproxTime())));
            Log.d(TAG, "UserAproxTimeRound: " + String.valueOf(Math.round(app.getUserInfo().get(store.get_id()).getAproxTime())));
            Log.d(TAG, "UserAproxTime: " + String.valueOf(app.getUserInfo().get(store.get_id()).getAproxTime()));
            aproxTime.setText(String.valueOf(Math.round(aproxTimeAux)) + " " + getString(R.string.minutes)); // arrodonim al int mes proper al numero decimal que rebem del servidor
        }*/
    }

    public void setTimeLabelsVisibility (boolean state) {
        if (state) {
            timeIcon.setVisibility(View.VISIBLE);
            aproxTime.setVisibility(View.VISIBLE);
        }
        else {
            timeIcon.setVisibility(View.GONE);
            aproxTime.setVisibility(View.GONE);
        }
    }

    /*public void putUserTurnInPref(Integer turn) {
        SharedPreferences.Editor editor = ((ETornApplication) getApplication()).getSharedPreferences().edit();
        editor.putInt("userTurnNumber", turn);
        editor.commit();
    }*/
}
