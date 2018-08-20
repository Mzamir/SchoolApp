package com.seamlabs.BlueRide.parent_flow.profile.view;

public interface EditProfileViewCommunicator {
    void showProgress();

    void hideProgress();

    void onSuccessEditProfile() ;

    void onErrorEditProfile(String errorMessage) ;
}
