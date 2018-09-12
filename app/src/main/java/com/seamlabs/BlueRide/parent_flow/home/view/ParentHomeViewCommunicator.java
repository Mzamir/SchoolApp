package com.seamlabs.BlueRide.parent_flow.home.view;

import com.seamlabs.BlueRide.network.response.ParentPickUpResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.model.SchoolModel;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;

import java.util.ArrayList;

public interface ParentHomeViewCommunicator {
    void showProgress();

    void hideProgress();

    void onErrorGettingSchools(String error);

    void onSuccessGettingSchool(ArrayList<SchoolModel> schoolList);

    void onSuccessGettingStudentForASchool(ArrayList<StudentModel> studentList);

//    void onSuccessPickUpRequest(ParentPickUpResponseModel responseModel);

    void getStudentsForASchool(String schoolId);

    void onSuccessGettingUserData() ;
}
