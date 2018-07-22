package com.example.mahmoudsamir.schoolappand.helper_account.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mahmoudsamir.schoolappand.parent_account.view.ParentSignupActivity;
import com.example.mahmoudsamir.schoolappand.parent_home.view.ParentHomeActivity;
import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.helper_account.presenter.HelperRegistrationInteractor;
import com.example.mahmoudsamir.schoolappand.helper_account.presenter.HelperSignupPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelperSignupActivity extends AppCompatActivity implements HelperRegistrationView {

    HelperSignupPresenter presenter;

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
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) ;
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_helper_signup);
        ButterKnife.bind(this);

        presenter = new HelperSignupPresenter(this, new HelperRegistrationInteractor());
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
            }
        });
        signup_as_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelperSignupActivity.this, ParentSignupActivity.class));
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
        startActivity(new Intent(this, ParentHomeActivity.class));
        finish();
    }

    private void validateCredentials() {
        presenter.validateCredentials(username_edx.getText().toString()
                , email_edx.getText().toString()
                , password_edx.getText().toString()
                , id_number_edx.getText().toString()
                , phone_edx.getText().toString());
    }
}
