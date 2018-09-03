package com.seamlabs.BlueRide.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.R;

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
            progressDialog = new ProgressDialog(context, R.style.progressDialog);
            progressDialog.setCancelable(false); // disable dismiss by tapping outside of the dialog
            if (!progressDialog.isShowing()){
                progressDialog.show();
            }
        } catch (Exception e) {
            Log.i("Utility", "Dialog Exception " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void hideProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
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
}
