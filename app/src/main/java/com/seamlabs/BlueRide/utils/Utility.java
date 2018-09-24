package com.seamlabs.BlueRide.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.pusher.pushnotifications.PushNotifications;
import com.pusher.pushnotifications.fcm.FCMInstanceIDService;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.R;

import java.util.Locale;

import static com.seamlabs.BlueRide.utils.Constants.ARABIC;
import static com.seamlabs.BlueRide.utils.UserSettingsPreference.getUserLanguage;

public class Utility {

    public static ProgressDialog progressDialog;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showProgressDialog(Context context) {

        try {
            if (progressDialog == null)
                progressDialog = new ProgressDialog(context, R.style.progressDialog);
            progressDialog.setCancelable(false); // disable dismiss by tapping outside of the dialog
            if (progressDialog.isShowing() == false) {
                progressDialog.show();
            }
        } catch (Exception e) {
            Log.i("Utility", "Dialog Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void hideProgressDialog() {
        try {
            if (progressDialog != null)
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
        } catch (Exception e) {
            Log.i("Utility", "Dialog Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String[] getCountryCodes() {
        String phoneCodesList[] = new String[2];
        phoneCodesList[0] = "+966";
        phoneCodesList[1] = "+2";
        return phoneCodesList;
    }

    public static void setLanguage(Context context, String language) {

        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language);
        res.updateConfiguration(conf, dm);
    }

    public static String localizeNumbers(String originalText) {
        try {
            if (getUserLanguage(MyApplication.getMyApplicationContext()).equals(ARABIC)) {
                char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < originalText.length(); i++) {
                    if (Character.isDigit(originalText.charAt(i))) {
                        builder.append(arabicChars[(int) (originalText.charAt(i)) - 48]);
                    } else {
                        builder.append(originalText.charAt(i));
                    }
                }
                return builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return originalText;
        }
        return originalText;
    }

    public static String getDeviceToken(Context context) {
        String androidId = "";
        try {
            androidId = androidId + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            PrefUtils.storeDeviceToken(context, androidId);
            Log.i("Utility", "DeviceToken " + androidId);
        } catch (Exception e) {
            Log.i("Utility", "DeviceToken exception " + e.getMessage().toString());
        }
        return androidId;
    }
}
