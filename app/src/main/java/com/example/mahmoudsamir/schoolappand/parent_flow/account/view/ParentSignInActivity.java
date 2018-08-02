package com.example.mahmoudsamir.schoolappand.parent_flow.account.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mahmoudsamir.schoolappand.MainActivity;
import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.parent_flow.account.presenter.ParentRegistrationInteractor;
import com.example.mahmoudsamir.schoolappand.parent_flow.account.presenter.ParentSignInPresenter;
import com.example.mahmoudsamir.schoolappand.utils.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentSignInActivity extends AppCompatActivity implements ParentRegistrationView {

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
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onErrorRegistration() {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), "Invalid ID-Number or password", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void navigateToParentHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void validateCredentials() {
        boolean formComplete = true;
        if (email_edx.getText().toString().isEmpty()) {
            email_edx.setError(getResources().getString(R.string.empty_field));
            formComplete = false;
        }
        if (password_edx.getText().toString().isEmpty()) {
            password_edx.setError(getResources().getString(R.string.empty_field));
            formComplete = false;
        }
        if (formComplete)
            presenter.validateCredentials(email_edx.getText().toString(), password_edx.getText().toString());
    }
}
