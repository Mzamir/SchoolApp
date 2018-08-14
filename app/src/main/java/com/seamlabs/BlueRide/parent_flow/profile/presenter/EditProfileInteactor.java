package com.seamlabs.BlueRide.parent_flow.profile.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.requests.EditProfileRequestModel;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EditProfileInteactor {
    String TAG = ParentProfileInteactor.class.getSimpleName();

    public interface OnEditProfileInteractorListener {
        void onSuccessEditProfile(UserProfileResponseModel userProfileResponseModel);

        void onErrorGettingEditProfile();

    }

    public void editProfile(EditProfileRequestModel editProfileRequestModel, final OnEditProfileInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.editProfile(editProfileRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserProfileResponseModel>() {
                    @Override
                    public void onSuccess(UserProfileResponseModel userProfileResponseModel) {
                        if (userProfileResponseModel.getMessage() == null) {
                            listener.onSuccessEditProfile(userProfileResponseModel);
                        } else {
                            Log.i(TAG, "onSuccess exception");
                            listener.onErrorGettingEditProfile();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "OnError exception " + e.getMessage());
                        listener.onErrorGettingEditProfile();
                    }
                });
    }
}
