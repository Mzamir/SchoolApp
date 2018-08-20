package com.seamlabs.BlueRide.parent_flow.helper_profile;

import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;

public interface HelperProfileViewCommunicator {
    void showProgress();

    void hideProgress();

    void onSuccessChangeHelperState();

    void onErrorChangeHelperState(String errorMessage);

    void onSuccessAssigningHelper();

    void onErrorAssigningHelper(String errorMessage);

    void showPermissionActions(boolean show);
}
