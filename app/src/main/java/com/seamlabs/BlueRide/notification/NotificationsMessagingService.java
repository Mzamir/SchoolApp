package com.seamlabs.BlueRide.notification;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.pusher.pushnotifications.fcm.MessagingService;
import com.seamlabs.BlueRide.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import static com.seamlabs.BlueRide.utils.Constants.EVENT_NOTIFICATION_RECEIVED;
import static com.seamlabs.BlueRide.utils.Constants.EVENT_PICTURE_CHANGED;

public class NotificationsMessagingService extends MessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("NotificationsService", "Got a remote message " + remoteMessage.getNotification().getBody());
        EventBus.getDefault().post(new MessageEvent(EVENT_NOTIFICATION_RECEIVED));
    }

    @NotNull
    @Override
    public IBinder onBind(Intent i) {
        Log.i("NotificationsService", "OnBind");
        return super.onBind(i);
    }

}
