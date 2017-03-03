package com.example.admin.e_torn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ausias on 01/03/17.
 */

public class Super {

    private String name;
    private String address;
    private String phone;
    private String fax;
    private int photo;
    private List<Store> stores;

    Super(String name, String address, String phone, String fax, int photo) {
        this.name = name;
        this.address =address;
        this.phone = phone;
        this.fax = fax;
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getFax() {
        return fax;
    }

    public int getPhoto() {
        return photo;
    }

    public List<Store> getStores() {
        return stores;
    }
}
