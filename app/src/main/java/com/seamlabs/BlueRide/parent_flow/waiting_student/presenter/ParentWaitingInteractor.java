package com.seamlabs.BlueRide.parent_flow.waiting_student.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;
import com.seamlabs.BlueRide.network.response.ParentArrivedResponseModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;

public class ParentWaitingInteractor {
    String TAG = ParentWaitingInteractor.class.getSimpleName();

    public interface OnParentWaitingInteractorListener {
        void onSuccessReport(String successMessage);

        void onSuccessReceived(String successMessage);

        void onErrorReport(String errorMessage);

        void onErrorReceived(String errorMessage);
    }

    void report(final int request_id, final OnParentWaitingInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.report(request_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ParentArrivedResponseModel>() {
                    @Override
                    public void onSuccess(ParentArrivedResponseModel response) {
                        if (response.getMessage() == null && response.getErrors() == null) {
                            listener.onSuccessReport("Request Reported successfully");
                        } else {
                            String errorMessage = GENERAL_ERROR;
                            if (response.getMessage() != null) {
                                errorMessage = response.getMessage();
                            } else if (response.getErrors() != null) {
                                errorMessage = response.getErrors();
                            }
                            listener.onErrorReceived(errorMessage);
                            Log.i(TAG, "onSuccess " + errorMessage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onErrorReport(GENERAL_ERROR);
                        Log.i(TAG, "onError " + e.getMessage());
                    }
                });

    }

    void delivered(int request_id, final OnParentWaitingInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.delivered(request_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getErrors() == null && response.getMessage() == null) {
                            listener.onSuccessReceived("Students Received successfully");
                        } else {
                            String errorMessage = GENERAL_ERROR;
                            if (response.getMessage() != null) {
                                errorMessage = response.getMessage();
                            } else if (response.getErrors() != null) {
                                errorMessage = response.getErrors();
                            }
                            listener.onErrorReceived(errorMessage);
                            Log.i(TAG, "onSuccess " + errorMessage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onErrorReceived(GENERAL_ERROR);
                    }
                });
    }

}
