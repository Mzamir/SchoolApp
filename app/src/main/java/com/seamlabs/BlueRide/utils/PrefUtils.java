package com.seamlabs.BlueRide.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
    /**
     * Storing API Key in shared preferences to
     * add it in header part of every retrofit request
     */
    public PrefUtils() {
    }

    public static SharedPreferences getPrefUtilsSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }

    public static void storeApiKey(Context context, String apiKey) {
        SharedPreferences.Editor editor = getPrefUtilsSharedPreferences(context).edit();
        editor.putString("API_KEY", apiKey);
        editor.commit();
    }

    public static String getApiKey(Context context) {
        return getPrefUtilsSharedPreferences(context).getString("API_KEY", null);
    }

    public static void storeDeviceToken(Context context, String apiKey) {
        SharedPreferences.Editor editor = getPrefUtilsSharedPreferences(context).edit();
        editor.putString("DEVICE_TOKEN", apiKey);
        editor.commit();
    }

    public static String getDeviceToken(Context context) {
        return getPrefUtilsSharedPreferences(context).getString("DEVICE_TOKEN", null);
    }
}
