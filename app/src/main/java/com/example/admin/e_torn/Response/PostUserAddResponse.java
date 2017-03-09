package com.example.admin.e_torn.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Patango on 09/03/2017.
 */

public class PostUserAddResponse {

    @SerializedName("turn")
    @Expose
    private Integer turn;

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }
}
