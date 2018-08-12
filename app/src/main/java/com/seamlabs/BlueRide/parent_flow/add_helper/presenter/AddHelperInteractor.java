package com.seamlabs.BlueRide.parent_flow.add_helper.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;

public class AddHelperInteractor {
    String TAG = AddHelperInteractor.class.getSimpleName();

    public interface OnAddingHelperListener {
        void onError(String errorMessage);

        void onSuccess();
    }

    void addHelper(String phone, final OnAddingHelperListener listener) {

        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.addHelper(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getMessage() == null && response.getErrors() == null)
                            listener.onSuccess();
                        else {
                            String errorMessage = "Enter a valid phone number.";
                            if (response.getMessage() != null) {
                                errorMessage = response.getMessage();
                            } else if (response.getErrors() != null) {
                                errorMessage = response.getErrors();
                            }
                            listener.onError(errorMessage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(GENERAL_ERROR);
                        Log.i(TAG, "onError " + e.getMessage());
                    }
                });
    }
}
