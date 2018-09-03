package com.seamlabs.BlueRide.network.response;


import java.util.ArrayList;

public class ParentStudentForASchoolResponse {
    int id;
    String name;
    String national_id;
    int school_id;
    int class_id;
    String created_at;
    String updated_at;
    String class_name;
    String grade_name;
    boolean in_request ;
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

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
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

    public ArrayList<ImagesResponseModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagesResponseModel> images) {
        this.images = images;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }

    public boolean isIn_request() {
        return in_request;
    }

    public void setIn_request(boolean in_request) {
        this.in_request = in_request;
    }
}
