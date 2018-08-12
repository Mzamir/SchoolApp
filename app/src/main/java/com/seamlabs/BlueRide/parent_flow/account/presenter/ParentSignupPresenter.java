package com.seamlabs.BlueRide.parent_flow.account.presenter;

import com.seamlabs.BlueRide.parent_flow.account.view.ParentRegistrationView;

public class ParentSignupPresenter implements ParentRegistrationInteractor.OnParentSignInFinishedListener {

    private ParentRegistrationView view;
    private ParentRegistrationInteractor interactor;

    public ParentSignupPresenter(ParentRegistrationView view, ParentRegistrationInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void validateCredentials(String national_id, String password) {
        if (view != null) {
            view.showProgress();
        }
        interactor.parenetSignup(national_id, password, this);
    }

    @Override
    public void onError(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onErrorRegistration(errorMessage);
        }
    }

    @Override
    public void onSuccess() {
        if (view != null) {
            view.navigateToParentHome();
        }
    }
}