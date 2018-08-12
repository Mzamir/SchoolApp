package com.seamlabs.BlueRide.network.response;

import java.io.Serializable;

public class StudentPivotResponseModel implements Serializable {
    int user_id ;
    int student_id ;
    String type ;
    int one_time ;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOne_time() {
        return one_time;
    }

    public void setOne_time(int one_time) {
        this.one_time = one_time;
    }
}
