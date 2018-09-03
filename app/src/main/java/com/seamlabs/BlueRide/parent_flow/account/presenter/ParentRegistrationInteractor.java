package com.seamlabs.BlueRide.parent_flow.account.presenter;

import android.util.Log;
import android.widget.Toast;

import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.parent_flow.profile.model.UserProfileModel;
import com.seamlabs.BlueRide.utils.PrefUtils;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.ADMIN_LOGIN_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.HELPER_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.MENTOR_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.PARENT_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.TEACHER_USER_TYPE;

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

        apiService.signUpParent(national_id, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response response) {
                        if (response.isSuccessful()) {
                            UserResponseModel userResponseModel = (UserResponseModel) response.body();
                            if (userResponseModel.getRoles().size() == 1) {
                                if (userResponseModel.getRoles().get(0).getName().equalsIgnoreCase("admin") || userResponseModel.getRoles().get(0).getName().equalsIgnoreCase("security")) {
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
        apiService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserResponseModel>() {
                    @Override
                    public void onSuccess(UserResponseModel userResponseModel) {
                        if (userResponseModel.getRoles().size() == 1) {
                            if (userResponseModel.getRoles().get(0).getName().equalsIgnoreCase("admin") || userResponseModel.getRoles().get(0).getName().equalsIgnoreCase("security")) {
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

    private UserProfileModel convertResponseToUserProfileObject(UserResponseModel loginResponse) {
        UserProfileModel user = new UserProfileModel();
        user.setId(loginResponse.getId());
        user.setName(loginResponse.getName());
        user.setEmail(loginResponse.getEmail());
        user.setNational_id(loginResponse.getNational_id());
        user.setPhone(loginResponse.getPhone());
        user.setCreated_at(loginResponse.getCreated_at());
        user.setUpdated_at(loginResponse.getUpdated_at());
        user.setAuthy_code(loginResponse.getAuthy_code());
        user.setToken(loginResponse.getToken());
        if (loginResponse.getRoles().get(0).getName().equals("parent")) {
            user.setRole(PARENT_USER_TYPE);
        } else if (loginResponse.getRoles().get(0).getName().equals("helper")) {
            user.setRole(HELPER_USER_TYPE);
        } else if (loginResponse.getRoles().get(0).getName().equals("mentor")) {
            user.setRole(MENTOR_USER_TYPE);
        } else if (loginResponse.getRoles().get(0).getName().equals("teacher")) {
            user.setRole(TEACHER_USER_TYPE);
        }
        return user;
    }

}
