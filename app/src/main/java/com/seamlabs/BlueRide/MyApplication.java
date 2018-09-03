package com.seamlabs.BlueRide;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.seamlabs.BlueRide.utils.PrefUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.pusher.pushnotifications.PushNotifications;

import io.realm.Realm;

import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_CLUSTER;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_ID;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_KEY;


public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        context = getApplicationContext();
        Realm.init(this);

//        PusherOptions options = new PusherOptions().setCluster(PUSHER_API_CLUSTER);
//        options.setCluster(PUSHER_API_CLUSTER);
//        Pusher pusher = new Pusher(PUSHER_API_KEY, options);
//        pusher.connect();
//
//        PushNotifications.start(getApplicationContext(), PUSHER_API_ID);
//        PushNotifications.subscribe("mentor");
        if (PrefUtils.getApiKey(this) != null) {
            Log.i("TOKEN ", PrefUtils.getApiKey(this));
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getMyApplicationContext() {
        return context;
    }


}
