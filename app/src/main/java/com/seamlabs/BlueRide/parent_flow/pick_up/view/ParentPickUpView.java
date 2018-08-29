package com.seamlabs.BlueRide.parent_flow.pick_up.view;

import com.seamlabs.BlueRide.network.response.ParentArrivedResponseModel;
import com.seamlabs.BlueRide.network.response.ParentPickUpResponseModel;

public interface ParentPickUpView {
    void showProgress();

    void hideProgress();

    void onSuccessParentArrived(ParentArrivedResponseModel parentArrivedResponseModel);

    void onSuccessPickUpRequest(ParentPickUpResponseModel responseModel);

    void onError(String errorMessage);

    void onSuccessCancelingRequest(String success);
}
