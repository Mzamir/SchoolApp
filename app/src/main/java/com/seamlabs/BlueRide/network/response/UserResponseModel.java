package com.seamlabs.BlueRide.network.response;

import com.google.gson.annotations.SerializedName;
import com.seamlabs.BlueRide.network.BaseResponse;

import java.util.ArrayList;

public class UserResponseModel extends BaseResponse {
    private int id;
    private String name;
    private String email;
    private String national_id;
    private String phone;
    private String created_at;
    private String updated_at;
    private int status;
    private String authy_code;
    private String token;
    int is_verified;
    private String address;
    private double lat;
    @SerializedName("long")
    private double lon;
    int unreadNotifications;
    String login_as;
    private ArrayList<ImagesResponseModel> images = new ArrayList<>();
    private ArrayList<RolesResponseModel> roles = new ArrayList<>();
    private ArrayList<SchoolsResponse> schools = new ArrayList<>();

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

    public String getAuthy_code() {
        return authy_code;
    }

    public void setAuthy_code(String authy_code) {
        this.authy_code = authy_code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<RolesResponseModel> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<RolesResponseModel> roles) {
        this.roles = roles;
    }

    public ArrayList<SchoolsResponse> getSchools() {
        return schools;
    }

    public void setSchools(ArrayList<SchoolsResponse> schools) {
        this.schools = schools;
    }

    public ArrayList<ImagesResponseModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagesResponseModel> images) {
        this.images = images;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getUnreadNotifications() {
        return unreadNotifications;
    }

    public void setUnreadNotifications(int unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
    }

    public int getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }

    public String getLogin_as() {
        return login_as;
    }

    public void setLogin_as(String login_as) {
        this.login_as = login_as;
    }
}
