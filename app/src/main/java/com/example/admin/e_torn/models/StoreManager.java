package com.example.admin.e_torn.models;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gcqui on 30/05/2017.
 */

class StoreWithoutIdException extends RuntimeException {}

class NullStoreIdException extends RuntimeException {}

public class StoreManager {

    public static final String TAG = "StoreManager";

    static Map<String, Store> storeMap;

    private static StoreManager storeManagerInstance;

    public static StoreManager getInstance() {
        Log.d(TAG, "getInstance");

        if (storeManagerInstance == null)
            storeManagerInstance = new StoreManager();

        return storeManagerInstance;
    }

    private StoreManager() {
        storeMap = new HashMap<>();
    }

    public void putStore(Store s) {
        Log.d(TAG, "putStore( " + s + " )");

        String id = s.getId();

        if (id == null)
            throw new StoreWithoutIdException();

        storeMap.put(id, s);
    }

    public Store getStoreWithId(String id) {
        Log.d(TAG, "getStoreWithId( " + id + " )");

        Store s = storeMap.get(id);

        if (s == null) {
            s = new Store();
            s.set_id(id);
            try {
                putStore(s);
            } catch (StoreWithoutIdException e) {
                throw new NullStoreIdException();
            }
        }

        return s;
    }

    public List<Store> getStores() {
        return (List<Store>) storeMap.values();
    }
}
