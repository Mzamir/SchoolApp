package com.example.mahmoudsamir.schoolappand.parent_flow.profile.view;

import com.example.mahmoudsamir.schoolappand.network.response.UserProfileResponseModel;

public interface ParentProfileViewCommunicator {

    void showProgress();

    void hideProgress();

    void onSuccessGettingUserProfile(UserProfileResponseModel userProfileResponseModel) ;

    void onErrorGettingUserProfile() ;
}
