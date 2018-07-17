package com.example.mahmoudsamir.schoolappand.helper_account.presenter;

import com.example.mahmoudsamir.schoolappand.MyApplication;
import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.BaseResponse;
import com.example.mahmoudsamir.schoolappand.network.response.HelperSignupResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class HelperRegistrationInteractor {

    public interface OnRegistrationFinishedListener {

        void onError();

        void onSuccess();
    }

    void helperSignin(String email, String password, final OnRegistrationFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onSuccess();
                    }
                });

    }

    void helperSignup(String name, String email, String password, String national_id, String phone , final OnRegistrationFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.signupHelper(name, email, password, national_id, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<HelperSignupResponse>() {
                    @Override
                    public void onSuccess(HelperSignupResponse helperSignupResponse) {
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onSuccess();
                    }
                });
    }
}
