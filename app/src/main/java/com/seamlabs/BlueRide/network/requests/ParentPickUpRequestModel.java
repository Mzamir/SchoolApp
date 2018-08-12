package com.seamlabs.BlueRide.network.requests;

import java.io.Serializable;
import java.util.ArrayList;

public class ParentPickUpRequestModel implements Serializable{
    int school_id ;
    ArrayList<Integer> student_ids = new ArrayList<>() ;

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public ArrayList<Integer> getStudent_ids() {
        return student_ids;
    }

    public void setStudent_ids(ArrayList<Integer> student_ids) {
        this.student_ids = student_ids;
    }
}
