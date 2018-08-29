package com.seamlabs.BlueRide.network.response;

import java.util.ArrayList;

public class ErrorResponseModel {

    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> password = new ArrayList<>();
    ArrayList<String> national_id = new ArrayList<>();
    ArrayList<String> phone = new ArrayList<>();

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public ArrayList<String> getPassword() {
        return password;
    }

    public void setPassword(ArrayList<String> password) {
        this.password = password;
    }

    public ArrayList<String> getNational_id() {
        return national_id;
    }

    public void setNational_id(ArrayList<String> national_id) {
        this.national_id = national_id;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }
}
