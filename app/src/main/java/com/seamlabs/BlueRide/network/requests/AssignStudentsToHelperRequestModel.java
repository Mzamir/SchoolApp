package com.seamlabs.BlueRide.network.requests;

import java.util.ArrayList;

public class AssignStudentsToHelperRequestModel {
    private int helper_id ;
    private ArrayList<Integer> student_ids = new ArrayList<>() ;

    private int one_time ;

    public int getHelper_id() {
        return helper_id;
    }

    public void setHelper_id(int helper_id) {
        this.helper_id = helper_id;
    }

    public ArrayList<Integer> getStudent_ids() {
        return student_ids;
    }

    public void setStudent_ids(ArrayList<Integer> student_ids) {
        this.student_ids = student_ids;
    }


    public int getOne_time() {
        return one_time;
    }

    public void setOne_time(int one_time) {
        this.one_time = one_time;
    }
}
