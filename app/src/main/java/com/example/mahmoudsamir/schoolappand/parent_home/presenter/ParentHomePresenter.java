package com.example.mahmoudsamir.schoolappand.parent_home.presenter;

import android.view.View;

import com.example.mahmoudsamir.schoolappand.network.response.UserSchoolsResponse;
import com.example.mahmoudsamir.schoolappand.parent_home.model.SchoolModel;
import com.example.mahmoudsamir.schoolappand.parent_home.view.ParentHomeView;

import java.util.ArrayList;
import java.util.List;

public class ParentHomePresenter implements ParentHomeInteractor.OnGettingParentSchoolFinishedListener {

    ParentHomeView view;
    ParentHomeInteractor interactor;

    public ParentHomePresenter(ParentHomeView view, ParentHomeInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void getParentSchools() {
        if (view != null)
            view.showProgress();
        interactor.getParentSchools(this);
    }

    @Override
    public void onSuccess(ArrayList<UserSchoolsResponse> userSchoolsResponses) {
        if (view != null) {
            view.hideProgress();
            if (userSchoolsResponses!=null && userSchoolsResponses.size()>0)
            view.onSuccessGettingSchool(convertResponseToModel(userSchoolsResponses));
        }
    }

    @Override
    public void onError(String error) {
        if (view != null) {
            view.hideProgress();
            view.onErrorGettingSchools(error);
        }
    }

    private ArrayList<SchoolModel> convertResponseToModel(ArrayList<UserSchoolsResponse> userSchoolsResponses) {
        ArrayList<SchoolModel> schoolModels = new ArrayList<>();
        for (UserSchoolsResponse userSchoolsResponse : userSchoolsResponses) {
            SchoolModel schoolModel = new SchoolModel();
            schoolModel.setMarked(false);
            schoolModel.setSchoolTitle(userSchoolsResponse.getName());
            schoolModel.setShcoolAddress(userSchoolsResponse.getAddress());
            schoolModel.setShcoolID(userSchoolsResponse.getId());
            schoolModel.setShcoolCreatedAt(userSchoolsResponse.getCreated_at());
            schoolModel.setShcoolUpdatedAt(userSchoolsResponse.getUpdated_at());
            schoolModel.setShcoolLong(userSchoolsResponse.getLongitude());
            schoolModel.setShcoolLat(userSchoolsResponse.getLat());
            schoolModels.add(schoolModel);
        }
        return schoolModels;
    }
}
