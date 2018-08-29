package com.seamlabs.BlueRide.parent_flow.profile.presenter;

import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;
import com.seamlabs.BlueRide.parent_flow.profile.view.ParentProfileViewCommunicator;

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

    public void updateStudentPicture(String imagePath , String national_id) {

        inteactor.editStudentImage(imagePath , national_id, this);
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

    @Override
    public void onSuccessEditingStudentImage() {
        if (view != null) {
            view.onSuccessEditingStudentImage();
        }
    }

    @Override
    public void onErrorEditingStudentImage(String message) {
        if (view != null) {
            view.onErrorEditingStudentImage(message);
        }
    }
}
