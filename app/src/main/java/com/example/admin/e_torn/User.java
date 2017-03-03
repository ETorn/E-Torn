package com.example.admin.e_torn;

/**
 * Created by Patango on 01/03/2017.
 */

public class User {

    private int turn;
    private int id;

    public User(int turn, int id) {
        this.turn = turn;
        this.id = id;
    }

    public int getTurn() {
        return turn;
    }

    public int getId() {
        return id;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setId(int id) {
        this.id = id;
    }
}
