package com.seamlabs.BlueRide.notification.presenter;

import com.seamlabs.BlueRide.network.response.NotificationResponseModel;
import com.seamlabs.BlueRide.notification.view.NotificationViewCommunicator;

import java.util.ArrayList;

public class NotificationPresenter implements NotificationInteractor.NotificationInteractorListener {
    NotificationViewCommunicator view;
    NotificationInteractor interactor;

    public NotificationPresenter(NotificationViewCommunicator view, NotificationInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void getNotifications() {
        if (view != null)
            view.showProgress();
        interactor.getNotifications(this);
    }

    @Override
    public void onErrorGettingNotifications(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.onErrorGettingNotifications(errorMessage);
        }
    }

    @Override
    public void onSuccessGetttingNotifications(ArrayList<NotificationResponseModel> notificationsList) {
        if (view != null) {
            view.hideProgress();
            view.onSuccessGetttingNotifications(notificationsList);
        }
    }
}
