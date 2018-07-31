package com.example.mahmoudsamir.schoolappand.parent_flow.waiting_student.presenter;

import com.example.mahmoudsamir.schoolappand.MyApplication;
import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.BaseResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.ERROR;

public class ParentWaitingInteractor {
    public interface OnParentWaitingInteractorListener {
        void onSuccessReport(String successMessage);

        void onSuccessReceived(String successMessage);

        void onErrorReport(String errorMessage);

        void onErrorReceived(String errorMessage);
    }

    void report(int request_id, final OnParentWaitingInteractorListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.report(request_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null)
                            if (response.getErrors() == null && response.getMessage() == null) {
                                listener.onSuccessReport(response.getSuccess());
                            } else {
                                listener.onErrorReport(response.getErrors());
                            }
                        else {
                            listener.onErrorReport(ERROR);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onErrorReport(ERROR);
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
                        if (response.getSuccess() != null) {
                            listener.onSuccessReceived(response.getSuccess());
                        } else {
                            listener.onErrorReceived(response.getErrors());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onErrorReceived(ERROR);
                    }
                });
    }

}
