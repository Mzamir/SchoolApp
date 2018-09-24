package com.seamlabs.BlueRide.helper_account.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.parent_flow.account.view.ParentSignInActivity;
import com.seamlabs.BlueRide.parent_flow.account.view.ParentSignupActivity;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.helper_account.presenter.HelperRegistrationInteractor;
import com.seamlabs.BlueRide.helper_account.presenter.HelperRegistrationPresenter;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;
import com.seamlabs.BlueRide.verify_code.view.VerificationCodeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.ADMIN_LOGIN_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.USER_ID;
import static com.seamlabs.BlueRide.utils.Constants.USER_NATIONAL_ID;

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

    @BindView(R.id.forget_password)
    TextView forget_password;

    @BindView(R.id.show_password)
    ImageView show_password;
    boolean isPasswordShown = false;

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
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelperSigninActivity.this, ActivityWebView.class));
            }
        });
        show_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordShown = !isPasswordShown;
                if (isPasswordShown) {
                    password_edx.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password_edx.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                Log.i("ParentgSignInActivity", "Password " + String.valueOf(isPasswordShown));
            }
        });
    }

    @Override
    public void showProgress() {
        Utility.showProgressDialog(HelperSigninActivity.this);
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
        UserResponseModel userResponseModel = UserSettingsPreference.getSavedUserProfile(this);
        if (userResponseModel.getStatus() == 1 && userResponseModel.getIs_verified() == 1) {
            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (userResponseModel.getStatus() == 0 && userResponseModel.getIs_verified() == 0) {
            if (userResponseModel.getAuthy_code() == null) {
                showSnackBar(getResources().getString(R.string.need_to_signup), true);
            } else {
                Intent intent = new Intent(this, VerificationCodeActivity.class);
                intent.putExtra(USER_NATIONAL_ID, userResponseModel.getNational_id());
                intent.putExtra(USER_ID, userResponseModel.getId());
                startActivity(intent);
                finish();
            }

        } else if (userResponseModel.getStatus() == 0 && userResponseModel.getIs_verified() == 1) {
            showSnackBar(ADMIN_LOGIN_ERROR, false);
        }
    }

    public void showSnackBar(String message, boolean showAction) {
        Snackbar snackbar = Snackbar
                .make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
        if (showAction) {
            snackbar.setAction(getResources().getString(R.string.signup), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HelperSigninActivity.this, ParentSignupActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
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
}
