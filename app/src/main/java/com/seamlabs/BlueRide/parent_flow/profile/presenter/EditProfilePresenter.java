package com.seamlabs.BlueRide.parent_flow.profile.presenter;

import com.seamlabs.BlueRide.network.requests.EditProfileRequestModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.parent_flow.profile.view.EditProfileViewCommunicator;

public class EditProfilePresenter implements EditProfileInteactor.OnEditProfileInteractorListener {

    EditProfileViewCommunicator view;
    EditProfileInteactor inteactor;

    public EditProfilePresenter(EditProfileViewCommunicator view, EditProfileInteactor inteactor) {
        this.view = view;
        this.inteactor = inteactor;
    }

    public void editProfile(EditProfileRequestModel requestModel) {
        if (view != null) {
            view.showProgress();
        }
        inteactor.editProfile(requestModel, this);
    }

    public void editProfile(String url, boolean showProgress) {
        if (showProgress)
            if (view != null) {
                view.showProgress();
            }
        inteactor.editProfile(url , showProgress, this);
    }

    @Override
    public void onSuccessEditProfile(UserResponseModel userResponseModel) {
        if (view != null) {
            view.hideProgress();
            view.onSuccessEditProfile();
        }
    }

    @Override
    public void onErrorGettingEditProfile(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onErrorEditProfile(errorMessage);
        }
    }
}
