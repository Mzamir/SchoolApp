package com.seamlabs.BlueRide.parent_flow.helper_profile;

import com.seamlabs.BlueRide.network.response.HelperProfileResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;

public interface HelperProfileViewCommunicator {
    void showProgress();

    void hideProgress();

    void onSuccessChangeHelperState();

    void onErrorChangeHelperState(String errorMessage);

    void onSuccessAssigningHelper();

    void onErrorAssigningHelper(String errorMessage);

    void showPermissionActions(boolean show, StudentModel studentModel);

    void onSuccessGettingHelperProfile(HelperProfileResponseModel helperProfileResponse);

    void onErrorGettingHelperProfile(String message);


}
