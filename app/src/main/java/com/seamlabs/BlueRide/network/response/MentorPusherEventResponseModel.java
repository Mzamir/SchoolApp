package com.seamlabs.BlueRide.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MentorPusherEventResponseModel {

    @SerializedName("request")
    private MentorPusherDetailsResponseModel mentorPusherDetailsResponseModel;
    @SerializedName("students")
    private ArrayList<StudentResponseModel> students = new ArrayList<>();

    public MentorPusherDetailsResponseModel getMentorPusherDetailsResponseModel() {
        return mentorPusherDetailsResponseModel;
    }

    public void setMentorPusherDetailsResponseModel(MentorPusherDetailsResponseModel mentorPusherDetailsResponseModel) {
        this.mentorPusherDetailsResponseModel = mentorPusherDetailsResponseModel;
    }

    public ArrayList<StudentResponseModel> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<StudentResponseModel> students) {
        this.students = students;
    }
}
