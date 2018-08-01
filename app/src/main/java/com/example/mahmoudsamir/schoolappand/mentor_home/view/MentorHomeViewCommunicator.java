package com.example.mahmoudsamir.schoolappand.mentor_home.view;

import com.example.mahmoudsamir.schoolappand.mentor_home.model.MentorStudentModel;
import com.example.mahmoudsamir.schoolappand.network.response.MentorQueueResponseModel;

import java.util.ArrayList;

public interface MentorHomeViewCommunicator {

    void showProgress();

    void hideProgress();

    void showDeliveryAction(boolean showDeliveryAction);

    void onSuccessGettingStudents(ArrayList<MentorStudentModel> studentList);

    void onSuccessDeliverAction();

    void onError();

}
