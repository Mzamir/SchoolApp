package com.example.mahmoudsamir.schoolappand.parent_flow.profile.presenter;

import com.example.mahmoudsamir.schoolappand.network.response.UserProfileResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.view.ParentProfileViewCommunicator;

public class ParentProfilePresenter implements ParentProfileInteactor.OnParentInteractorListener {
    ParentProfileViewCommunicator view;
    ParentProfileInteactor inteactor;

    public ParentProfilePresenter(ParentProfileViewCommunicator view, ParentProfileInteactor inteactor) {
        this.view = view;
        this.inteactor = inteactor;
    }

    public void getUserProfile() {
        if (view != null)
            view.showProgress();
        inteactor.getUserProfile(this);
    }

    @Override
    public void onSuccessGettingUserProfile(UserProfileResponseModel userProfileResponseModel) {
        if (view != null) {
            view.hideProgress();
            view.onSuccessGettingUserProfile(userProfileResponseModel);
        }
    }

    @Override
    public void onErrorGettingUserProfile() {
        if (view != null) {
            view.hideProgress();
            view.onErrorGettingUserProfile();
        }
    }
}
