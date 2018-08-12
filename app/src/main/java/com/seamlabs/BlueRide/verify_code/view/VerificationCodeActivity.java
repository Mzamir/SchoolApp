package com.seamlabs.BlueRide.verify_code.view;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.MyActivity;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;
import com.seamlabs.BlueRide.verify_code.presenter.VerifyPhoneInteractor;
import com.seamlabs.BlueRide.verify_code.presenter.VerifyPhonePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.USER_NATIONAL_ID;

public class VerificationCodeActivity extends MyActivity implements VerificationCodeView {

    @BindView(R.id.verification_code_edx)
    EditText verification_code_edx;

    @BindView(R.id.verify_code_btn)
    Button verify_code_btn;

    @BindView(R.id.resend_code)
    TextView resend_code;

    VerifyPhonePresenter presenter;
    String national_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_background_color));
//        }
        setContentView(R.layout.activity_verification_code);
        ButterKnife.bind(this);
        presenter = new VerifyPhonePresenter(this, new VerifyPhoneInteractor());
        national_id = getIntent().getStringExtra(USER_NATIONAL_ID);
        verify_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhoneNumber();
            }
        });
        resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode();
            }
        });

    }

    private void verifyPhoneNumber() {
        presenter.verifyPhone(verification_code_edx.getText().toString(), national_id);
    }

    private void resendVerificationCode() {
        presenter.resendVerificationCode(national_id);
    }


    @Override
    public void showProgress() {
        Utility.showProgressDialog(VerificationCodeActivity.this);
    }

    @Override
    public void hideProgress() {
        Utility.hideProgressDialog();
    }

    @Override
    public void onErrorVerifyPhone() {

    }

    @Override
    public void navigateToParentHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}