package com.seamlabs.BlueRide.parent_flow.account.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.BuildConfig;
import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.MyActivity;
import com.seamlabs.BlueRide.helper_account.view.HelperSignupActivity;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.parent_flow.account.presenter.ParentRegistrationInteractor;
import com.seamlabs.BlueRide.parent_flow.account.presenter.ParentSignupPresenter;
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
import static com.seamlabs.BlueRide.utils.Constants.USER_NATIONAL_ID;

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

    @BindView(R.id.termsAndConditions)
    TextView termsAndConditions;

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
        termsAndConditions.setPaintFlags(termsAndConditions.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfFileExit())
                    viewTermsAndConditionsFile();
                else
                    getTermsAndConditionsFile();
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
    public void navigateToParentHome(int status) {
        if (status == 0) {
            Intent intent = new Intent(this, VerificationCodeActivity.class);
            intent.putExtra(USER_NATIONAL_ID, UserSettingsPreference.getSavedUserProfile(this).getNational_id());
            startActivity(intent);
            finish();
        } else {
            UserSettingsPreference.updateLoginState(getMyApplicationContext(), true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
    }

    @Override
    public void onSuccessGettingTerms() {

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
            Toast.makeText(this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
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
}
