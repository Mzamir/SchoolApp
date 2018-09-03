package com.seamlabs.BlueRide.helper_account.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.requests.UserRequestModel;
import com.seamlabs.BlueRide.network.response.ErrorResponseModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.utils.PrefUtils;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

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
                        if (responseModel.getRoles().size() == 1) {
                            if (responseModel.getRoles().get(0).getName().equalsIgnoreCase("admin") || responseModel.getRoles().get(0).getName().equalsIgnoreCase("security")) {
                                listener.onError(ADMIN_LOGIN_ERROR);
                                return;
                            }
                        }
                        if (responseModel.getErrors() != null) {
                            listener.onError(responseModel.getErrors());
                            Log.i(TAG, "Error " + responseModel.getErrors());
                            return;
                        }
                        if (responseModel != null) {
                            PrefUtils.storeApiKey(getMyApplicationContext(), responseModel.getToken());
//                            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
                            UserSettingsPreference.saveUserProfile(getMyApplicationContext(), responseModel);
                            listener.onSuccess(responseModel.getStatus());
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
        apiService.signupHelper(name, email, password, national_id, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response response) {
                        if (response.isSuccessful()) {
                            UserResponseModel responseModel = (UserResponseModel) response.body();
                            if (responseModel.getRoles().size() == 1) {
                                if (responseModel.getRoles().get(0).getName().equalsIgnoreCase("admin") || responseModel.getRoles().get(0).getName().equalsIgnoreCase("security")) {
                                    listener.onError(ADMIN_LOGIN_ERROR);
                                    return;
                                }
                            }
                            if (responseModel.getErrors() != null) {
                                listener.onError(responseModel.getErrors());
                                Log.i(TAG, "Error " + responseModel.getErrors());
                                return;
                            }
                            if (responseModel != null) {
                                PrefUtils.storeApiKey(getMyApplicationContext(), responseModel.getToken());
                                UserSettingsPreference.saveUserProfile(getMyApplicationContext(), responseModel);
                                listener.onSuccess(responseModel.getStatus());
                            }
                        } else if (response.code() == 400) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if (jObjError.getString("errors") != null) {
                                    if (!jObjError.getString("errors").isEmpty())
                                        listener.onError(jObjError.getString("errors"));
                                } else if (jObjError.getString("message") != null) {
                                    if (!jObjError.getString("message").isEmpty())
                                        listener.onError(jObjError.getString("message"));
                                }
                            } catch (Exception e) {
                                listener.onError(SERVER_ERROR);
                            }
                        } else if (response.code() == 422) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Gson gson = new Gson();
                                ErrorResponseModel responseModel = gson.fromJson(jObjError.getString("errors"), ErrorResponseModel.class);
                                if (responseModel.getEmail().size() > 0) {
                                    listener.onError(responseModel.getEmail().get(0));
                                } else if (responseModel.getNational_id().size() > 0) {
                                    listener.onError(responseModel.getNational_id().get(0));
                                } else if (responseModel.getPassword().size() > 0) {
                                    listener.onError(responseModel.getPassword().get(0));
                                } else if (responseModel.getPhone().size() > 0) {
                                    listener.onError(responseModel.getPhone().get(0));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                listener.onError(SERVER_ERROR);
                            }

                        } else {
                            listener.onError(SERVER_ERROR);
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
