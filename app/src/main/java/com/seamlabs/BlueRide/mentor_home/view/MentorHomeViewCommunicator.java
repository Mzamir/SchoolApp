package com.seamlabs.BlueRide.mentor_home.view;

import com.seamlabs.BlueRide.mentor_home.model.MentorStudentModel;

import java.util.ArrayList;

public interface MentorHomeViewCommunicator {

    void showProgress();

    void hideProgress();

    void showDeliveryAction(boolean showDeliveryAction);

    void onSuccessGettingStudents(ArrayList<MentorStudentModel> studentList, int requestsCounter);

    void onSuccessDeliverAction();

    void onError();

}
