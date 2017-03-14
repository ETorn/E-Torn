package com.example.admin.e_torn;

import java.util.List;

/**
 * Created by ausias on 01/03/17.
 */

public class Super {

    private String _id;
    private String city;
    private String address; //super name
    private List<Double> coords;
    private int distance;
    private String phone;
    private String fax;
    private List<Store> stores;

    Super(String _id, String name, String address, String phone, String fax, List<Store> stores, List<Double> coords) {
        this._id = _id;
        this.city = name;
        this.address =address;
        this.phone = phone;
        this.fax = fax;
        this.stores = stores;
        this.coords = coords;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<Double> getCoords() {
        return coords;
    }

    public void setCoords(List<Double> coords) {
        this.coords = coords;
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
                "id='" + _id + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", stores=" + stores +
                '}';
    }
}
