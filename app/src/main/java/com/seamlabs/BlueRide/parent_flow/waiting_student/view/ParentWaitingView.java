package com.seamlabs.BlueRide.parent_flow.waiting_student.view;

public interface ParentWaitingView {

    void showProgress();

    void hideProgress();

    void onSuccessReport(String successMessage);

    void onSuccessDelivery(String successMessage);

    void onError(String errorMessage);
}
