package com.example.admin.e_torn.listeners;

import com.google.firebase.messaging.RemoteMessage;


/**
 * Listener per notificacions push de firebase
 *
 * @author Guillem Cruz
 */
public interface PushUpdateListener {
    /**
     * Metode callback cridat quan es rebi una actualització push de firebase
     * @param remoteMessage RemoteMessage amb les dades de la push notificaction
     */
    void onPushUpdate(RemoteMessage remoteMessage);
}
