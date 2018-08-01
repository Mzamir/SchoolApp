package com.example.mahmoudsamir.schoolappand.network.response;

import com.example.mahmoudsamir.schoolappand.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MentorQueueResponseModel extends BaseResponse {

    MentorDeliverStudentsResponseModel mentorDeliverStudentsResponseModel;
    @SerializedName("students")
    ArrayList<StudentResponseModel> students = new ArrayList<>();

    public MentorDeliverStudentsResponseModel getMentorDeliverStudentsResponseModel() {
        return mentorDeliverStudentsResponseModel;
    }

    public void setMentorDeliverStudentsResponseModel(MentorDeliverStudentsResponseModel mentorDeliverStudentsResponseModel) {
        this.mentorDeliverStudentsResponseModel = mentorDeliverStudentsResponseModel;
    }

    public ArrayList<StudentResponseModel> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<StudentResponseModel> students) {
        this.students = students;
    }
}
