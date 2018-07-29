package com.example.mahmoudsamir.schoolappand;

import android.app.Application;
import android.content.Context;
import android.support.design.widget.Snackbar;

import com.facebook.drawee.backends.pipeline.Fresco;

import io.realm.Realm;

//import io.realm.Realm;

public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        context = getApplicationContext();
        Realm.init(this);
    }

    public static Context getMyApplicationContext() {
        return context;
    }


}
