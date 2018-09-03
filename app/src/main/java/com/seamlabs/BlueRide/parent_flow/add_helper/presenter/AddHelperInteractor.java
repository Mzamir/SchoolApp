package com.seamlabs.BlueRide.parent_flow.add_helper.presenter;

import android.util.Log;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.SERVER_ERROR;

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
                .subscribe(new DisposableSingleObserver<Response>() {
                    @Override
                    public void onSuccess(Response mainResponse) {
                        if (mainResponse.isSuccessful()) {
                            BaseResponse response = (BaseResponse) mainResponse.body();
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
                        } else {
                            if (mainResponse.code() == 409) {
                                try {
                                    JSONObject jObjError = new JSONObject(mainResponse.errorBody().string());
                                    if (jObjError.getString("errors") != null) {
                                        if (!jObjError.getString("errors").isEmpty())
                                            listener.onError(jObjError.getString("errors"));
                                    } else if (jObjError.getString("message") != null) {
                                        if (!jObjError.getString("message").isEmpty())
                                            listener.onError(jObjError.getString("message"));
                                    }
                                } catch (Exception e) {
                                    listener.onError(SERVER_ERROR);
                                }
                            }
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
