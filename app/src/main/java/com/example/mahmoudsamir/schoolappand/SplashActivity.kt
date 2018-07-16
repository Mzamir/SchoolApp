package com.example.mahmoudsamir.schoolappand

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.mahmoudsamir.schoolappand.helper_signup.HelperSignupActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    val timer = Timer("schedule", true)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        timer.schedule(2000) {
            val intent = Intent(this@SplashActivity, HelperSignupActivity::class.java)
            startActivity(intent)
        }
    }
}
