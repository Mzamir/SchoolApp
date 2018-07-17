package com.example.mahmoudsamir.schoolappand.helper_account.presenter;

import android.view.View;

import com.example.mahmoudsamir.schoolappand.helper_account.view.HelperRegistrationView;

public class HelperSigninPresenter implements HelperRegistrationInteractor.OnRegistrationFinishedListener {

    HelperRegistrationView view;
    HelperRegistrationInteractor interactor;

    public HelperSigninPresenter(HelperRegistrationView view, HelperRegistrationInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void validateCredentials(String username, String password) {
        if (view != null) {
            view.showProgress();
        }
        interactor.helperSignin(username, password, this);
    }
    @Override
    public void onError() {
        if (view != null) {
            view.hideProgress();
            view.onErrorRegistration();
        }
    }

    @Override
    public void onSuccess() {
        if (view != null) {
            view.navigateToParentHome();
        }
    }
}
