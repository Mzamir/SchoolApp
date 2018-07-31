package com.example.mahmoudsamir.schoolappand.parent_flow.profile;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mahmoudsamir.schoolappand.R;

public class ParentProfileActivity extends AppCompatActivity {


    // TODO  start implementation of this class when finishing of the realm scenario
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.activity_parent_profile);
    }
}
