package com.example.mahmoudsamir.schoolappand.parent_flow.home.presenter;

import com.example.mahmoudsamir.schoolappand.network.requests.ParentPickUpRequestModel;
import com.example.mahmoudsamir.schoolappand.network.response.ParentPickUpResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.ParentSchoolsResponse;
import com.example.mahmoudsamir.schoolappand.network.response.ParentStudentForASchoolResponse;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.model.SchoolModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.model.StudentModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.view.ParentHomeViewCommunicator;

import java.util.ArrayList;

public class ParentHomePresenter implements ParentHomeInteractor.OnGettingParentSchoolFinishedListener {

    ParentHomeViewCommunicator view;
    ParentHomeInteractor interactor;

    public ParentHomePresenter(ParentHomeViewCommunicator view, ParentHomeInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void getParentSchools() {
        if (view != null)
            view.showProgress();
        interactor.getParentSchools(this);
    }

    public void getParentStudentForASchool(String schoolID) {
        if (view != null)
            view.showProgress();
        interactor.getParentStudentsForASchool(schoolID, this);
    }

    public void parentPickUpRequest(ParentPickUpRequestModel pickUpRequestModel) {
        if (view != null)
            view.showProgress();
        interactor.parentPickUpRequest(pickUpRequestModel, this);
    }

    @Override
    public void onSuccessGettingParentSchools(ArrayList<ParentSchoolsResponse> parentSchoolsResponse) {
        if (view != null) {
            view.hideProgress();
            if (parentSchoolsResponse != null && parentSchoolsResponse.size() > 0)
                view.onSuccessGettingSchool(convertResponseToModel(parentSchoolsResponse));
        }
    }

    @Override
    public void onSuccessGettingParentStudentsForASchool(ArrayList<ParentStudentForASchoolResponse> parentStudentForASchoolResponses) {
        if (view != null) {
            view.hideProgress();
            if (parentStudentForASchoolResponses != null && parentStudentForASchoolResponses.size() > 0)
                view.onSuccessGettingStudentForASchool(convertStudentsResponseToStudentModel(parentStudentForASchoolResponses));
        }
    }

    @Override
    public void onSuccessPickUpRequest(ParentPickUpResponseModel responseModel) {
        if (view != null) {
            view.hideProgress();
            if (responseModel != null)
                view.onSuccessPickUpRequest(responseModel);
        }
    }

    private ArrayList<StudentModel> convertStudentsResponseToStudentModel(ArrayList<ParentStudentForASchoolResponse> parentStudentForASchoolResponses) {
        ArrayList<StudentModel> studentModels = new ArrayList<>();
        for (ParentStudentForASchoolResponse response : parentStudentForASchoolResponses) {
            StudentModel studentModel = new StudentModel();
            studentModel.setMarked(false);
            studentModel.setStudentID(response.getId());
            studentModel.setStudentName(response.getName());
            studentModel.setStudentNationalID(response.getNational_id());
            studentModel.setSchoolID(response.getSchool_id());
            studentModel.setClassID(response.getClass_id());
            studentModel.setStudentCreatedAt(response.getCreated_at());
            studentModel.setStudentUpdatedAt(response.getUpdated_at());
            studentModels.add(studentModel);
        }
        return studentModels;
    }

    @Override
    public void onError(String error) {
        if (view != null) {
            view.hideProgress();
            view.onErrorGettingSchools(error);
        }
    }

    private ArrayList<SchoolModel> convertResponseToModel(ArrayList<ParentSchoolsResponse> parentSchoolsRespons) {
        ArrayList<SchoolModel> schoolModels = new ArrayList<>();
        for (ParentSchoolsResponse parentSchoolsResponse : parentSchoolsRespons) {
            SchoolModel schoolModel = new SchoolModel();
            schoolModel.setMarked(false);
            schoolModel.setSchoolTitle(parentSchoolsResponse.getName());
            schoolModel.setschoolAddress(parentSchoolsResponse.getAddress());
            schoolModel.setschoolID(parentSchoolsResponse.getId());
            schoolModel.setschoolCreatedAt(parentSchoolsResponse.getCreated_at());
            schoolModel.setschoolUpdatedAt(parentSchoolsResponse.getUpdated_at());
            schoolModel.setschoolLong(parentSchoolsResponse.getLongitude());
            schoolModel.setschoolLat(parentSchoolsResponse.getLat());
            schoolModels.add(schoolModel);
        }
        return schoolModels;
    }
}
