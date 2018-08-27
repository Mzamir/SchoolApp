package com.seamlabs.BlueRide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.seamlabs.BlueRide.network.requests.UpdateLocationRequestModel;
import com.seamlabs.BlueRide.network.response.UpdateLocationResponseModel;
import com.seamlabs.BlueRide.parent_flow.account.view.ParentSignInActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.seamlabs.BlueRide.utils.Constants.HELPER_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.MENTOR_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.PARENT_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_EVENT_NAME;
import static com.seamlabs.BlueRide.utils.Constants.USER_TYPE;
import static com.seamlabs.BlueRide.utils.UserSettingsPreference.getLoginState;
import static com.seamlabs.BlueRide.utils.UserSettingsPreference.getUserType;

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
            intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra(USER_TYPE, getUserType(SplashActivity.this));
            Log.i(TAG, getUserType(SplashActivity.this));
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