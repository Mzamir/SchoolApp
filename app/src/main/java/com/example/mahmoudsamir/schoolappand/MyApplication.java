package com.example.mahmoudsamir.schoolappand;

import android.app.Application;
import android.content.Context;

//import io.realm.Realm;

public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        Realm.init(this);
    }

    public static Context getMyApplicationContext() {
        return context;
    }

}
