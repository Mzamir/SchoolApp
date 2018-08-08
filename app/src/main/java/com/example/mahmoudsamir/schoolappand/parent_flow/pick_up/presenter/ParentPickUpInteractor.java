package com.example.mahmoudsamir.schoolappand.parent_flow.pick_up.presenter;

import android.util.Log;

import com.example.mahmoudsamir.schoolappand.MyApplication;
import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.response.ParentArrivedResponseModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ParentPickUpInteractor {

    String TAG = ParentPickUpInteractor.class.getSimpleName();

    public interface onPickerArrivedListener {
        void onSuccess(ParentArrivedResponseModel parentArrivedResponseModel);

        void onError();
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
                            listener.onError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                        Log.i(TAG, "onError " + e.getMessage());
                    }
                });
    }
}
