package com.example.mahmoudsamir.schoolappand.parent_signin.presenter;

import com.example.mahmoudsamir.schoolappand.parent_signin.view.ParentSigninView;

public class ParentSignInPresenter implements ParentSignInInteractor.OnParentSignInFinishedListener {

    private ParentSigninView view;
    private ParentSignInInteractor interactor;

    public ParentSignInPresenter(ParentSigninView view, ParentSignInInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void validateCredentials(String username, String password) {
        if (view != null) {
            view.showProgress();
        }
        interactor.login(username, password, this);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess() {
        if (view != null) {
            view.navigateToParentHome();
        }
    }
}
