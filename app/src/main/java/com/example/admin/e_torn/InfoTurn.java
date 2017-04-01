package com.example.admin.e_torn;

/**
 * Created by Patango on 01/04/2017.
 */

class InfoTurn {

    private String userId;
    private int userTurn;

    public InfoTurn(String storeId, int userTurn) {
        this.userId = storeId;
        this.userTurn = userTurn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserTurn() {
        return userTurn;
    }

    public void setUserTurn(int userTurn) {
        this.userTurn = userTurn;
    }
}
