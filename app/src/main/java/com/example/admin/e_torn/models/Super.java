package com.example.admin.e_torn.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ausias on 01/03/17.
 */

public class Super implements Parcelable{

    private String _id;
    private String city;
    private String address; //super name
    private long distance;
    private String phone;
    private String fax;
    private List<Store> stores;

    public Super(String _id, String name, String address, String phone, String fax, List<Store> stores, Long distance) {
        this._id = _id;
        this.city = name;
        this.address =address;
        this.phone = phone;
        this.fax = fax;
        this.stores = stores;
        this.distance = distance;
    }

    protected Super(Parcel in) {
        _id = in.readString();
        city = in.readString();
        address = in.readString();
        distance = in.readLong();
        phone = in.readString();
        fax = in.readString();
        stores = in.createTypedArrayList(Store.CREATOR);
    }

    public static final Creator<Super> CREATOR = new Creator<Super>() {
        @Override
        public Super createFromParcel(Parcel in) {
            return new Super(in);
        }

        @Override
        public Super[] newArray(int size) {
            return new Super[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeLong(this.distance);
        dest.writeString(this.fax);
    }
}
