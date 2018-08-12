package com.seamlabs.BlueRide.network.response;

import java.io.Serializable;

public class HelperPivotResponseModel implements Serializable{
    int parent_id ;
    int helper_id ;
    int status ;

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getHelper_id() {
        return helper_id;
    }

    public void setHelper_id(int helper_id) {
        this.helper_id = helper_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
