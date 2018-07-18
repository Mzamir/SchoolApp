package com.example.mahmoudsamir.schoolappand.parent_account.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mahmoudsamir.schoolappand.parent_home.ParentHomeActivity;
import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.parent_account.presenter.ParentRegistrationInteractor;
import com.example.mahmoudsamir.schoolappand.parent_account.presenter.ParentSignInPresenter;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_sign_in);
        ButterKnife.bind(this);
        presenter = new ParentSignInPresenter(this, new ParentRegistrationInteractor());

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials();
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
        presenter.validateCredentials(email_edx.getText().toString(), password_edx.getText().toString());
    }
}
