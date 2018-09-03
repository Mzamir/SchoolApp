package com.seamlabs.BlueRide.parent_flow.waiting_student.presenter;

import android.util.Log;
import android.widget.BaseAdapter;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;
import com.seamlabs.BlueRide.network.requests.UpdateLocationRequestModel;
import com.seamlabs.BlueRide.network.response.CheckRequestStateResponseModel;
import com.seamlabs.BlueRide.network.response.MentorQueueResponseModel;
import com.seamlabs.BlueRide.network.response.ParentArrivedResponseModel;
import com.seamlabs.BlueRide.network.response.UpdateLocationResponseModel;
import com.seamlabs.BlueRide.parent_flow.pick_up.presenter.ParentPickUpInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.seamlabs.BlueRide.utils.Constants.BASE_URL;
import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;

public class ParentWaitingInteractor {
    String TAG = ParentWaitingInteractor.class.getSimpleName();

    public interface OnParentWaitingInteractorListener {
        void onSuccessReport(String successMessage);

        void onSuccessReceived(String successMessage);

        void onErrorReport(String errorMessage);

        void onErrorReceived(String errorMessage);

        void onSuccessCheckingRequestState(CheckRequestStateResponseModel responseModel);

        void onErrorCheckingRequestState(String errorMessage);
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
                            Log.i(TAG, "onSuccessParentArrived " + errorMessage);
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
                            Log.i(TAG, "onSuccessParentArrived " + errorMessage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onErrorReceived(GENERAL_ERROR);
                    }
                });
    }

    void checkIfCanReceive(int request_id, final OnParentWaitingInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.checkIfCanReceive(BASE_URL + "requests/" + request_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<CheckRequestStateResponseModel>() {
                    @Override
                    public void onSuccess(CheckRequestStateResponseModel response) {
                        if (response != null) {
                            if (response.getErrors() == null && response.getMessage() == null) {
                                listener.onSuccessCheckingRequestState(response);
                            } else {
                                String errorMessage = GENERAL_ERROR;
                                if (response.getMessage() != null) {
                                    errorMessage = response.getMessage();
                                } else if (response.getErrors() != null) {
                                    errorMessage = response.getErrors();
                                }
                                listener.onErrorCheckingRequestState(errorMessage);
                                Log.i(TAG, "onSuccessParentArrived " + errorMessage);
                            }
                        } else {
                            listener.onErrorCheckingRequestState(GENERAL_ERROR);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onErrorCheckingRequestState(GENERAL_ERROR);
                    }
                });
    }

    public void updateLocation(double lat, double longitude, final OnParentWaitingInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);
        UpdateLocationRequestModel locationRequestModel = new UpdateLocationRequestModel(lat, longitude);

        apiService.updateLocation(locationRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response mainResponse) {
                        if (mainResponse.isSuccessful()) {
                            BaseResponse response = (BaseResponse) mainResponse.body();
                            if (response.getMessage() == null && response.getErrors() == null) {
                                Log.i(TAG, "updateLocation successfully");
                                return;
                            } else {
                                Log.i(TAG, "updateLocation error");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "updateLocation exception " + e.getMessage().toString());
                    }
                });
    }
}
