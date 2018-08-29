package com.seamlabs.BlueRide.parent_flow.profile.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.seamlabs.BlueRide.MessageEvent;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.requests.EditProfileRequestModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.EVENT_PICTURE_CHANGED;
import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;

public class EditProfileInteactor {
    String TAG = EditProfileInteactor.class.getSimpleName();

    public interface OnEditProfileInteractorListener {
        void onSuccessEditProfile(UserResponseModel userResponseModel);

        void onErrorGettingEditProfile(String errorMessage);

    }

    public void editProfile(EditProfileRequestModel editProfileRequestModel, final OnEditProfileInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        Log.i(TAG, "editProfile request " + new Gson().toJson(editProfileRequestModel));
        apiService.editProfile(editProfileRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserResponseModel>() {
                    @Override
                    public void onSuccess(UserResponseModel userResponseModel) {
                        if (userResponseModel != null) {
                            UserSettingsPreference.saveUserProfile(getMyApplicationContext(), userResponseModel);
                            EventBus.getDefault().post(new MessageEvent(EVENT_PICTURE_CHANGED));
                            listener.onSuccessEditProfile(userResponseModel);
                            return;
                        }
                        listener.onErrorGettingEditProfile(userResponseModel.getErrors());
                        Log.i(TAG, "onSuccessParentArrived exception");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "OnError exception " + e.getMessage());
                        listener.onErrorGettingEditProfile(SERVER_ERROR);
                    }
                });
    }

    public void editProfile(String image, final boolean showProgress, final OnEditProfileInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);
        apiService.editProfile(getImageRequestPart(image))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserResponseModel>() {
                    @Override
                    public void onSuccess(UserResponseModel userResponseModel) {
                        if (userResponseModel != null) {
                            UserSettingsPreference.saveUserProfile(getMyApplicationContext(), userResponseModel);
                            EventBus.getDefault().post(new MessageEvent(EVENT_PICTURE_CHANGED));
                            if (showProgress) {
                                listener.onSuccessEditProfile(userResponseModel);
                            }
                            return;
                        }
                        listener.onErrorGettingEditProfile(userResponseModel.getErrors());
                        Log.i(TAG, "onSuccessParentArrived exception");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "OnError exception " + e.getMessage());
                        listener.onErrorGettingEditProfile(SERVER_ERROR);
                        e.printStackTrace();
                    }
                });
    }

    private MultipartBody.Part getImageRequestPart(String filePath) {
        if (!filePath.isEmpty()) {
            //pass it like this
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            return body;
        }
        return null;
    }

}
