package com.example.mahmoudsamir.schoolappand;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.mahmoudsamir.schoolappand.utils.PrefUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.pusher.pushnotifications.PushNotifications;

import io.realm.Realm;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.PUSHER_API_KEY;


public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        context = getApplicationContext();
        Realm.init(this);

        PushNotifications.start(getApplicationContext(), PUSHER_API_KEY);
        PushNotifications.subscribe("mentor");
        if (PrefUtils.getApiKey(this) != null) {
            Log.i("TOKEN ", PrefUtils.getApiKey(this));
        }
    }


    public static Context getMyApplicationContext() {
        return context;
    }


}
