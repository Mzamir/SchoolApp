package com.example.mahmoudsamir.schoolappand.helper_account.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mahmoudsamir.schoolappand.MainActivity;
import com.example.mahmoudsamir.schoolappand.parent_flow.account.view.ParentSignupActivity;
import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.helper_account.presenter.HelperRegistrationInteractor;
import com.example.mahmoudsamir.schoolappand.helper_account.presenter.HelperRegistrationPresenter;
import com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference;
import com.example.mahmoudsamir.schoolappand.verify_code.view.VerificationCodeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.USER_NATIONAL_ID;

public class HelperSigninActivity extends AppCompatActivity implements HelperRegistrationView {
    HelperRegistrationPresenter presenter;
    @BindView(R.id.signin_btn)
    Button signin_btn;

    @BindView(R.id.email_edx)
    EditText email_edx;

    @BindView(R.id.password_edx)
    EditText password_edx;

    @BindView(R.id.signup_btn)
    TextView signup_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_sign_in);
        ButterKnife.bind(this);
        presenter = new HelperRegistrationPresenter(this, new HelperRegistrationInteractor());

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelperSigninActivity.this, ParentSignupActivity.class));
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onErrorRegistration(String errorMessage) {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void navigateToParentHome(int status) {
        if (status == 0) {
            Intent intent = new Intent(this, VerificationCodeActivity.class);
            intent.putExtra(USER_NATIONAL_ID, UserSettingsPreference.getSavedUserProfile(this).getNational_id());
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void validateCredentials() {
        presenter.validateCredentials(email_edx.getText().toString(), password_edx.getText().toString());
    }
}
