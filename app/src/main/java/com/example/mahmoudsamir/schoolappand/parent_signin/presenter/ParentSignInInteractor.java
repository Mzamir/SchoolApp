package com.example.mahmoudsamir.schoolappand.parent_signin.presenter;

import com.example.mahmoudsamir.schoolappand.network.ApiClient;
import com.example.mahmoudsamir.schoolappand.network.ApiService;
import com.example.mahmoudsamir.schoolappand.network.response.ParentSignInResponse;
import com.example.mahmoudsamir.schoolappand.parent_signin.model.ParentModel;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.mahmoudsamir.schoolappand.MyApplication.getMyApplicationContext;

public class ParentSignInInteractor {

    public interface OnParentSignInFinishedListener {
        void onError();
        void onSuccess();
    }


    // here to implement the REST-API
    public void login(final String national_id, final String password, final OnParentSignInFinishedListener listener) {
        ParentModel parentModel = new ParentModel(national_id, password);
        ApiService apiService = ApiClient.getClient(getMyApplicationContext())
                .create(ApiService.class);

        apiService.signUpParent(national_id, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ParentSignInResponse>() {
                    @Override
                    public void onSuccess(ParentSignInResponse parentSignInResponse) {
                        listener.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                    }
                });
    }
}
