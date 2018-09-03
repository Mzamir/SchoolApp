package com.seamlabs.BlueRide.parent_flow.add_helper.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.MyActivity;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.parent_flow.add_helper.presenter.AddHelperInteractor;
import com.seamlabs.BlueRide.parent_flow.add_helper.presenter.AddHelperPresenter;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.EMPTY_FIELD_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;
import static com.seamlabs.BlueRide.utils.Utility.getCountryCodes;

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

    @BindView(R.id.spinner_counter_codes)
    TextView spinner_counter_codes;
    @BindView(R.id.spinnerLayout)
    LinearLayout spinnerLayout;
    ListPopupWindow listPopupWindow;
    String spinnerList[];
    String selectedCountryCode = "+966";

    private void handleCountrySpinner() {
        spinnerList = getCountryCodes();
        spinner_counter_codes.setText(spinnerList[0]);
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter(
                this,
                R.layout.spinner_text, spinnerList));
        listPopupWindow.setAnchorView(spinnerLayout);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spinner_counter_codes.setText(spinnerList[position]);
                selectedCountryCode = spinnerList[position];
                listPopupWindow.dismiss();
            }
        });
        spinnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow.show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        handleCountrySpinner();

    }

    private void addHelper() {
        String phoneNumber = phone_edx.getText().toString();
        if (phoneNumber.isEmpty()) {
            phone_edx.setError(EMPTY_FIELD_ERROR);
            return;
        }
        if (phoneNumber.length() < 10) {
            phone_edx.setError(getResources().getString(R.string.id_number_error));
            return;
        }
        phoneNumber = selectedCountryCode + phoneNumber;
        presenter.addHelper(phoneNumber);
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
