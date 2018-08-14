package com.seamlabs.BlueRide.parent_flow.tracking_helper.presenter;

import com.seamlabs.BlueRide.parent_flow.profile.model.HelperModel;
import com.seamlabs.BlueRide.parent_flow.tracking_helper.view.TrackingHelpersViewCommunicator;

import java.util.ArrayList;

public class TrackingHelperPresenter implements TrackingHelperInteractor.TrackingHelperInteractorListener {

    TrackingHelpersViewCommunicator view;
    TrackingHelperInteractor interactor;

    public TrackingHelperPresenter(TrackingHelpersViewCommunicator communicator, TrackingHelperInteractor interactor) {
        this.view = communicator;
        this.interactor = interactor;
    }

    public void getHelpers() {
        if (view != null) {
            view.showProgress();
        }
        interactor.getHelpers(this);
    }

    @Override
    public void onSuccessGettingHelpers(ArrayList<HelperModel> helperModels) {
        if (view != null) {
            view.hideProgress();
            view.onSuccessGettingHelpers(helperModels);
        }

    }

    @Override
    public void onError(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onError(errorMessage);
        }
    }
}
