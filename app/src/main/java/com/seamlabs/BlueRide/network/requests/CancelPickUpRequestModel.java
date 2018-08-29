package com.seamlabs.BlueRide.network.requests;

import com.google.gson.annotations.SerializedName;

public class CancelPickUpRequestModel {

    @SerializedName("request_id")
    int request_id ;

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }
}
