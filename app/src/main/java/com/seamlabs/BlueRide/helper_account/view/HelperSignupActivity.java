package com.seamlabs.BlueRide.helper_account.view;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.BuildConfig;
import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.MyActivity;
import com.seamlabs.BlueRide.helper_account.presenter.HelperRegistrationPresenter;
import com.seamlabs.BlueRide.parent_flow.account.view.ParentSignupActivity;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.helper_account.presenter.HelperRegistrationInteractor;
import com.seamlabs.BlueRide.utils.FileDownloader;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;
import com.seamlabs.BlueRide.verify_code.view.VerificationCodeActivity;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.MyApplication.getMyApplicationContext;
import static com.seamlabs.BlueRide.utils.Constants.BASE_URL;
import static com.seamlabs.BlueRide.utils.Constants.TERMS_AND_CONDITIONS_FILE_NAME;
import static com.seamlabs.BlueRide.utils.Constants.TERMS_AND_CONDITIONS_FOLDER_NAME;
import static com.seamlabs.BlueRide.utils.Constants.USER_ID;
import static com.seamlabs.BlueRide.utils.Constants.USER_NATIONAL_ID;
import static com.seamlabs.BlueRide.utils.Utility.getCountryCodes;

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

    @BindView(R.id.termsAndConditions)
    TextView termsAndConditions;
    @BindView(R.id.terms_checkbox)
    CheckBox terms_checkbox;
    boolean termsAccepted = false;

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
        termsAndConditions.setPaintFlags(termsAndConditions.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission())
                    performTermsAndConditionsAction();
            }
        });
        terms_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                termsAccepted = isChecked;
            }
        });
        handleCountrySpinner();
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
            intent.putExtra(USER_ID, UserSettingsPreference.getSavedUserProfile(this).getId());
            startActivity(intent);
            finish();
        } else {
            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
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
        if (password_edx.getText().toString().length() < 6) {
            password_edx.setError(getResources().getString(R.string.invalid_password));
            return;
        }
        if (id_number_edx.getText().toString().isEmpty()) {
            id_number_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        if (id_number_edx.getText().toString().length() < 10) {
            id_number_edx.setError(getResources().getString(R.string.invalid_id));
            return;
        }
        if (phone_edx.getText().toString().isEmpty()) {
            phone_edx.setError(getResources().getString(R.string.empty_field));
            return;
        }
        if (termsAccepted) {
            presenter.validateCredentials(username_edx.getText().toString()
                    , email_edx.getText().toString()
                    , password_edx.getText().toString()
                    , id_number_edx.getText().toString()
                    , selectedCountryCode + phone_edx.getText().toString());
        } else {
            Toast.makeText(this, getResources().getString(R.string.accept_terms), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean checkIfFileExit() {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + TERMS_AND_CONDITIONS_FOLDER_NAME + "/" + TERMS_AND_CONDITIONS_FILE_NAME);
        if (file.exists())
            return true;

        return false;
    }

    public void getTermsAndConditionsFile() {
        new DownloadFile().execute(BASE_URL + "terms_conditions", TERMS_AND_CONDITIONS_FILE_NAME);
    }

    public void viewTermsAndConditionsFile() {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + TERMS_AND_CONDITIONS_FOLDER_NAME + "/" + TERMS_AND_CONDITIONS_FILE_NAME);  // -> filename = maven.pdf
        Uri path = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getResources().getString(R.string.no_app_to_PDF), Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgress();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, TERMS_AND_CONDITIONS_FOLDER_NAME);
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hideProgress();
            viewTermsAndConditionsFile();
        }
    }

    public static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 1000;

    void performTermsAndConditionsAction() {
        if (checkIfFileExit())
            viewTermsAndConditionsFile();
        else
            getTermsAndConditionsFile();
    }

    private boolean checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showExplanation(getResources().getString(R.string.permission_needed),
                        getResources().getString(R.string.terms_permission_explanation),
                        Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_EXTERNAL_STORAGE);
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_EXTERNAL_STORAGE);
            }
            return false;
        }
        return true;
    }

    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{permissionName}, permissionRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getResources().getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                    performTermsAndConditionsAction();
                } else {
                    boolean showRationale = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                    }
                    if (!showRationale) {
                        showSnackBar(getResources().getString(R.string.permission_denied), true);
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permissions[0])) {
                        Toast.makeText(this, getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    public void showSnackBar(String message, boolean showAction) {
        Snackbar snackbar = Snackbar
                .make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();

        if (showAction) {
            snackbar.setAction(getResources().getString(R.string.allow_permission), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                }
            });
        }
    }
}
