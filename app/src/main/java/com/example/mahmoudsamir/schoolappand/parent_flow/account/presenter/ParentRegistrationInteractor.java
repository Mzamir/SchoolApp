package com.example.mahmoudsamir.schoolappand.parent_flow.account.presenter;

import android.util.Log;

import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.response.LoginResponse;
import com.example.mahmoudsamir.schoolappand.network.response.ParentSignupResponse;
import com.example.mahmoudsamir.schoolappand.utils.PrefUtils;
import com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.mahmoudsamir.schoolappand.MyApplication.getMyApplicationContext;

public class ParentRegistrationInteractor {

    String TAG = ParentRegistrationInteractor.class.getSimpleName();

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
                        if (parentSignupResponse.getErrors() != null) {
                            listener.onError();
                            Log.i(TAG, "Error " + parentSignupResponse.getErrors());
                        } else if (parentSignupResponse.getEmail() != null) {
                            Log.i(TAG, "Success " + parentSignupResponse.getEmail());
                            PrefUtils.storeApiKey(getMyApplicationContext(), parentSignupResponse.getToken());
                            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
                            listener.onSuccess();
                        }
                        Log.i(TAG, "Success ");
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

        Log.i(TAG, "parenetSignin");
        apiService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        if (loginResponse.getErrors() != null) {
                            listener.onError();
                            Log.i(TAG, "Error " + loginResponse.getErrors());
                        } else if (loginResponse.getEmail() != null) {
                            Log.i(TAG, "Success " + loginResponse.getEmail());
                            PrefUtils.storeApiKey(getMyApplicationContext(), loginResponse.getToken());
                            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
                            listener.onSuccess();
                        }
                        Log.i(TAG, "Success ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                        Log.i(TAG, "onError " + e.getMessage().toString());
                    }
                });
    }
}
