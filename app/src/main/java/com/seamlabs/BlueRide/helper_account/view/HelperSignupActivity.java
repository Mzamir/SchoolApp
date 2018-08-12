package com.seamlabs.BlueRide.helper_account.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.MyActivity;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.helper_account.presenter.HelperRegistrationPresenter;
import com.seamlabs.BlueRide.parent_flow.account.view.ParentSignupActivity;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.helper_account.presenter.HelperRegistrationInteractor;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;
import com.seamlabs.BlueRide.verify_code.view.VerificationCodeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.USER_NATIONAL_ID;

public class HelperSignupActivity extends MyActivity implements HelperRegistrationView {

    HelperRegistrationPresenter presenter;

    @BindView(R.id.email_edx)
    EditText email_edx;
    @BindView(R.id.username_edx)
    EditText username_edx;
    @BindView(R.id.password_edx)
    EditText password_edx;
    @BindView(R.id.id_number_edx)
    EditText id_number_edx;
    @BindView(R.id.phone_edx)
    EditText phone_edx;

    @BindView(R.id.signup_btn)
    Button signup_btn;
    @BindView(R.id.signin_btn)
    TextView signin_btn;
    @BindView(R.id.signup_as_helper)
    TextView signup_as_helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_background_color));
//        }
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) ;
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_helper_signup);
        ButterKnife.bind(this);

        presenter = new HelperRegistrationPresenter(this, new HelperRegistrationInteractor());
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
            }
        });
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelperSignupActivity.this, HelperSigninActivity.class));
                finish();
            }
        });
        signup_as_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelperSignupActivity.this, ParentSignupActivity.class));
                finish();
            }
        });
    }

    @Override
    public void showProgress() {
        Utility.showProgressDialog(HelperSignupActivity.this);
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
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void validateCredentials() {
        if (email_edx.getText().toString().isEmpty()) {
            email_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        if (username_edx.getText().toString().isEmpty()) {
            username_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        if (password_edx.getText().toString().isEmpty()) {
            password_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        if (id_number_edx.getText().toString().isEmpty()) {
            id_number_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        if (phone_edx.getText().toString().isEmpty()) {
            phone_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        presenter.validateCredentials(username_edx.getText().toString()
                , email_edx.getText().toString()
                , password_edx.getText().toString()
                , id_number_edx.getText().toString()
                , phone_edx.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}