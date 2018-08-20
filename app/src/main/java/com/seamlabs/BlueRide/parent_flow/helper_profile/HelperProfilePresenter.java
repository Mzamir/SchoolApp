package com.seamlabs.BlueRide.parent_flow.helper_profile;

import com.seamlabs.BlueRide.network.requests.AssignStudentsToHelperRequestModel;

public class HelperProfilePresenter implements HelperProfileInteractor.onEditChangeHelperStatesListener {
    HelperProfileViewCommunicator view;
    HelperProfileInteractor interactor;

    public HelperProfilePresenter(HelperProfileViewCommunicator view, HelperProfileInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void changeHelperState(int helperID) {
        if (view != null)
            view.showProgress();

        interactor.changeHelperState(helperID, this);
    }

    public void assignHelperToStudents(AssignStudentsToHelperRequestModel requestModel) {
        if (view != null)
            view.showProgress();

        interactor.assignHelperToStudents(requestModel, this);
    }

    @Override
    public void onSuccessChangeHelperState() {
        if (view != null) {
            view.hideProgress();
            view.onSuccessChangeHelperState();
        }
    }

    @Override
    public void onErrorChangeHelperState(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onErrorChangeHelperState(errorMessage);
        }
    }

    @Override
    public void onSuccessAssigningHelper() {
        if (view != null) {
            view.hideProgress();
            view.onSuccessAssigningHelper();
        }
    }

    @Override
    public void onErrorAssigningHelper(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onErrorAssigningHelper(errorMessage);
        }
    }
}
