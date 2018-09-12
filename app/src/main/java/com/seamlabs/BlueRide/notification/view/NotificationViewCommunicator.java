package com.seamlabs.BlueRide.notification.view;

import com.seamlabs.BlueRide.network.response.NotificationResponseModel;

import java.util.ArrayList;

public interface NotificationViewCommunicator {

    void showProgress();

    void hideProgress();

    void onErrorGettingNotifications(String errorMessage);

    void onSuccessGettingNotifications(ArrayList<NotificationResponseModel> notificationsList);
}
