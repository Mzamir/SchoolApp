package com.seamlabs.BlueRide.network.response;

import com.seamlabs.BlueRide.network.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ParentPickUpResponseModel extends BaseResponse {
    int picker_id;
    int mentor_id;
    int school_id;
    String updated_at;
    String created_at;
    @SerializedName("id")
    int requestID;
    ArrayList<ParentStudentForASchoolResponse> students = new ArrayList<>();
    MentorResponseModel mentor;

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

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getid() {
        return requestID;
    }

    public void setid(int id) {
        this.requestID = id;
    }

    public ArrayList<ParentStudentForASchoolResponse> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<ParentStudentForASchoolResponse> students) {
        this.students = students;
    }

    public MentorResponseModel getMentor() {
        return mentor;
    }

    public void setMentor(MentorResponseModel mentor) {
        this.mentor = mentor;
    }
}
