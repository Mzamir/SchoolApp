package com.seamlabs.BlueRide.network.requests;

import com.google.gson.annotations.SerializedName;

public class UpdateLocationRequestModel {

    private double lat;
    @SerializedName("long")
    private double longitude;

    public UpdateLocationRequestModel(double lat, double longitude) {
        this.lat = lat;
        this.longitude = longitude;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
