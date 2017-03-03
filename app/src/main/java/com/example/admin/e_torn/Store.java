package com.example.admin.e_torn;

import java.util.List;

/**
 * Created by ausias on 01/03/17.
 */

public class Store {

    private String name;
    private List<User> users;
    private int storeTurn;
    private int usersTurn;
    private int photo;

    public Store(String name, int usersTurn, int storeTurn, int photo) {
        this.usersTurn = usersTurn;
        this.storeTurn = storeTurn;
        this.name = name;
        this.photo = photo;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStoreTurn() {
        return storeTurn;
    }

    public void setStoreTurn(int storeTurn) {
        this.storeTurn = storeTurn;
    }

    public int getUsersTurn() {
        return usersTurn;
    }

    public void setUsersTurn(int usersTurn) {
        this.usersTurn = usersTurn;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
