package com.seamlabs.BlueRide.parent_flow.helper_profile;

import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;

public interface HelperProfileViewCommunicator {
    void showProgress();

    void hideProgress();

    void onSuccessGettingHelperStudents(UserProfileResponseModel userProfileResponseModel) ;

    void onErrorGettingHelperStudents() ;

}
