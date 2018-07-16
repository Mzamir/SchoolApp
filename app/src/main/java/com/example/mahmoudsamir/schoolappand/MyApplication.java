package com.example.mahmoudsamir.schoolappand;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getMyApplicationContext() {
        return context;
    }

}
