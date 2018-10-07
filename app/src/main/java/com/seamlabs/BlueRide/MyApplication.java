package com.seamlabs.BlueRide;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.pushnotifications.PushNotificationsInstance;
import com.seamlabs.BlueRide.utils.LocaleUtils;
import com.seamlabs.BlueRide.utils.PrefUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.pusher.pushnotifications.PushNotifications;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;

import java.util.Locale;

import io.realm.Realm;

import static com.seamlabs.BlueRide.utils.Constants.ENGLISH;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_CLUSTER;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_ID;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_KEY;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_BEAMS_INSTANCE_ID;


public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        context = getApplicationContext();

        PushNotifications.start(getApplicationContext(), PUSHER_BEAMS_INSTANCE_ID);

        LocaleUtils.setLocale(new Locale(UserSettingsPreference.getUserLanguage(this)));
        LocaleUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleUtils.updateConfig(this, newConfig);
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
