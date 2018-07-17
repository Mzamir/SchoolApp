package com.example.mahmoudsamir.schoolappand.verify_code;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.add_helper.presenter.AddHelperInteractor;
import com.example.mahmoudsamir.schoolappand.add_helper.presenter.AddHelperPresenter;
import com.example.mahmoudsamir.schoolappand.add_helper.view.AddHelperView;
import com.example.mahmoudsamir.schoolappand.verify_code.presenter.VerifyPhoneInteractor;
import com.example.mahmoudsamir.schoolappand.verify_code.presenter.VerifyPhonePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerificationCodeActivity extends AppCompatActivity implements VerificationCodeView {

    @BindView(R.id.verification_code_edx)
    EditText verification_code_edx;

    @BindView(R.id.verify_code_btn)
    Button verify_code_btn;

    @BindView(R.id.resend_code)
    TextView resend_code;

    VerifyPhonePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        ButterKnife.bind(this);
        presenter = new VerifyPhonePresenter(this, new VerifyPhoneInteractor());

        verify_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhoneNumber();
            }
        });

    }

    private void verifyPhoneNumber() {
        // TODO get the national_id from sign-up activity intent
        presenter.verifyPhone(verification_code_edx.getText().toString(), "");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onErrorVerifyPhone() {

    }

    @Override
    public void navigateToParentHome() {

    }
}
