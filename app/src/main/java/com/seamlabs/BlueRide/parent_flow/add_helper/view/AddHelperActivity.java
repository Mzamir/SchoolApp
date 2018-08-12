package com.seamlabs.BlueRide.parent_flow.add_helper.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.MyActivity;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.parent_flow.add_helper.presenter.AddHelperInteractor;
import com.seamlabs.BlueRide.parent_flow.add_helper.presenter.AddHelperPresenter;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;

public class AddHelperActivity extends MyActivity implements AddHelperView {

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
        username.setText("Welcome " + UserSettingsPreference.getSavedUserProfile(this).getName());
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
        Utility.showProgressDialog(AddHelperActivity.this);
    }

    @Override
    public void hideProgress() {
        Utility.hideProgressDialog();
    }

    @Override
    public void onErrorAddingHelper(String errorMessage) {
        if (errorMessage.equals(GENERAL_ERROR))
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
        Intent intent = new Intent(AddHelperActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
