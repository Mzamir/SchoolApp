package com.seamlabs.BlueRide.network.response;

import com.google.gson.annotations.SerializedName;
import com.seamlabs.BlueRide.network.requests.UpdateLocationRequestModel;

public class UpdateLocationResponseModel {

    @SerializedName("location")
    UpdateLocationRequestModel locationRequestModel;

    public UpdateLocationRequestModel getLocationRequestModel() {
        return locationRequestModel;
    }

    public void setLocationRequestModel(UpdateLocationRequestModel locationRequestModel) {
        this.locationRequestModel = locationRequestModel;
    }
}
