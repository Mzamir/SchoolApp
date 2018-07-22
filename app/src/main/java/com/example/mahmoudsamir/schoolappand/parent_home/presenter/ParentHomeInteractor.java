package com.example.mahmoudsamir.schoolappand.parent_home.presenter;

import com.example.mahmoudsamir.schoolappand.MyApplication;
import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.response.UserSchoolsResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.SERVER_ERROR;

public class ParentHomeInteractor {

    public interface OnGettingParentSchoolFinishedListener {
        void onSuccess(ArrayList<UserSchoolsResponse> userSchoolsResponses);

        void onError(String error);
    }

    public void getParentSchools(final OnGettingParentSchoolFinishedListener listener) {
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);

        apiService.getUserSchools()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ArrayList<UserSchoolsResponse>>() {
                    @Override
                    public void onSuccess(ArrayList<UserSchoolsResponse> userSchoolsResponses) {
                        if (userSchoolsResponses == null || userSchoolsResponses.size() == 0) {
                            listener.onError("Unauthenticated");
                        } else {
                            listener.onSuccess(userSchoolsResponses);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(SERVER_ERROR);
                    }
                });
    }
}
