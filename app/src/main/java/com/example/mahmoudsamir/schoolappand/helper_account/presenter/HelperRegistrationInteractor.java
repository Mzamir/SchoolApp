package com.example.mahmoudsamir.schoolappand.helper_account.presenter;

import android.util.Log;

import com.example.mahmoudsamir.schoolappand.MyApplication;
import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.requests.UserRequestModel;
import com.example.mahmoudsamir.schoolappand.network.response.UserResponseModel;
import com.example.mahmoudsamir.schoolappand.utils.PrefUtils;
import com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.mahmoudsamir.schoolappand.MyApplication.getMyApplicationContext;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.ADMIN_LOGIN_ERROR;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.GENERAL_ERROR;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.PHONE_NUMBER_CODE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.SERVER_ERROR;

public class HelperRegistrationInteractor {

    String TAG = HelperRegistrationInteractor.class.getSimpleName();

    public interface OnRegistrationFinishedListener {

        void onError(String errorMessage);

        void onSuccess(int status);
    }

    void helperSignin(String email, String password, final OnRegistrationFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserResponseModel>() {
                    @Override
                    public void onSuccess(UserResponseModel responseModel) {
                        if (responseModel.getRoles().size() == 1 && responseModel.getRoles().get(0).getName().equalsIgnoreCase("admin")) {
                            listener.onError(ADMIN_LOGIN_ERROR);
                            return;
                        }
                        if (responseModel.getErrors() != null) {
                            listener.onError(responseModel.getErrors());
                            Log.i(TAG, "Error " + responseModel.getErrors());
                            return;
                        }
                        if (responseModel != null) {
                            listener.onSuccess(responseModel.getStatus());
                            PrefUtils.storeApiKey(getMyApplicationContext(), responseModel.getToken());
                            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
                            UserSettingsPreference.saveUserProfile(getMyApplicationContext(), responseModel);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(SERVER_ERROR);
                    }
                });

    }

    void helperSignup(String name, String email, String password, String national_id, String phone, final OnRegistrationFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);
        UserRequestModel userRequestModel = new UserRequestModel();
        userRequestModel.setEmail(email);
        userRequestModel.setName(name);
        userRequestModel.setNational_id(national_id);
        userRequestModel.setPassword(password);
        userRequestModel.setPhone(PHONE_NUMBER_CODE + phone);
        apiService.signupHelper(userRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserResponseModel>() {
                    @Override
                    public void onSuccess(UserResponseModel responseModel) {
                        if (responseModel.getRoles().size() == 1 && responseModel.getRoles().get(0).getName().equalsIgnoreCase("admin")) {
                            listener.onError(ADMIN_LOGIN_ERROR);
                            return;
                        }
                        if (responseModel.getErrors() != null) {
                            listener.onError(responseModel.getErrors());
                            Log.i(TAG, "Error " + responseModel.getErrors());
                            return;
                        }
                        if (responseModel != null) {
                            listener.onSuccess(responseModel.getStatus());
                            PrefUtils.storeApiKey(getMyApplicationContext(), responseModel.getToken());
                            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
                            UserSettingsPreference.saveUserProfile(getMyApplicationContext(), responseModel);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "Error " + e.getMessage());
                        listener.onError(SERVER_ERROR);
                    }
                });
    }
}
