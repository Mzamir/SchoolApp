package com.seamlabs.BlueRide.network.response;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class HelperResponseModel implements Serializable {
    int id;
    String name;
    String email;
    String national_id;
    String phone;
    String created_at;
    String updated_at;
    int status;
    @Nullable
    String authy_code;

    @SerializedName("pivot")
    HelperPivotResponseModel pivot = new HelperPivotResponseModel();
    ArrayList<ImagesResponseModel> images = new ArrayList<>();
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Nullable
    public String getAuthy_code() {
        return authy_code;
    }

    public void setAuthy_code(@Nullable String authy_code) {
        this.authy_code = authy_code;
    }

    public ArrayList<ImagesResponseModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagesResponseModel> images) {
        this.images = images;
    }

    public HelperPivotResponseModel getPivot() {
        return pivot;
    }

    public void setPivot(HelperPivotResponseModel pivot) {
        this.pivot = pivot;
    }
}