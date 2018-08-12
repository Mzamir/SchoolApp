package com.seamlabs.BlueRide.parent_flow.profile.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ParentProfileInteactor {

    String TAG = ParentProfileInteactor.class.getSimpleName();

    public interface OnParentInteractorListener {
        void onSuccessGettingUserProfile(UserProfileResponseModel userProfileResponseModel);

        void onErrorGettingUserProfile();

    }

    public void getUserProfile(final OnParentInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.getUserProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<UserProfileResponseModel>() {
                    @Override
                    public void onSuccess(UserProfileResponseModel userProfileResponseModel) {
                        if (userProfileResponseModel.getMessage() == null) {
                            listener.onSuccessGettingUserProfile(userProfileResponseModel);
                        } else {
                            Log.i(TAG, "onSuccess exception");
                            listener.onErrorGettingUserProfile();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "OnError exception " + e.getMessage());
                        listener.onErrorGettingUserProfile();
                    }
                });
    }
}
