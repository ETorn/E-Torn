package com.example.admin.e_torn.models;

/**
 * Created by Patango on 01/03/2017.
 */

public class User {

    private Integer turn;
    private String _id;

    public User(Integer turn, String id) {
        this.turn = turn;
        this._id = id;
    }

    public User() {};

    public Integer getTurn() {
        return turn;
    }

    public String getId() {
        return _id;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public void setId(String _id) {
        this._id = _id;
    }
}
