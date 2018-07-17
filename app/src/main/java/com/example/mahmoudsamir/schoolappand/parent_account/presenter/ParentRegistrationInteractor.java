package com.example.mahmoudsamir.schoolappand.parent_account.presenter;

import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.BaseResponse;
import com.example.mahmoudsamir.schoolappand.network.response.ParentSignupResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.mahmoudsamir.schoolappand.MyApplication.getMyApplicationContext;

public class ParentRegistrationInteractor {

    public interface OnParentSignInFinishedListener {
        void onError();

        void onSuccess();
    }


    // here to implement the REST-API
    public void parenetSignup(final String national_id, final String password, final OnParentSignInFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(getMyApplicationContext())
                .create(ApiService.class);

        apiService.signUpParent(national_id, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ParentSignupResponse>() {
                    @Override
                    public void onSuccess(ParentSignupResponse parentSignupResponse) {
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                    }
                });
    }

    public void parenetSignin(final String email, final String password, final OnParentSignInFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(getMyApplicationContext())
                .create(ApiService.class);

        apiService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse parentSignInResponse) {
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                    }
                });
    }
}
