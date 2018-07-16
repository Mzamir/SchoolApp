package com.example.mahmoudsamir.schoolappand.parent_signin.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mahmoudsamir.schoolappand.ParentHomeActivity;
import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.parent_signin.presenter.ParentSignInInteractor;
import com.example.mahmoudsamir.schoolappand.parent_signin.presenter.ParentSignInPresenter;

import butterknife.BindView;

public class ParentSignInActivity extends AppCompatActivity implements ParentSigninView {

    ParentSignInPresenter presenter;
    @BindView(R.id.signup_btn)
    Button signup_btn;

    @BindView(R.id.id_number_edx)
    EditText id_number_edx;

    @BindView(R.id.password_edx)
    EditText password_edx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_sign_in);

        presenter = new ParentSignInPresenter(this, new ParentSignInInteractor());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setIDNumberError() {
        id_number_edx.setError(getString(R.string.id_number_error));
    }

    @Override
    public void setPasswordError() {
        password_edx.setError(getString(R.string.password_error));
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
