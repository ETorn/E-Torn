package com.example.admin.e_torn;

import java.util.List;

/**
 * Created by ausias on 01/03/17.
 */

public class Super {

    private String _id;
    private String city;
    private String address; //super name
    private long distance;
    private String phone;
    private String fax;
    private List<Store> stores;

    Super(String _id, String name, String address, String phone, String fax, List<Store> stores, Long distance) {
        this._id = _id;
        this.city = name;
        this.address =address;
        this.phone = phone;
        this.fax = fax;
        this.stores = stores;
        this.distance = distance;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setCity(String city) {
        this.city = city;
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

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public String getCity() {
        return city;
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
                "_id='" + _id + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", distance=" + distance +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", stores=" + stores +
                '}';
    }
}
