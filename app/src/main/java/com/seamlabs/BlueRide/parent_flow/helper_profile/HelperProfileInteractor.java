package com.seamlabs.BlueRide.parent_flow.helper_profile;

import android.util.Log;

import com.google.gson.Gson;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;
import com.seamlabs.BlueRide.network.requests.AssignStudentsToHelperRequestModel;
import com.seamlabs.BlueRide.network.response.HelperProfileResponseModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.seamlabs.BlueRide.utils.Constants.BASE_URL;
import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;

public class HelperProfileInteractor {
    String TAG = HelperProfileInteractor.class.getSimpleName();


    public interface onEditChangeHelperStatesListener {

        void onSuccessChangeHelperState();

        void onErrorChangeHelperState(String errorMessage);

        void onSuccessAssigningHelper();

        void onErrorAssigningHelper(String errorMessage);

        void onSuccessGettingHelperProfile(HelperProfileResponseModel helperProfileResponse);

        void onErrorGettingHelperProfile(String message);

    }

    public void changeHelperState(int helperID, final onEditChangeHelperStatesListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.changeHelperState(helperID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getMessage() == null && response.getErrors() == null)
                            listener.onSuccessChangeHelperState();
                        Log.i(TAG, "onSuccessParentArrived Exception " + new Gson().toJson(response).toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i(TAG, "onError Exception " + e.getMessage());
                        listener.onErrorChangeHelperState(GENERAL_ERROR);
                    }
                });
    }

    public void assignHelperToStudents(AssignStudentsToHelperRequestModel requestModel, final onEditChangeHelperStatesListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.assignStudentsToHelper(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response.getMessage() == null && response.getErrors() == null)
                            listener.onSuccessAssigningHelper();
                        Log.i(TAG, "onSuccessParentArrived Exception " + new Gson().toJson(response).toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i(TAG, "onError Exception " + e.getMessage());
                        listener.onErrorAssigningHelper(GENERAL_ERROR);
                    }
                });
    }

    public void getProfileHelper(int helper_id, final onEditChangeHelperStatesListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.getHelperProfile(BASE_URL + "helper_profile/" + helper_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSingleObserver<HelperProfileResponseModel>() {
                    @Override
                    public void onSuccess(HelperProfileResponseModel response) {
                        if (response.getMessage() == null && response.getErrors() == null)
                            listener.onSuccessGettingHelperProfile(response);
                        else if (response.getErrors() != null) {
                            listener.onErrorGettingHelperProfile(response.getErrors());
                        }
                        Log.i(TAG, "onSuccessParentArrived Exception " + new Gson().toJson(response).toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.i(TAG, "onError Exception " + e.getMessage());
                        listener.onErrorGettingHelperProfile(GENERAL_ERROR);
                    }
                });
    }
}
