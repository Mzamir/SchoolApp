package com.seamlabs.BlueRide.parent_flow.pick_up.presenter;

import com.seamlabs.BlueRide.network.requests.ParentPickUpRequestModel;
import com.seamlabs.BlueRide.network.response.ParentArrivedResponseModel;
import com.seamlabs.BlueRide.network.response.ParentPickUpResponseModel;
import com.seamlabs.BlueRide.parent_flow.pick_up.view.ParentPickUpView;

public class ParentPickUpPresenter implements ParentPickUpInteractor.onPickerArrivedListener {

    ParentPickUpView view;
    ParentPickUpInteractor interactor;

    public ParentPickUpPresenter(ParentPickUpView view, ParentPickUpInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void parent_arrived(int request_id) {
        if (view != null)
            view.showProgress();

        interactor.parent_arrived(request_id, this);
    }

    @Override
    public void onSuccess(ParentArrivedResponseModel parentArrivedResponseModel) {
        if (view != null) {
            view.hideProgress();
            view.onSuccess(parentArrivedResponseModel);
        }
    }

    @Override
    public void onError(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onError();
        }
    }
    public void parentPickUpRequest(ParentPickUpRequestModel pickUpRequestModel) {
        if (view != null)
            view.showProgress();
        interactor.parentPickUpRequest(pickUpRequestModel, this);
    }
    @Override
    public void onSuccessPickUpRequest(ParentPickUpResponseModel responseModel) {
        if (view != null) {
            view.hideProgress();
            if (responseModel != null)
                view.onSuccessPickUpRequest(responseModel);
        }
    }

    public void cancelRequest(int request_id) {
        if (view!=null){
            view.showProgress();
        }
        interactor.cancelPickUpRequest(request_id , this) ;
    }
}
