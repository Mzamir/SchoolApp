package com.seamlabs.BlueRide.notification;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyInstanceIDListenerService extends FirebaseMessagingService {
    String TAG = "InstanceIDListenerService";


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}