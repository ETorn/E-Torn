package com.example.admin.e_torn.models;

import java.util.List;

/**
 * Created by Patango on 01/03/2017.
 */

/**
 * Model User
 *
 * @author Victor Puigcerver
 */
public class User {

    private String firebaseId;
    private String _id;
    private List<Turn> turns;
    private int notificationTurns;

    /**
     * Constructor buit
     */
    public User() {}

    /**
     * Constructor per crear un User
     * @param firebaseId String id de firebase
     * @param _id String id de mongo
     * @param turns llista de torns de l'usuari
     * @param notificationTurns Nombre de torns d'antel·lacio amb que l'usuari vol ser avisat
     */
    public User(String firebaseId, String _id, List<Turn> turns, int notificationTurns) {
        this.firebaseId = firebaseId;
        this._id = _id;
        this.turns = turns;
        this.notificationTurns = notificationTurns;
    }

    /**
     * Retorna el nombre de torns d'antel·lacio amb que l'usuari vol ser avisat
     * @return Nombre de torns d'antel·lacio amb que l'usuari vol ser avisat
     */
    public int getNotificationTurns() {
        return notificationTurns;
    }

    /**
     * Estableix el nombre de torns d'antel·lacio amb que l'usuari vol ser avisat
     * @param notificationTurns Nombre de torns d'antel·lacio amb que l'usuari vol ser avisat
     */
    public void setNotificationTurns(int notificationTurns) {
        this.notificationTurns = notificationTurns;
    }

    /**
     * Retorna la id de firebase d'aquest usuari
     *
     * @return id de firebase
     */
    public String getFirebaseId() {
        return firebaseId;
    }

    /**
     * Estableix la id de firebase
     *
     * @param firebaseId id de firebase
     */
    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    /**
     * Retorna la id de mongo d'aquest usuari
     *
     * @return id de mongo
     */
    public String get_id() {
        return _id;
    }

    /**
     * Estableix la id de mongo
     * @param _id id de mongo
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     * Retorna la llista de torns de l'user
     *
     * @return Llista de torns
     */
    public List<Turn> getTurns() {
        return turns;
    }

    /**
     * Estableix la llista de torns
     *
     * @param turns Llista de torns
     */
    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    @Override
    public String toString() {
        return "User{" +
                "firebaseId='" + firebaseId + '\'' +
                ", _id='" + _id + '\'' +
                ", turns=" + turns +
                '}';
    }
}
