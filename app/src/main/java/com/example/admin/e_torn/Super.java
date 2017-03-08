package com.example.admin.e_torn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ausias on 01/03/17.
 */

public class Super {

    private String _id;
    private String name;
    private String address;
    private String phone;
    private String fax;
    private int photo;
    private List<Store> stores;

    Super(String _id, String name, String address, String phone, String fax, int photo, List<Store> stores) {
        this._id = _id;
        this.name = name;
        this.address =address;
        this.phone = phone;
        this.fax = fax;
        this.photo = photo;
        this.stores = stores;
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

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    @Override
    public String toString() {
        return "Super{" +
                "id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", photo=" + photo +
                ", stores=" + stores +
                '}';
    }
}
