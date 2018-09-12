package com.seamlabs.BlueRide.helper_account.presenter;

import android.view.View;
import android.widget.Toast;

import com.seamlabs.BlueRide.helper_account.view.HelperRegistrationView;

public class HelperRegistrationPresenter implements HelperRegistrationInteractor.OnRegistrationFinishedListener {

    HelperRegistrationView view;
    HelperRegistrationInteractor interactor;

    public HelperRegistrationPresenter(HelperRegistrationView view, HelperRegistrationInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void validateCredentials(String username, String password) {
        if (view != null) {
            view.showProgress();
        }
        interactor.helperSignin(username, password, this);
    }

    public void validateCredentials(String name, String email, String password, String national_id, String phone) {
        if (view != null) {
            view.showProgress();
        }
        interactor.helperSignup(name, email, password, national_id, phone, this);
    }

    @Override
    public void onError(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onErrorRegistration(errorMessage);
        }
    }

    @Override
    public void onSuccess(int status) {
        if (view != null) {
            view.hideProgress();
            view.navigateToParentHome(status);
        }
    }
}
