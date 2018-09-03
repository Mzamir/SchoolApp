package com.seamlabs.BlueRide.verify_code.view;

public interface VerificationCodeView {

    void showProgress();

    void hideProgress();

    void onErrorVerifyPhone(String message) ;

    void navigateToParentHome();

    void onSuccessResendCode() ;

    void onErrorResendCode(String message);
}
