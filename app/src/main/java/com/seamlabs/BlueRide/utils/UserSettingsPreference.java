package com.seamlabs.BlueRide.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.parent_flow.profile.model.UserProfileModel;
import com.google.gson.Gson;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.PARENT_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.SHARED_USER_LOGGING_STATE;
import static com.seamlabs.BlueRide.utils.Constants.SHARED_USER_SETTING;
import static com.seamlabs.BlueRide.utils.Constants.SHARED_USER_TYPE;

public class UserSettingsPreference {

    private static final String SHARED_USER_PROFILE = "userProfile";

    public static SharedPreferences getUserSettingsSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_USER_SETTING, Context.MODE_PRIVATE);
    }

    public static void updateLoginState(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getUserSettingsSharedPreferences(context).edit();
        editor.putBoolean(SHARED_USER_LOGGING_STATE, loggedIn);
        editor.commit();
    }

    public static boolean getLoginState(Context context) {
        return getUserSettingsSharedPreferences(context).getBoolean(SHARED_USER_LOGGING_STATE, false);
    }

    public static void setUserType(Context context, String userType) {
        SharedPreferences.Editor editor = getUserSettingsSharedPreferences(context).edit();
        editor.putString(SHARED_USER_TYPE, userType);
        editor.commit();
    }

    public static String getUserType(Context context) {
        return getUserSettingsSharedPreferences(context).getString(SHARED_USER_TYPE, PARENT_USER_TYPE);
    }

    public static void saveUserProfile(Context context, UserResponseModel userProfileModel) {
        try {
            SharedPreferences.Editor editor = getUserSettingsSharedPreferences(context).edit();
            Gson gson = new Gson();
            String json = gson.toJson(userProfileModel);
            editor.putString(SHARED_USER_PROFILE, json);
            editor.commit();
            UserSettingsPreference.setUserType(getMyApplicationContext(), userProfileModel.getRoles().get(0).getName());
        } catch (Exception e) {

        }
    }

    public static UserResponseModel getSavedUserProfile(Context context) {
        try {
            Gson gson = new Gson();
            String json = getUserSettingsSharedPreferences(context).getString(SHARED_USER_PROFILE, null);
            UserResponseModel userProfileModel = gson.fromJson(json, UserResponseModel.class);
            return userProfileModel;
        } catch (Exception e) {
            return null;
        }
    }
}
