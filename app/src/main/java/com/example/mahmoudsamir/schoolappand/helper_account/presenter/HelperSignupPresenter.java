package com.example.mahmoudsamir.schoolappand.helper_account.presenter;

import com.example.mahmoudsamir.schoolappand.helper_account.view.HelperRegistrationView;

public class HelperSignupPresenter implements HelperRegistrationInteractor.OnRegistrationFinishedListener {

    HelperRegistrationView view;
    HelperRegistrationInteractor interactor;

    public HelperSignupPresenter(HelperRegistrationView view, HelperRegistrationInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void validateCredentials(String name, String email, String password, String national_id, String phone) {
        if (view != null) {
            view.showProgress();
        }
        interactor.helperSignup(name, email, password, national_id, phone, this);
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
