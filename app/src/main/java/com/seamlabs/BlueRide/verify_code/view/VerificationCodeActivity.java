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
import android.widget.Toast;

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

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.USER_ID;
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
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        ButterKnife.bind(this);
        presenter = new VerifyPhonePresenter(this, new VerifyPhoneInteractor());
        national_id = getIntent().getStringExtra(USER_NATIONAL_ID);
        id = getIntent().getIntExtra(USER_ID, -1);
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
        if (verification_code_edx.getText().toString().isEmpty()) {
            verification_code_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        presenter.verifyPhone(verification_code_edx.getText().toString(), national_id);
    }

    private void resendVerificationCode() {
        presenter.resendVerificationCode(id);
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
    public void onErrorVerifyPhone(String message) {

    }

    @Override
    public void navigateToParentHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
        finish();
    }

    @Override
    public void onSuccessResendCode() {
        Toast.makeText(this, "Code successfully sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResendCode(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
