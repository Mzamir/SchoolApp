package com.seamlabs.BlueRide.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MentorPusherEventResponseModel {

    @SerializedName("students")
    private ArrayList<StudentResponseModel> students = new ArrayList<>();
    int id;
    String status;
    boolean mentor_can_deliver;

    public ArrayList<StudentResponseModel> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<StudentResponseModel> students) {
        this.students = students;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isMentor_can_deliver() {
        return mentor_can_deliver;
    }

    public void setMentor_can_deliver(boolean mentor_can_deliver) {
        this.mentor_can_deliver = mentor_can_deliver;
    }
}
