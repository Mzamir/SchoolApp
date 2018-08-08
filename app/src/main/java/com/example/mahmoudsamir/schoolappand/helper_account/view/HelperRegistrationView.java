package com.example.mahmoudsamir.schoolappand.helper_account.view;

public interface HelperRegistrationView {
    void showProgress();

    void hideProgress();

    void onErrorRegistration(String errorMessage) ;

    void navigateToParentHome(int status);
}
