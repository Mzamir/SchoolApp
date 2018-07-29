package com.example.mahmoudsamir.schoolappand.database_mangament;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.REALM_DATABASE_VERSION;

public class RealmObject {

    public static RealmConfiguration realmConfiguration;

    public static Realm getRealmObject(Context context) {
        Realm realm;
        Realm.init(context);
        realmConfiguration = new RealmConfiguration.Builder().schemaVersion(REALM_DATABASE_VERSION).deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(realmConfiguration);
        return realm;
    }
}
