package com.example.mahmoudsamir.schoolappand.parent_flow.add_helper.presenter;

import android.util.Log;

import com.example.mahmoudsamir.schoolappand.MyApplication;
import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.BaseResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.BASE_URL;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.ERROR;

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
                            listener.onError("Enter a valid phone number.");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(ERROR);
                        Log.i(TAG, "onError " + e.getMessage());
                    }
                });
    }
}
