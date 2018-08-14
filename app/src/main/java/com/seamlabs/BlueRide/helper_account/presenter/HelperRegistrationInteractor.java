package com.seamlabs.BlueRide.helper_account.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.requests.UserRequestModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.utils.PrefUtils;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.ADMIN_LOGIN_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.PHONE_NUMBER_CODE;
import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;

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
        userRequestModel.setPhone(phone);
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
