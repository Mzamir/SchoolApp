package com.seamlabs.BlueRide.notification.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seamlabs.BlueRide.MyFragment;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.response.NotificationResponseModel;
import com.seamlabs.BlueRide.notification.presenter.NotificationInteractor;
import com.seamlabs.BlueRide.notification.presenter.NotificationPresenter;
import com.seamlabs.BlueRide.utils.Utility;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class NotificationFragment extends MyFragment implements NotificationViewCommunicator {

    Activity activity ;
    NotificationPresenter presenter ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        presenter = new NotificationPresenter(this , new NotificationInteractor()) ;
        return view;
    }

    @Override
    public void showProgress() {
        Utility.showProgressDialog(activity);
    }

    @Override
    public void hideProgress() {
        Utility.hideProgressDialog();
    }

    @Override
    public void onErrorGettingNotifications(String errorMessage) {

    }

    @Override
    public void onSuccessGetttingNotifications(ArrayList<NotificationResponseModel> notificationsList) {

    }
}
