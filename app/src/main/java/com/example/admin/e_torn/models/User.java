package com.example.admin.e_torn.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Patango on 01/03/2017.
 */

public class User {

    private String firebaseId;
    private String _id;
    private List<Turn> turns;
    private int notificationTurns;

    public User() {
    }

    public User(String firebaseId, String _id, List<Turn> turns, int notificationTurns) {
        this.firebaseId = firebaseId;
        this._id = _id;
        this.turns = turns;
        this.notificationTurns = notificationTurns;
    }

    public int getNotificationTurns() {
        return notificationTurns;
    }

    public void setNotificationTurns(int notificationTurns) {
        this.notificationTurns = notificationTurns;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Turn> getTurns() {
        return turns;
    }

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
