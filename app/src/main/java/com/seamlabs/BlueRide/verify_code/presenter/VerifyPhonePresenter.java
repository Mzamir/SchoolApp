package com.seamlabs.BlueRide.verify_code.presenter;

import com.seamlabs.BlueRide.verify_code.view.VerificationCodeView;

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

    public void resendVerificationCode(String id) {
        if (view != null) {
            view.showProgress();
        }
        interactor.resendVerificationCode(id, this);
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
