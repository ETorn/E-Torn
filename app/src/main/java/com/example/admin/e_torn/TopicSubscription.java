package com.example.admin.e_torn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.admin.e_torn.listeners.PushUpdateListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Guillem Cruz
 *
 * Representa una subscripció a un topic de firebase
 */
public class TopicSubscription extends BroadcastReceiver implements PushUpdateListener {

    private static final String TAG = "TopicSubscription";

    private static Map<String, Integer> topicMap;

    private String topic;

    private PushUpdateListener listener;

    private Context ctx;

    private boolean subscribed;

    /**
     * Inicialitza la instància
     *
     * @param ctx El contexte on es faràn els subscripcions
     * @param topic Una string que representi un topic de firebase
     */
    public TopicSubscription(Context ctx, String topic) {
        this.topic = topic;
        this.ctx = ctx;
        listener = this;

        subscribed = false;

        if (topicMap == null)
            topicMap = new HashMap<>();
    }

    /**
     * Estableix el callback de les notificacions
     *
     * @param listener Instancia de tipus {@link PushUpdateListener}
     */
    public void setListener(PushUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * Retorna el topic que representa aquesta instància
     *
     * @return String topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Retorna si aquesta instància actualment rebrà notificacions o no
     *
     * @return boolean representant el valor
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    /**
     * Configura firebase i registra un broadcastReciever
     * per ser avisat quan es rep una notificacio de firebase.
     *
     * Incrementa un contador per subscripcio a firebase, d'aquesta forma
     * més d'un {@link TopicSubscription} pot estar subscrit a la vegada almateix topic.
     */
    public void subscribe() {
        Log.d(TAG, "Subcribing to firebase topic '" + topic + "'");

        if (subscribed) {
            Log.d(TAG, "Already Subscribed");
            return;
        }

        FirebaseMessaging.getInstance().subscribeToTopic(topic);

        Integer subscriptions = topicMap.get(topic);

        if (subscriptions == null)
            subscriptions = 0;

        topicMap.put(topic, subscriptions + 1);

        Log.d(TAG, "Current subscriptions to topic '" + topic + "' is " + topicMap.get(topic));

        subscribed = true;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.packageName + "." + topic);
        ctx.registerReceiver(this, intentFilter);
    }

    /**
     * Desregistra el broadcastReciever i decrementa el comptador per firebase topic.
     * Si el comptador rriba a 0, es desubscribeix firebase del topic
     * i no es rebran mes notificacions d'aquets topic.
     *
     * Després d'executar aquest metode, el callback d'aquesta instancia ja no serà cridat
     * a no ser que es cridi subscribe() una altre vegada.
     */
    public void unsubscribe() {
        Log.d(TAG, "Unsubscribing from firebase topic '" + topic + "'");

        int subscriptions = topicMap.get(topic);

        topicMap.put(topic, subscriptions - 1);

        Log.d(TAG, "Current subscriptions to topic '" + topic + "' is " + topicMap.get(topic));

        if (topicMap.get(topic) == 0) {
            Log.d(TAG, "Unsubscrubidubidu de veritat");
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        }

        if (subscribed)
            ctx.unregisterReceiver(this);

        subscribed = false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onRecieve called");
        listener.onPushUpdate((RemoteMessage) intent.getParcelableExtra("remoteMessage"));
    }

    @Override
    public void onPushUpdate(RemoteMessage remoteMessage) {
        Log.w(TAG, "Default listener called. Set your custom listener.");
    }

    @Override
    public String toString() {
        return "TopicSubscription{" +
                "topic='" + topic + '\'' +
                ", subscribed=" + subscribed +
                '}';
    }
}
