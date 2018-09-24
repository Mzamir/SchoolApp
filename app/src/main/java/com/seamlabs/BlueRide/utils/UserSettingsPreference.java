package com.seamlabs.BlueRide.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.seamlabs.BlueRide.mentor_home.model.MentorStudentModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.ENGLISH;
import static com.seamlabs.BlueRide.utils.Constants.PARENT_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.SHARED_PENDING_STUDENTS;
import static com.seamlabs.BlueRide.utils.Constants.SHARED_PENDING_STUDENTS_LIST;
import static com.seamlabs.BlueRide.utils.Constants.SHARED_USER_LANGUAGE;
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
        String userType = getUserSettingsSharedPreferences(context).getString(SHARED_USER_TYPE, PARENT_USER_TYPE);
        Log.e(SHARED_USER_PROFILE, "userType " + userType);
        return userType;
    }

    public static void setUserLanguage(Context context, String userType) {
        SharedPreferences.Editor editor = getUserSettingsSharedPreferences(context).edit();
        editor.putString(SHARED_USER_LANGUAGE, userType);
        editor.commit();
    }

    public static String getUserLanguage(Context context) {
        return getUserSettingsSharedPreferences(context).getString(SHARED_USER_LANGUAGE, ENGLISH);
    }

    public static void saveUserProfile(Context context, UserResponseModel userProfileModel) {
        try {
            SharedPreferences.Editor editor = getUserSettingsSharedPreferences(context).edit();
            Gson gson = new Gson();
            String json = gson.toJson(userProfileModel);
            editor.putString(SHARED_USER_PROFILE, json);
            editor.commit();
            UserSettingsPreference.setUserType(getMyApplicationContext(), userProfileModel.getLogin_as());
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

    public static ArrayList<MentorStudentModel> getPendingStudentsFromShared() {
        Gson gson = new Gson();
        List<MentorStudentModel> tempList;
        SharedPreferences prefs = getMyApplicationContext().getSharedPreferences(SHARED_PENDING_STUDENTS, Context.MODE_PRIVATE);
        String listJson = prefs.getString(SHARED_PENDING_STUDENTS_LIST, "");
        Type type = new TypeToken<List<MentorStudentModel>>() {
        }.getType();
        tempList = gson.fromJson(listJson, type);
        if (tempList != null) {
            if (!tempList.isEmpty()) {
                ArrayList<MentorStudentModel> list = new ArrayList<>(tempList);
                return list;
            }
        }
        return null;
    }

    public static void savePendingStudentsToShare(ArrayList<MentorStudentModel> list) {
        SharedPreferences sharedPreferences = getMyApplicationContext().getSharedPreferences(SHARED_PENDING_STUDENTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(list);
        editor.putString(SHARED_PENDING_STUDENTS_LIST, json);
        editor.commit();
    }
}
