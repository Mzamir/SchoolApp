package com.seamlabs.BlueRide.parent_flow.account.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.MyActivity;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.helper_account.view.ActivityWebView;
import com.seamlabs.BlueRide.parent_flow.account.presenter.ParentRegistrationInteractor;
import com.seamlabs.BlueRide.parent_flow.account.presenter.ParentSignInPresenter;
import com.seamlabs.BlueRide.utils.PrefUtils;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;
import com.seamlabs.BlueRide.verify_code.view.VerificationCodeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.USER_ID;
import static com.seamlabs.BlueRide.utils.Constants.USER_NATIONAL_ID;

public class ParentSignInActivity extends MyActivity implements ParentRegistrationView {

    ParentSignInPresenter presenter;
    @BindView(R.id.signin_btn)
    Button signin_btn;

    @BindView(R.id.email_edx)
    EditText email_edx;

    @BindView(R.id.password_edx)
    EditText password_edx;

    @BindView(R.id.signup_btn)
    TextView signup_btn;

    @BindView(R.id.forget_password)
    TextView forget_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_background_color));
//        }
        setContentView(R.layout.activity_parent_sign_in);
        ButterKnife.bind(this);
        presenter = new ParentSignInPresenter(this, new ParentRegistrationInteractor());

        PrefUtils.storeApiKey(this, "");
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParentSignInActivity.this, ParentSignupActivity.class));
                finish();
            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParentSignInActivity.this , ActivityWebView.class));
            }
        });
    }

    @Override
    public void showProgress() {
        Utility.showProgressDialog(ParentSignInActivity.this);
    }

    @Override
    public void hideProgress() {
        Utility.hideProgressDialog();
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
            intent.putExtra(USER_ID, UserSettingsPreference.getSavedUserProfile(this).getId());
            startActivity(intent);
            finish();
        } else {
            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSuccessGettingTerms() {

    }

    private void validateCredentials() {
        if (email_edx.getText().toString().isEmpty()) {
            email_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        if (password_edx.getText().toString().isEmpty()) {
            password_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        presenter.validateCredentials(email_edx.getText().toString(), password_edx.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
