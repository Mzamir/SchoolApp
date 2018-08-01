package com.example.mahmoudsamir.schoolappand.parent_flow.add_helper.view;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.parent_flow.add_helper.presenter.AddHelperInteractor;
import com.example.mahmoudsamir.schoolappand.parent_flow.add_helper.presenter.AddHelperPresenter;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.view.ParentHomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.ERROR;

public class AddHelperActivity extends AppCompatActivity implements AddHelperView {

    AddHelperPresenter presenter;
    @BindView(R.id.phone_edx)
    EditText phone_edx;

    @BindView(R.id.add_helper)
    Button add_helper;

    @BindView(R.id.later_tv)
    TextView later_tv;

    @BindView(R.id.username)
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_background_color));
//        }

        setContentView(R.layout.activity_add_helper);
        ButterKnife.bind(this);
        presenter = new AddHelperPresenter(this, new AddHelperInteractor());
        // TODO get username when finish realm scenario
        username.setText("Welcome " + "username");
        add_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHelper();
            }
        });
        later_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHomeActivity();
            }
        });
    }

    private void addHelper() {
        presenter.addHelper(phone_edx.getText().toString());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onErrorAddingHelper(String errorMessage) {
        if (errorMessage.equals(ERROR))
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        else {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onSuccessAddingHelper() {
        navigateToHomeActivity();
    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(AddHelperActivity.this, ParentHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
