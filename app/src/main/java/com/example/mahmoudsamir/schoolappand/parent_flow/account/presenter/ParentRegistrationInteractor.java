package com.example.mahmoudsamir.schoolappand.parent_flow.account.presenter;

import android.util.Log;

import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.response.UserResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.model.UserProfileModel;
import com.example.mahmoudsamir.schoolappand.utils.PrefUtils;
import com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.mahmoudsamir.schoolappand.MyApplication.getMyApplicationContext;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.HELPER_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.MENTOR_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.PARENT_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.SERVER_ERROR;

public class ParentRegistrationInteractor {

    String TAG = ParentRegistrationInteractor.class.getSimpleName();

    public interface OnParentSignInFinishedListener {
        void onError(String errorMessage);

        void onSuccess();
    }


    // here to implement the REST-API
    public void parenetSignup(final String national_id, final String password, final OnParentSignInFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(getMyApplicationContext())
                .create(ApiService.class);

        apiService.signUpParent(national_id, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserResponseModel>() {
                    @Override
                    public void onSuccess(UserResponseModel userResponseModel) {
                        if (userResponseModel.getErrors() != null) {
                            listener.onError(userResponseModel.getErrors());
                            Log.i(TAG, "Error " + userResponseModel.getErrors());
                        } else if (userResponseModel.getEmail() != null) {
                            Log.i(TAG, "Success " + userResponseModel.getEmail());
                            PrefUtils.storeApiKey(getMyApplicationContext(), userResponseModel.getToken());
                            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
                            UserSettingsPreference.saveUserProfile(getMyApplicationContext(), userResponseModel);
                            listener.onSuccess();
                        }
                        Log.i(TAG, "Success ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(SERVER_ERROR);
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
                        if (userResponseModel.getErrors() != null) {
                            listener.onError(userResponseModel.getErrors());
                            Log.i(TAG, "Error " + userResponseModel.getErrors());
                        } else if (userResponseModel.getEmail() != null) {
                            Log.i(TAG, "Success " + userResponseModel.getEmail());
                            PrefUtils.storeApiKey(getMyApplicationContext(), userResponseModel.getToken());
                            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
                            UserSettingsPreference.saveUserProfile(getMyApplicationContext(), userResponseModel);
                            listener.onSuccess();
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
        }
        return user;
    }

}
