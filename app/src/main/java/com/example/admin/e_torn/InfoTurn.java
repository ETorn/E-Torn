package com.example.admin.e_torn;

/**
 * Created by Patango on 01/04/2017.
 */

class InfoTurn {

    private String storeId;
    private int userTurn;

    public InfoTurn(String storeId, int userTurn) {
        this.storeId = storeId;
        this.userTurn = userTurn;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public int getUserTurn() {
        return userTurn;
    }

    public void setUserTurn(int userTurn) {
        this.userTurn = userTurn;
    }
}
