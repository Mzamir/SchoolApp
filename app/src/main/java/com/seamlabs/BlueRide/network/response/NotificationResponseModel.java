package com.seamlabs.BlueRide.network.response;

import com.seamlabs.BlueRide.network.BaseResponse;

public class NotificationResponseModel extends BaseResponse{

    String school;
    String message;
    String time;
    int is_read;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }
}
