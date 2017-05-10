package com.example.admin.e_torn.models;

/**
 * Created by ausias on 05/04/17.
 */

public class Turn {
    private String userId;
    private String storeId;
    private int turn;
    private int queue;
    private float aproxTime;

    public Turn(String userId, String storeId, int turn, int turnQueue) {
        this.userId = userId;
        this.storeId = storeId;
        this.turn = turn;
        this.queue = turnQueue;
    }

    public float getAproxTime() {
        return aproxTime;
    }

    public void setAproxTime(float aproxTime) {
        this.aproxTime = aproxTime;
    }

    public int getQueue() {
        return queue;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        return "Turn{" +
                "userId='" + userId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", turn=" + turn +
                ", queue=" + queue +
                ", aproxTime=" + aproxTime +
                '}';
    }
}
