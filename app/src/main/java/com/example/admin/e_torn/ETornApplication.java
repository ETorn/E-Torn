package com.example.admin.e_torn;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.example.admin.e_torn.models.Turn;
import com.example.admin.e_torn.models.User;
import com.example.admin.e_torn.response.PostUserResponse;
import com.example.admin.e_torn.services.RetrofitManager;
import com.example.admin.e_torn.services.UserService;
import com.google.firebase.messaging.RemoteMessage;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ETornApplication extends Application implements PushUpdateListener {

    private static final String TAG = "ETornApplication";
    private static final String MQTT_TAG = TAG + ":mqtt";

    MqttAndroidClient mqttAndroidClient;

    TopicSubscription allSubscription;

    SharedPreferences sharedPreferences;

    //Map per a identificar en quina store ha demanat torn el usuari
    HashMap<String, Turn> userInfo;

    User user;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "APP STARTED");

        user = new User();

        userInfo = new HashMap<>();

        sharedPreferences = getSharedPreferences(Constants.PREFERENCES_NAME, MODE_PRIVATE);

        //Efectuem crida a post /users per a obtenir una ID per a l'usuari
        final UserService userService = RetrofitManager.retrofit.create(UserService.class);
        final Call<User> findCall = userService.getExistingUser(getFCMToken());
        findCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "UserResponse: " + response.body().toString());
                //Si l'usuari existeix, asignem la seva id de mongo
                if (response.body().get_id() != null) {
                    Log.d(TAG, "Usuari ja registrat, retornant id de mongodb...");
                    user.set_id(response.body().get_id());
                    if (response.body().getTurns() != null) {
                        user.setTurns(response.body().getTurns());
                        for (Turn turn : response.body().getTurns()) {
                            userInfo.put(turn.getStoreId(), turn); //Omplim el hashMap amb els torns que l'usuari ja havia demanats
                        }
                        Log.d(TAG, "UsersTurns  " + userInfo.toString());
                    }
                 }
                  //Sino existeix, el creem
                else {
                     Log.d(TAG, "Creant un usuari nou...");
                    //Efectuem crida a post /users per a obtenir una ID per a l'usuari
                    PostUserResponse postUserResponse = new PostUserResponse(getFCMToken());
                    final Call<PostUserResponse> callGet = userService.getUserId(postUserResponse);
                    callGet.enqueue(new Callback<PostUserResponse>() {

                        @Override
                        public void onResponse(Call<PostUserResponse> call, Response<PostUserResponse> response) {
                            user.set_id(response.body().getUserId());
                        }

                        @Override
                        public void onFailure(Call<PostUserResponse> call, Throwable t) {
                            Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
                        }
                    });
                }
          }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(Constants.RETROFIT_FAILURE_TAG, t.getMessage());
            }
        });

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), Constants.MQTT_URL, "ID");
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                Log.d(MQTT_TAG, "Connect complete. Is reconnect:" + reconnect);

                subscribeToTopic();
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d(MQTT_TAG, "Connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(MQTT_TAG, "Message arrived. Topic: " + topic + " Payload: " + message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(MQTT_TAG, "Delivery complete");
            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(MQTT_TAG, "Connected sucessfully");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(MQTT_TAG, "Failed to connect");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        allSubscription = new TopicSubscription(this, "everyone");
        allSubscription.setListener(this);
        allSubscription.subscribe();
    }

    public void subscribeToTopic(){
        try {
            mqttAndroidClient.subscribe("etorn/store/#", 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(MQTT_TAG, "Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(MQTT_TAG, "Failed to subscribe");
                }
            });
        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public String getFCMToken() {
        return getSharedPreferences().getString(Constants.FCE_TOKEN_NAME, null);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.d(TAG, "New configuration");
    }

    @Override
    public void onPushUpdate(RemoteMessage remoteMessage) {
        Log.d(TAG, "New push notification for everyone");
    }

    public HashMap<String, Turn> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(HashMap<String, Turn> userInfo) {
        this.userInfo = userInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
