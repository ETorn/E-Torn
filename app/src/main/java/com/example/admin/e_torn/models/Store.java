package com.example.admin.e_torn.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ausias on 01/03/17.
 */


/**
 * Model de store
 *
 * @author Victor Puigcerver
 */
public class Store implements Parcelable{
    private String _id;
    private String name;
    private int storeTurn;
    private int usersTurn;
    private int queue;
    private float aproxTime;
    private boolean inTurn;


    /**
     * Constructor buit
     */
    public Store(){};

    /**
     *
     * @param _id String mongo id
     * @param name String nom store
     * @param usersTurn int torn disponible
     * @param storeTurn int torn actual
     */
    public Store(String _id, String name, int usersTurn, int storeTurn) {
        this._id = _id;
        this.usersTurn = usersTurn;
        this.storeTurn = storeTurn;
        this.name = name;
        this.queue = usersTurn - storeTurn;
    }

    /**
     * Constructor per crear una store a partir de un {@link Parcel}
     * @param in Parcel parcel
     */
    public Store (Parcel in) {
        this.name = in.readString();
        this._id = in.readString();
        this.storeTurn = in.readInt();
        this.usersTurn = in.readInt();
        this.queue = in.readInt();
        this.aproxTime = in.readInt();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    /**
     * Retorna true si l'usuari ha demanat torn a aquesta store
     *
     * @return boolean si ha demanat torn o no
     */
    public boolean isInTurn() {
        return inTurn;
    }

    /**
     * Estableix si l'usuari ha demanat torn a aquesta botiga
     * @param inTurn boolean establint el valor
     */
    public void setInTurn(boolean inTurn) {
        this.inTurn = inTurn;
    }

    /**
     * Retorna la cua de la store
     *
     * @return int cua de la store
     */
    public int getQueue() {
        return queue;
    }

    /**
     * Estableix la cua de la store
     *
     * @param queue int cua de la store
     */
    public void setQueue(int queue) {
        this.queue = queue;
    }

    /**
     * Retorna el temps per torn aproximat
     *
     * @return float el temps aproximat per torn
     */
    public float getAproxTime() {
        return aproxTime;
    }

    /**
     * Estableix el temps per torn aproximat
     * @param aproxTime float temps per torn aproximat
     */
    public void setAproxTime(float aproxTime) {
        this.aproxTime = aproxTime;
    }

    public int getReloadedQueue () {
       return this.queue = this.usersTurn - this.storeTurn;
    }


    /**
     * Retorna la id de store
     * @return String id
     */
    public String getId() {
        return _id;
    }

    /**
     * Estableix la id de store
     *
     * @param id String id
     */
    public void setId(String id) {
        this._id = id;
    }

    /**
     * Retorna el nom de la store
     *
     * @return String nom
     */
    public String getName() {
        return name;
    }

    /**
     * Estableix el nom de la store
     *
     * @param name String nom
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna el torn actual a la store
     *
     * @return int torn actual
     */
    public int getStoreTurn() {
        return storeTurn;
    }

    /**
     * Estableix el torn actual a la store
     * @param storeTurn int torn actual
     */
    public void setStoreTurn(int storeTurn) {
        this.storeTurn = storeTurn;
    }

    /**
     * Retorna el seguent torn disponible
     *
     * @return int torn disponible
     */
    public int getUsersTurn() {
        return usersTurn;
    }

    /**
     * Estableix el torn disponible de la store
     *
     * @param usersTurn int torn disponible
     */
    public void setUsersTurn(int usersTurn) {
        this.usersTurn = usersTurn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this._id);
        dest.writeInt(this.storeTurn);
        dest.writeInt(this.usersTurn);
        dest.writeInt(this.queue);
        dest.writeFloat(this.aproxTime);
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    @Override
    public String toString() {
        return "Store{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", storeTurn=" + storeTurn +
                ", usersTurn=" + usersTurn +
                ", queue=" + queue +
                ", aproxTime=" + aproxTime +
                ", inTurn=" + inTurn +
                '}';
    }
}
