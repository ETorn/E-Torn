package com.example.admin.e_torn.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Patango on 09/03/2017.
 */

public class PostUserResponse {
    @SerializedName("userId")
    @Expose
    private String userId;

    private String firebaseId;

    public PostUserResponse(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
