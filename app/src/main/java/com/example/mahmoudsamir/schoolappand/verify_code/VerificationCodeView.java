package com.example.mahmoudsamir.schoolappand.verify_code;

public interface VerificationCodeView {

    void showProgress();

    void hideProgress();

    void onErrorVerifyPhone() ;

    void navigateToParentHome();

}
