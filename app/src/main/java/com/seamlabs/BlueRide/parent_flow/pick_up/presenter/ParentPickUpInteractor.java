package com.seamlabs.BlueRide.parent_flow.pick_up.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;
import com.seamlabs.BlueRide.network.requests.ParentPickUpRequestModel;
import com.seamlabs.BlueRide.network.requests.UpdateLocationRequestModel;
import com.seamlabs.BlueRide.network.response.ParentArrivedResponseModel;
import com.seamlabs.BlueRide.network.response.ParentPickUpResponseModel;
import com.seamlabs.BlueRide.network.response.UpdateLocationResponseModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;

public class ParentPickUpInteractor {

    String TAG = ParentPickUpInteractor.class.getSimpleName();


    public interface onPickerArrivedListener {
        void onSuccess(ParentArrivedResponseModel parentArrivedResponseModel);

        void onSuccessPickUpRequest(ParentPickUpResponseModel responseModel);

        void onError(String errorMessage);

        void onSuccessCancelingRequest();
    }

    public void parent_arrived(int request_id, final onPickerArrivedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.parentArrived(request_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<ParentArrivedResponseModel>() {
                    @Override
                    public void onSuccess(ParentArrivedResponseModel response) {
                        if (response.getMessage() == null && response.getErrors() == null)
                            listener.onSuccess(response);
                        else {
                            listener.onError(GENERAL_ERROR);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(SERVER_ERROR);
                        Log.i(TAG, "onError " + e.getMessage());
                    }
                });
    }

    public void parentPickUpRequest(ParentPickUpRequestModel requestModel, final ParentPickUpInteractor.onPickerArrivedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.parentPickUpRequest(requestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ParentPickUpResponseModel>() {
                    @Override
                    public void onSuccess(ParentPickUpResponseModel parentSchoolsResponse) {
                        if (parentSchoolsResponse == null) {
                            listener.onError(GENERAL_ERROR);
                        } else {
                            if (parentSchoolsResponse.getMessage() != null) {
                                listener.onError(GENERAL_ERROR);
                            } else {
                                listener.onSuccessPickUpRequest(parentSchoolsResponse);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ParentHomeInteractor", "Exception" + e.getMessage());
                        listener.onError(SERVER_ERROR);
                    }
                });
    }

    public void cancelPickUpRequest(int request_id, final onPickerArrivedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);
        apiService.cancelPickUpRequest(request_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (baseResponse.getSuccess() != null)
                            listener.onSuccessCancelingRequest();
                        else {
                            if (baseResponse.getErrors() != null) {
                                listener.onError(baseResponse.getErrors());
                                return;
                            }
                            if (baseResponse.getMessage() != null) {
                                listener.onError(baseResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ParentHomeInteractor", "Exception" + e.getMessage());
                        listener.onError(SERVER_ERROR);
                    }
                });
    }



}
