package com.example.mahmoudsamir.schoolappand.parent_flow.account.presenter;

import com.example.mahmoudsamir.schoolappand.parent_flow.account.view.ParentRegistrationView;

public class ParentSignInPresenter implements ParentRegistrationInteractor.OnParentSignInFinishedListener {

    private ParentRegistrationView view;
    private ParentRegistrationInteractor interactor;

    public ParentSignInPresenter(ParentRegistrationView view, ParentRegistrationInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void validateCredentials(String email, String password) {
        if (view != null) {
            view.showProgress();
        }
        interactor.parenetSignin(email, password, this);
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
