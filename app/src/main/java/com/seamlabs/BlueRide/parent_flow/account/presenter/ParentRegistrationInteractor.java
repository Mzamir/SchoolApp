package com.seamlabs.BlueRide.parent_flow.account.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.utils.PrefUtils;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.ADMIN_LOGIN_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;
import static com.seamlabs.BlueRide.utils.PrefUtils.storeDeviceToken;

public class ParentRegistrationInteractor {

    String TAG = ParentRegistrationInteractor.class.getSimpleName();


    public interface OnParentSignInFinishedListener {
        void onError(String errorMessage);

        void onSuccess(int status);

    }


    // here to implement the REST-API
    public void parenetSignup(final String national_id, final String password, final OnParentSignInFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(getMyApplicationContext())
                .create(ApiService.class);
        storeDeviceToken(MyApplication.getMyApplicationContext(), national_id);
        apiService.signUpParent(national_id, password, national_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response response) {
                        if (response.isSuccessful()) {
                            UserResponseModel userResponseModel = (UserResponseModel) response.body();
                            if (userResponseModel.getLogin_as() != null) {
                                if (userResponseModel.getLogin_as().equalsIgnoreCase("admin") || userResponseModel.getLogin_as().equalsIgnoreCase("security")) {
                                    listener.onError(ADMIN_LOGIN_ERROR);
                                    return;
                                }
                            }
                            if (userResponseModel.getErrors() != null) {
                                listener.onError(userResponseModel.getErrors());
                                Log.i(TAG, "Error " + userResponseModel.getErrors());
                                return;
                            } else if (userResponseModel.getEmail() != null) {
                                Log.i(TAG, "Success " + userResponseModel.getEmail());
                                PrefUtils.storeApiKey(getMyApplicationContext(), userResponseModel.getToken());
                                UserSettingsPreference.saveUserProfile(getMyApplicationContext(), userResponseModel);
                                listener.onSuccess(userResponseModel.getStatus());
                                return;
                            }
                        } else if (response.code() == 400) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                listener.onError(jObjError.getString("errors"));
                            } catch (Exception e) {
                                listener.onError(SERVER_ERROR);
                            }
                        } else {
                            listener.onError(SERVER_ERROR);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(SERVER_ERROR);
                        Log.i(TAG, "Error " + e.getMessage().toString());
                    }
                });
    }

    public void parenetSignin(final String email, final String password, final OnParentSignInFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(getMyApplicationContext())
                .create(ApiService.class);

        Log.i(TAG, "parenetSignin");
        storeDeviceToken(MyApplication.getMyApplicationContext(), email);
        apiService.login(email, password, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserResponseModel>() {
                    @Override
                    public void onSuccess(UserResponseModel userResponseModel) {
                        if (userResponseModel.getLogin_as() != null) {
                            if (userResponseModel.getLogin_as().equalsIgnoreCase("admin") || userResponseModel.getLogin_as().equalsIgnoreCase("security")) {
                                listener.onError(ADMIN_LOGIN_ERROR);
                                return;
                            }
                        }
                        if (userResponseModel.getErrors() != null) {
                            listener.onError(userResponseModel.getErrors());
                            Log.i(TAG, "Error " + userResponseModel.getErrors());
                        } else if (userResponseModel.getEmail() != null) {
                            Log.i(TAG, "Success " + userResponseModel.getEmail());
                            PrefUtils.storeApiKey(getMyApplicationContext(), userResponseModel.getToken());
//                            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
                            UserSettingsPreference.saveUserProfile(getMyApplicationContext(), userResponseModel);
                            listener.onSuccess(userResponseModel.getStatus());
                        }
                        Log.i(TAG, "Success ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(SERVER_ERROR);
                        Log.i(TAG, "onError " + e.getMessage().toString());
                    }
                });
    }


}
