package com.example.admin.e_torn.models;

/**
 * Created by ausias on 05/04/17.
 */

public class Turn {
    private String userId;
    private String storeId;
    private int turn;

    public Turn(String userId, String storeId, int turn) {
        this.userId = userId;
        this.storeId = storeId;
        this.turn = turn;
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
                '}';
    }
}
