package com.seamlabs.BlueRide.parent_flow.account.view;

public interface ParentRegistrationView {

    void showProgress();

    void hideProgress();

    void onErrorRegistration(String errorMessage) ;

    void navigateToParentHome(int status);

    void onSuccessGettingTerms();
}
