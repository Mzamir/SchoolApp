package com.example.mahmoudsamir.schoolappand.parent_flow.account.view;

public interface ParentRegistrationView {

    void showProgress();

    void hideProgress();

    void onErrorRegistration(String errorMessage) ;

    void navigateToParentHome();

}
