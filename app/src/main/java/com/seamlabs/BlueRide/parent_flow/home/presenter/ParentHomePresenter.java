package com.seamlabs.BlueRide.parent_flow.home.presenter;

import com.seamlabs.BlueRide.network.requests.ParentPickUpRequestModel;
import com.seamlabs.BlueRide.network.response.ParentPickUpResponseModel;
import com.seamlabs.BlueRide.network.response.SchoolsResponse;
import com.seamlabs.BlueRide.network.response.ParentStudentForASchoolResponse;
import com.seamlabs.BlueRide.parent_flow.home.model.SchoolModel;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;
import com.seamlabs.BlueRide.parent_flow.home.view.ParentHomeViewCommunicator;

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


    @Override
    public void onSuccessGettingParentSchools(ArrayList<SchoolsResponse> schoolsResponse) {
        if (view != null) {
            view.hideProgress();
            if (schoolsResponse != null)
                view.onSuccessGettingSchool(convertResponseToModel(schoolsResponse));
        }
    }

    @Override
    public void onSuccessGettingParentStudentsForASchool(ArrayList<ParentStudentForASchoolResponse> parentStudentForASchoolResponses) {
        if (view != null) {
            view.hideProgress();
            if (parentStudentForASchoolResponses != null)
                view.onSuccessGettingStudentForASchool(convertStudentsResponseToStudentModel(parentStudentForASchoolResponses));
        }
    }


    private ArrayList<StudentModel> convertStudentsResponseToStudentModel(ArrayList<ParentStudentForASchoolResponse> parentStudentForASchoolResponses) {
        ArrayList<StudentModel> studentModels = new ArrayList<>();
        for (ParentStudentForASchoolResponse response : parentStudentForASchoolResponses) {
            StudentModel studentModel = new StudentModel();
            if (response.getImages() != null) {
                if (response.getImages().size() > 0) {
                    studentModel.setStudentPicture(response.getImages().get(0).getPath());
                }
            }
            studentModel.setMarked(false);
            studentModel.setStudentID(response.getId());
            studentModel.setStudentName(response.getName());
            studentModel.setStudentNationalID(response.getNational_id());
            studentModel.setSchoolID(response.getSchool_id());
            studentModel.setClassID(response.getClass_id());
            studentModel.setStudentCreatedAt(response.getCreated_at());
            studentModel.setStudentUpdatedAt(response.getUpdated_at());
            studentModel.setClass_name(response.getClass_name());
            studentModel.setGrade_name(response.getGrade_name());
            studentModel.setIn_request(response.isIn_request());
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

    private ArrayList<SchoolModel> convertResponseToModel(ArrayList<SchoolsResponse> schoolsResponseList) {
        ArrayList<SchoolModel> schoolModels = new ArrayList<>();
        for (SchoolsResponse schoolsResponse : schoolsResponseList) {
            SchoolModel schoolModel = new SchoolModel();
            if (schoolsResponse.getImages() != null) {
                if (schoolsResponse.getImages().size() > 0) {
                    schoolModel.setSchoolCover(schoolsResponse.getImages().get(0).getPath());
                }
            }
            schoolModel.setMarked(false);
            schoolModel.setSchoolTitle(schoolsResponse.getName());
            schoolModel.setschoolAddress(schoolsResponse.getAddress());
            schoolModel.setschoolID(schoolsResponse.getId());
            schoolModel.setschoolCreatedAt(schoolsResponse.getCreated_at());
            schoolModel.setschoolUpdatedAt(schoolsResponse.getUpdated_at());
            schoolModel.setschoolLong(schoolsResponse.getLongitude());
            schoolModel.setschoolLat(schoolsResponse.getLat());
            schoolModel.setBig_zone(schoolsResponse.getBig_zone());
            schoolModel.setSmall_zone(schoolsResponse.getSmall_zone());
            schoolModels.add(schoolModel);
        }
        return schoolModels;
    }
}
