package com.seamlabs.BlueRide.parent_flow.account.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seamlabs.BlueRide.MyActivity;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.helper_account.view.HelperSignupActivity;
import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.parent_flow.account.presenter.ParentRegistrationInteractor;
import com.seamlabs.BlueRide.parent_flow.account.presenter.ParentSignupPresenter;
import com.seamlabs.BlueRide.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentSignupActivity extends MyActivity implements ParentRegistrationView {

    ParentSignupPresenter presenter;

    @BindView(R.id.signup_btn)
    Button signup_btn;

    @BindView(R.id.id_number_edx)
    EditText id_number_edx;

    @BindView(R.id.password_edx)
    EditText password_edx;

    @BindView(R.id.signup_as_helper)
    TextView signup_as_helper;

    @BindView(R.id.signin_btn)
    TextView signin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_signup);
        ButterKnife.bind(this);
        presenter = new ParentSignupPresenter(this, new ParentRegistrationInteractor());

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
            }
        });
        signup_as_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParentSignupActivity.this, HelperSignupActivity.class));
                finish();
            }
        });
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParentSignupActivity.this, ParentSignInActivity.class));
                finish();
            }
        });

    }

    @Override
    public void showProgress() {
        Utility.showProgressDialog(ParentSignupActivity.this);
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
    public void navigateToParentHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void validateCredentials() {
        if (id_number_edx.getText().toString().isEmpty()) {
            id_number_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        if (password_edx.getText().toString().isEmpty()) {
            password_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        presenter.validateCredentials(id_number_edx.getText().toString(), password_edx.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
