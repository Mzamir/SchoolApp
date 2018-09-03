package com.seamlabs.BlueRide.verify_code.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.seamlabs.BlueRide.utils.Constants.BASE_URL;
import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;

public class VerifyPhoneInteractor {

    String TAG = VerifyPhoneInteractor.class.getSimpleName();

    public interface OnVerifyPhoneFinishedListener {
        void onError(String message);

        void onSuccess();

        void onSuccessResendCode();

        void onErrorResendCode(String message);
    }

    public void verifyPhone(String code, String national_id, final OnVerifyPhoneFinishedListener listener) {

        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.verifyCode(code, national_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getMessage() != null) {
                            listener.onError(response.getMessage());
                            return;
                        }
                        if (response.getErrors() != null) {
                            listener.onError(response.getErrors());
                            return;
                        }
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(GENERAL_ERROR);

                    }
                });
    }

    public void resendVerificationCode(int code, final OnVerifyPhoneFinishedListener listener) {

        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.resendVerificationCode(BASE_URL + "resend_verification_code/" + code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getMessage() != null) {
                            listener.onErrorResendCode(response.getMessage());
                            return;
                        }
                        if (response.getErrors() != null) {
                            listener.onErrorResendCode(response.getErrors());
                            return;
                        }
                        listener.onSuccessResendCode();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onErrorResendCode(GENERAL_ERROR);
                        Log.i(TAG, "Resend exception " + e.getMessage());
                    }
                });
    }
}
