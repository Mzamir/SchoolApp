package com.seamlabs.BlueRide.parent_flow.profile.view;

import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.network.response.StudentResponseModel;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;

public interface ParentProfileViewCommunicator {

    void showProgress();

    void hideProgress();

    void onSuccessGettingUserProfile(UserProfileResponseModel userProfileResponseModel) ;

    void onErrorGettingUserProfile() ;

    void onHelperClickListener(HelperResponseModel helperModel) ;

    void showPermissionActions(boolean show) ;
}
