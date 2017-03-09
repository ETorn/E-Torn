package com.example.admin.e_torn;

/**
 * Created by Patango on 01/03/2017.
 */

public class User {

    private int turn;
    private String _id;

    public User(int turn, String id) {
        this.turn = turn;
        this._id = id;
    }

    public User() {};

    public int getTurn() {
        return turn;
    }

    public String getId() {
        return _id;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setId(String _id) {
        this._id = _id;
    }
}
