package com.seamlabs.BlueRide.parent_flow.account.presenter;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.seamlabs.BlueRide.parent_flow.account.view.ParentRegistrationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.seamlabs.BlueRide.utils.Constants.BASE_URL;

public class ParentSignupPresenter implements ParentRegistrationInteractor.OnParentSignInFinishedListener {

    private ParentRegistrationView view;
    private ParentRegistrationInteractor interactor;

    public ParentSignupPresenter(ParentRegistrationView view, ParentRegistrationInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void validateCredentials(String national_id, String password) {
        if (view != null) {
            view.showProgress();
        }
        interactor.parenetSignup(national_id, password, this);
    }

    @Override
    public void onError(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onErrorRegistration(errorMessage);
        }
    }

    @Override
    public void onSuccess(int status) {
        if (view != null) {
            view.navigateToParentHome(status);
        }
    }


}
