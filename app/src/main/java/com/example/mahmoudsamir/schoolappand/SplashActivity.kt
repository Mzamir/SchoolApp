package com.example.mahmoudsamir.schoolappand

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.example.mahmoudsamir.schoolappand.parent_account.view.ParentSignInActivity
import com.example.mahmoudsamir.schoolappand.parent_home.view.ParentHomeActivity
import com.example.mahmoudsamir.schoolappand.utils.Constants.HELPER_USER_TYPE
import com.example.mahmoudsamir.schoolappand.utils.Constants.PARENT_USER_TYPE
import com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference.getLoginState
import com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference.getUserType
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    internal var TAG = SplashActivity::class.java.simpleName
    internal var intent: Intent? = null

    val timer = Timer("schedule", true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        if (getLoginState(this@SplashActivity)) {
            if (getUserType(this@SplashActivity) == PARENT_USER_TYPE)
                intent = Intent(this@SplashActivity, ParentHomeActivity::class.java)
            else if (getUserType(this@SplashActivity) == HELPER_USER_TYPE) {
                // TODO HelperHomeActivity
                Log.i(TAG, HELPER_USER_TYPE)
            }
        } else
            intent = Intent(this@SplashActivity, ParentSignInActivity::class.java)

        timer.schedule(2000) {
            Log.e(TAG, "Run timer")
            startActivity(intent)
            finish()
        }
    }
}