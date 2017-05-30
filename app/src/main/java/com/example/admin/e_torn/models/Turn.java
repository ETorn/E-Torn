package com.example.admin.e_torn.models;

/**
 * Model de torn
 *
 * @author Victor Puigcerver
 */
public class Turn {
    private String userId;
    private String storeId;
    private int turn;
    private int queue;
    private float aproxTime;

    /**
     * Constructor de torn
     *
     * @param userId id de usuari
     * @param storeId id store
     * @param turn numero de torn
     * @param turnQueue numero de torns davant d'aquest
     */
    public Turn(String userId, String storeId, int turn, int turnQueue) {
        this.userId = userId;
        this.storeId = storeId;
        this.turn = turn;
        this.queue = turnQueue;
    }

    /**
     * Retorna el temps aproximat per que arribi aquest torn
     *
     * @return temps aproximat
     */
    public float getAproxTime() {
        return aproxTime;
    }

    /**
     * Estableix el temps aproximat per que arribi aquest torn
     *
     * @param aproxTime temps aproximat
     */
    public void setAproxTime(float aproxTime) {
        this.aproxTime = aproxTime;
    }

    /**
     * Retorna el nombre de persones d'avant d'aquest torn
     *
     * @return nombre de persones davant
     */
    public int getQueue() {
        return queue;
    }

    /**
     * Estableix la cua davant d'aquest torn
     *
     * @param queue nombre de persones davant d'aquest torn
     */
    public void setQueue(int queue) {
        this.queue = queue;
    }

    /**
     * Retorna la id de usuari que ha demanat el torn
     *
     * @return id user
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Estableix la id de usuari que ha demanat aquest torn
     *
     * @param userId user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Retorna la id de store a la que aquest torn pertany
     *
     * @return id de store
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * Estableix la id de store a la que aquest torn pertany
     *
     * @param storeId id store
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    /**
     * Retorna el torn
     *
     * @return numero de torn
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Estableix el numero de torn
     *
     * @param turn numero de torn
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }

    @Override
    public String toString() {
        return "Turn{" +
                "userId='" + userId + '\'' +
                ", storeId='" + storeId + '\'' +
                ", turn=" + turn +
                ", queue=" + queue +
                ", aproxTime=" + aproxTime +
                '}';
    }
}
