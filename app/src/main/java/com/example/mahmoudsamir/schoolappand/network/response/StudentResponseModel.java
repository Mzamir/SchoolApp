package com.example.mahmoudsamir.schoolappand.network.response;

import android.support.annotation.Nullable;

public class StudentResponseModel {

    int id;
    String name;
    String national_id;
    int school_id;
    @Nullable
    int class_id;
    String created_at;
    String updated_at;

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

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    @Nullable
    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(@Nullable int class_id) {
        this.class_id = class_id;
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
}
