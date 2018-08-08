package com.example.mahmoudsamir.schoolappand.mentor_home.view;

import com.example.mahmoudsamir.schoolappand.mentor_home.model.MentorStudentModel;

import java.util.ArrayList;

public interface MentorHomeViewCommunicator {

    void showProgress();

    void hideProgress();

    void showDeliveryAction(boolean showDeliveryAction);

    void onSuccessGettingStudents(ArrayList<MentorStudentModel> studentList, int requestsCounter);

    void onSuccessDeliverAction();

    void onError();

}
