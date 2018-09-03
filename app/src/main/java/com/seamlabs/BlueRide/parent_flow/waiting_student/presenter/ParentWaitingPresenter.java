package com.seamlabs.BlueRide.parent_flow.waiting_student.presenter;

import com.seamlabs.BlueRide.network.response.CheckRequestStateResponseModel;
import com.seamlabs.BlueRide.network.response.MentorQueueResponseModel;
import com.seamlabs.BlueRide.parent_flow.waiting_student.view.ParentWaitingView;

public class ParentWaitingPresenter implements ParentWaitingInteractor.OnParentWaitingInteractorListener {

    ParentWaitingInteractor interactor;
    ParentWaitingView view;

    public ParentWaitingPresenter(ParentWaitingView view, ParentWaitingInteractor interactor) {
        this.interactor = interactor;
        this.view = view;
    }

    public ParentWaitingPresenter(ParentWaitingInteractor interactor) {
        this.interactor = interactor;
    }

    public void report(int request_id) {
        if (view != null)
            view.showProgress();

        interactor.report(request_id, this);
    }

    public void delivered(int request_id) {
        if (view != null)
            view.showProgress();

        interactor.delivered(request_id, this);
    }
    public void checkIfCanReceive(int request_id) {
        if (view != null)
            view.showProgress();

        interactor.checkIfCanReceive(request_id, this);
    }

    public void updateHelperLocation(double lat, double longitude) {
        if (view != null) {
            view.showProgress();
        }
        interactor.updateLocation(lat, longitude, this);
    }

    @Override
    public void onSuccessReport(String successMessage) {
        if (view != null) {
            view.hideProgress();
            view.onSuccessReport(successMessage);
        }
    }

    @Override
    public void onSuccessReceived(String successMessage) {
        if (view != null) {
            view.hideProgress();
            view.onSuccessDelivery(successMessage);
        }
    }

    @Override
    public void onErrorReport(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onError(errorMessage);
        }
    }

    @Override
    public void onErrorReceived(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onError(errorMessage);
        }
    }

    @Override
    public void onSuccessCheckingRequestState(CheckRequestStateResponseModel responseModel) {
        if (view != null) {
            view.hideProgress();
            view.onSuccessCheckingRequestState(responseModel);
        }
    }

    @Override
    public void onErrorCheckingRequestState(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onErrorCheckingRequestState(errorMessage);
        }
    }
}
