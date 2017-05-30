package com.example.admin.e_torn.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Model de super
 *
 * @author Victor Puigcerver
 */
public class Super implements Parcelable{

    private String _id;
    private String city;
    private String address; //super name
    private long distance;
    private String phone;
    private String fax;
    private List<Store> stores;

    /**
     * Constructor de super
     *
     * @param _id id mongo
     * @param name nom super
     * @param address adreça super
     * @param phone telefon super
     * @param fax deprecated
     * @param stores List de stores que te el super
     * @param distance distancia de l'usuari al super
     */
    public Super(String _id, String name, String address, String phone, String fax, List<Store> stores, Long distance) {
        this._id = _id;
        this.city = name;
        this.address =address;
        this.phone = phone;
        this.fax = fax;
        this.stores = stores;
        this.distance = distance;
    }

    /**
     * Constructor de super a partir de un {@link Parcel}
     * @param in eso
     */
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

    /**
     * Retorna la distancia de l'usuari al super
     *
     * @return distancia de l'usuari al super
     */
    public long getDistance() {
        return distance;
    }

    /**
     * Estbleix la distancia
     *
     * @param distance distancia
     */
    public void setDistance(long distance) {
        this.distance = distance;
    }

    /**
     * Retorna la id del super
     * e
     * @return id del super
     */
    public String get_id() {
        return _id;
    }

    /**
     * Estableix la id del super
     *
     * @param _id id del super
     */
    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     * Estableix la ciutat del super
     *
     * @param city ciutat
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Estableix la adrça
     *
     * @param address adreça
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Estableix el telefon del super
     *
     * @param phone telefon
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Estableix el fax del super
     *
     * @deprecated
     * @param fax eso
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * Estableix les stores que te el super
     *
     * @param stores Llista de stores
     */
    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    /**
     * Retorna la ciutat on esta situat el super
     *
     * @return ciutat
     */
    public String getCity() {
        return city;
    }

    /**
     * Retiorna la adreça del super
     *
     * @return adreça
     */
    public String getAddress() {
        return address;
    }

    /**
     * Retorna el telefon del super
     *
     * @return telefon
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Retorna el fax del super
     *
     * @deprecated
     * @return eso
     */
    public String getFax() {
        return fax;
    }

    /**
     * Retorna una llista de les stores que te aquest super
     *
     * @return Llista de stores
     */
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
