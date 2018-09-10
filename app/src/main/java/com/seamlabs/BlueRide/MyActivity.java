package com.seamlabs.BlueRide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.seamlabs.BlueRide.utils.LocaleUtils;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;

import static com.seamlabs.BlueRide.utils.Constants.ARABIC;
import static com.seamlabs.BlueRide.utils.Constants.ENGLISH;
import static com.seamlabs.BlueRide.utils.UserSettingsPreference.getUserLanguage;
import static com.seamlabs.BlueRide.utils.Utility.hideSoftKeyboard;

public class MyActivity extends AppCompatActivity {

    public MyActivity() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        adjustLanguage();
    }

    protected void adjustLanguage() {  // This method will be called when layout need to be localized

        if (getUserLanguage(this) != null)
            Utility.setLanguage(MyActivity.this, ENGLISH);

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            setupUI(findViewById(R.id.parent_layout));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MyActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

}
