package com.seamlabs.BlueRide.network.requests;

public class ParentSignInRequest {
    String national_id;
    String password;

    public ParentSignInRequest(String national_id, String password) {
        this.national_id = national_id;
        this.password = password;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
