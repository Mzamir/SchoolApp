package com.seamlabs.BlueRide.parent_flow.waiting_student.view;

import com.seamlabs.BlueRide.network.response.CheckRequestStateResponseModel;
import com.seamlabs.BlueRide.network.response.MentorQueueResponseModel;

public interface ParentWaitingView {

    void showProgress();

    void hideProgress();

    void onSuccessReport(String successMessage);

    void onSuccessDelivery(String successMessage);

    void onError(String errorMessage);

    void onSuccessCheckingRequestState(CheckRequestStateResponseModel responseModel);

    void onErrorCheckingRequestState(String errorMessage);
}
