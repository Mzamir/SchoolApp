package com.seamlabs.BlueRide.network.response;

import com.google.gson.annotations.SerializedName;

public class MentorPusherMainResponseModel {
    private int mentor;
    @SerializedName("request")
    private MentorPusherEventResponseModel mentorPusherEventResponseModel;

    public int getMentor() {
        return mentor;
    }

    public void setMentor(int mentor) {
        this.mentor = mentor;
    }

    public MentorPusherEventResponseModel getMentorPusherEventResponseModel() {
        return mentorPusherEventResponseModel;
    }

    public void setMentorPusherEventResponseModel(MentorPusherEventResponseModel mentorPusherEventResponseModel) {
        this.mentorPusherEventResponseModel = mentorPusherEventResponseModel;
    }
}
