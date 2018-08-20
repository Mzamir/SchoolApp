package com.seamlabs.BlueRide.network.response;

import com.google.gson.annotations.SerializedName;

public class MentorPusherMainResponseModel {
    private int mentor;
    @SerializedName("request")
    private MentorPusherEventResponseModel mentorPusherEventResponseModel;
    private boolean mentor_can_deliver;

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

    public boolean isMentor_can_deliver() {
        return mentor_can_deliver;
    }

    public void setMentor_can_deliver(boolean mentor_can_deliver) {
        this.mentor_can_deliver = mentor_can_deliver;
    }
}
