package com.seamlabs.BlueRide.network.requests;

import java.util.ArrayList;

public class TeacherDeliverStudentsRequestModel {
    ArrayList<Integer> request_ids = new ArrayList<>();
    ArrayList<Integer> student_ids = new ArrayList<>();

    public ArrayList<Integer> getRequest_ids() {
        return request_ids;
    }

    public void setRequest_ids(ArrayList<Integer> request_ids) {
        this.request_ids = request_ids;
    }

    public ArrayList<Integer> getStudent_ids() {
        return student_ids;
    }

    public void setStudent_ids(ArrayList<Integer> student_ids) {
        this.student_ids = student_ids;
    }
}
