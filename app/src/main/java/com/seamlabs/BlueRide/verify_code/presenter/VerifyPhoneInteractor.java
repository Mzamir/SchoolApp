package com.seamlabs.BlueRide.verify_code.presenter;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class VerifyPhoneInteractor {

    public interface OnVerifyPhoneFinishedListener {
        void onError();

        void onSuccess();
    }

    public void verifyPhone(String code, String national_id, final OnVerifyPhoneFinishedListener listener) {

        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.verifyCode(code, national_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                    }
                });
    }

    public void resendVerificationCode(String code, final OnVerifyPhoneFinishedListener listener) {

        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.resendVerificationCode(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                    }
                });
    }
}