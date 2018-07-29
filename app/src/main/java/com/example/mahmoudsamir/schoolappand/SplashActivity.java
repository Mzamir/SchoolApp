package com.example.mahmoudsamir.schoolappand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.mahmoudsamir.schoolappand.parent_flow.account.view.ParentSignInActivity;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.view.ParentHomeActivity;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.HELPER_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.PARENT_USER_TYPE;
import static com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference.getLoginState;
import static com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference.getUserType;

public class SplashActivity extends AppCompatActivity {
    String TAG = SplashActivity.class.getSimpleName();
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        if (getLoginState(SplashActivity.this)) {
            if (getUserType(SplashActivity.this).equals(PARENT_USER_TYPE))
                intent = new Intent(SplashActivity.this, ParentHomeActivity.class);
            else if (getUserType(SplashActivity.this).equals(HELPER_USER_TYPE)) {
                // TODO HelperHomeActivity
                Log.i(TAG, HELPER_USER_TYPE);
            }
        } else
            intent = new Intent(SplashActivity.this, ParentSignInActivity.class);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "Run timer");
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}