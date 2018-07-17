package com.example.mahmoudsamir.schoolappand.verify_code.presenter;

import com.example.mahmoudsamir.schoolappand.verify_code.VerificationCodeView;

public class VerifyPhonePresenter implements VerifyPhoneInteractor.OnVerifyPhoneFinishedListener {

    VerificationCodeView view;
    VerifyPhoneInteractor interactor;

    public VerifyPhonePresenter(VerificationCodeView view, VerifyPhoneInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void verifyPhone(String code, String national_id) {
        if (view != null) {
            view.showProgress();
        }
        interactor.verifyPhone(code, national_id, this);
    }

    @Override
    public void onError() {
        if (view != null) {
            view.hideProgress();
            view.onErrorVerifyPhone();
        }
    }

    @Override
    public void onSuccess() {
        if (view != null) {
            view.hideProgress();
            view.navigateToParentHome();
        }
    }
}
