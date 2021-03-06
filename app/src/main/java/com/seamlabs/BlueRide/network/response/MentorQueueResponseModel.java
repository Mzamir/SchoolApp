package com.seamlabs.BlueRide.network.response;

import com.seamlabs.BlueRide.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MentorQueueResponseModel extends BaseResponse {

    int id;
    int picker_id;
    int mentor_id;
    int security_id;
    int school_id;
    String created_at;
    String updated_at;
    String status;
    boolean mentor_can_deliver;

    @SerializedName("students")
    ArrayList<StudentResponseModel> students = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPicker_id() {
        return picker_id;
    }

    public void setPicker_id(int picker_id) {
        this.picker_id = picker_id;
    }

    public int getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(int mentor_id) {
        this.mentor_id = mentor_id;
    }

    public int getSecurity_id() {
        return security_id;
    }

    public void setSecurity_id(int security_id) {
        this.security_id = security_id;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<StudentResponseModel> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<StudentResponseModel> students) {
        this.students = students;
    }

    public boolean isMentor_can_deliver() {
        return mentor_can_deliver;
    }

    public void setMentor_can_deliver(boolean mentor_can_deliver) {
        this.mentor_can_deliver = mentor_can_deliver;
    }
}
