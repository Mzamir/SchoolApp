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
import com.example.mahmoudsamir.schoolappand.parent_account.presenter.ParentSignupPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentSignupActivity extends AppCompatActivity implements ParentRegistrationView {

    ParentSignupPresenter presenter;

    @BindView(R.id.signup_btn)
    Button signup_btn;

    @BindView(R.id.id_number_edx)
    EditText id_number_edx;

    @BindView(R.id.password_edx)
    EditText password_edx;

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
        presenter.validateCredentials(id_number_edx.getText().toString(), password_edx.getText().toString());
    }
}
