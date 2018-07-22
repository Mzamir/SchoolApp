package com.example.mahmoudsamir.schoolappand.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.PARENT_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.SHARED_USER_LOGGING_STATE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.SHARED_USER_SETTING;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.SHARED_USER_TYPE;

public class UserSettingsPreference {

    private static SharedPreferences getUserSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_USER_SETTING, Context.MODE_PRIVATE);
    }

    public static void updateLoginState(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getUserSharedPreferences(context).edit();
        editor.putBoolean(SHARED_USER_LOGGING_STATE, loggedIn);
        editor.commit();
    }

    public static boolean getLoginState(Context context) {
        return getUserSharedPreferences(context).getBoolean(SHARED_USER_LOGGING_STATE, false);
    }

    public static void setUserType(Context context, String userType) {
        SharedPreferences.Editor editor = getUserSharedPreferences(context).edit();
        editor.putString(SHARED_USER_TYPE, userType);
        editor.commit();
    }

    public static String getUserType(Context context) {
        return getUserSharedPreferences(context).getString(SHARED_USER_TYPE, PARENT_USER_TYPE);
    }

}
