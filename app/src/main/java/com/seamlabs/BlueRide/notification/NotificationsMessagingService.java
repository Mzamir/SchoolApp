package com.seamlabs.BlueRide.notification;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.pusher.pushnotifications.fcm.MessagingService;

import org.jetbrains.annotations.NotNull;

public class NotificationsMessagingService extends MessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("NotificationsService", "Got a remote message " + remoteMessage.getNotification().getBody());
    }

    @NotNull
    @Override
    public IBinder onBind(Intent i) {
        Log.i("NotificationsService", "OnBind");
        return super.onBind(i);
    }
}
