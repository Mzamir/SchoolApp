package com.example.mahmoudsamir.schoolappand.network.response;

import com.example.mahmoudsamir.schoolappand.network.BaseResponse;

import java.util.ArrayList;

public class ParentSignInResponse extends BaseResponse {

    int id;
    String name;
    String email;
    String national_id;
    String phone;
    String created_at;
    String updated_at;
    int status;
    String authy_code;
    ArrayList<Roles> roles = new ArrayList<>();

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

    public ArrayList<Roles> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Roles> roles) {
        this.roles = roles;
    }

    class Roles {
        int id;
        String name;
        String created_at;
        String updated_at;
    }


}
