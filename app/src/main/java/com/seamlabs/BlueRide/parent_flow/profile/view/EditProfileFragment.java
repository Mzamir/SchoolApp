package com.seamlabs.BlueRide.parent_flow.profile.view;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.requests.EditProfileRequestModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.parent_flow.profile.presenter.EditProfileInteactor;
import com.seamlabs.BlueRide.parent_flow.profile.presenter.EditProfilePresenter;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.EMPTY_FIELD_ERROR;
import static com.seamlabs.BlueRide.utils.Utility.isEmailValid;

public class EditProfileFragment extends Fragment implements EditProfileViewCommunicator {
    String TAG = EditProfileFragment.class.getSimpleName();
    @BindView(R.id.email_address)
    EditText email_address;
    @BindView(R.id.edit_email_address)
    ImageView edit_email_address;

    @BindView(R.id.phone_number)
    EditText phone_number;
    @BindView(R.id.edit_phone_number)
    ImageView edit_phone_number;

    @BindView(R.id.password_edx)
    EditText password_edx;
    @BindView(R.id.new_password_edx)
    EditText new_password_edx;
    @BindView(R.id.confirm_password_edx)
    EditText confirm_password_edx;
    @BindView(R.id.edit_password)
    ImageView edit_password;

    @BindView(R.id.address)
    EditText address_edx;
    @BindView(R.id.edit_address)
    ImageView edit_address;

    @BindView(R.id.profile_picture_layout)
    LinearLayout edit_profile_picture;

    @BindView(R.id.profile_picture)
    TextView profile_picture;

    @BindView(R.id.save_edits)
    Button save_edits;

    @BindView(R.id.new_password_layout)
    LinearLayout new_password_layout;

    UserResponseModel userProfileModel;
    Activity activity;

    EditProfilePresenter presenter;

    @BindView(R.id.transitions_container)
    ViewGroup transitionsContainer;
    private int RESULT_LOAD_IMAGE = 100;
    private final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 1000;

    private boolean somethingUpdated = false;
    EditProfileRequestModel requestModel;

    private String email = "";
    private String phone = "";
    private String current_password = "";
    private String new_password = "";
    private String confirm_password = "";
    private String address = "";
    private String imagePath = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_profile, container, false);
        ButterKnife.bind(this, view);
        requestModel = new EditProfileRequestModel();
        activity = getActivity();
        presenter = new EditProfilePresenter(this, new EditProfileInteactor());
        userProfileModel = UserSettingsPreference.getSavedUserProfile(activity);
        if (userProfileModel != null)
            bindBasicDateToViews();
        handleViewsListener();

        save_edits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdits();
            }
        });
        return view;
    }

    private void bindBasicDateToViews() {
        email_address.setText(userProfileModel.getEmail());
        phone_number.setText(userProfileModel.getPhone());
        if (userProfileModel.getAddress() != null) {
            address_edx.setText(userProfileModel.getAddress());
        }
    }

    private void handleViewsListener() {
        edit_email_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_address.setEnabled(true);
                save_edits.setVisibility(View.VISIBLE);
            }
        });
        edit_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_number.setEnabled(true);
                save_edits.setVisibility(View.VISIBLE);
            }
        });
        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_edx.setEnabled(true);
                save_edits.setVisibility(View.VISIBLE);
                showNewPassword(new_password_layout);
            }
        });
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_edx.setEnabled(true);
                save_edits.setVisibility(View.VISIBLE);
            }
        });

        edit_profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleProfilePictureChange();
            }
        });

    }

    private void showSaveActionButton(boolean show) {
        if (show) {
            if (save_edits.getVisibility() == View.INVISIBLE) {
                save_edits.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showProgress() {
        Utility.showProgressDialog(activity);
    }

    @Override
    public void hideProgress() {
        Utility.hideProgressDialog();
    }

    @Override
    public void onSuccessEditProfile() {
        Toast.makeText(activity, "Changes saved", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(activity, MainActivity.class);
//        intent.putExtra(FRAGMENT_TO_SHOW, PROFILE_FRAGMENT);
//        startActivity(intent);
//        activity.finish();
        activity.onBackPressed();
    }

    @Override
    public void onErrorEditProfile(String errorMessage) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void saveEdits() {

        if (checkViewsEnableState(email_address)) {
            if (checkViewsEmptyState(email_address)) {
                if (isEmailValid(email_address.getText().toString())) {
                    if (!email_address.getText().toString().equals(userProfileModel.getEmail())) {
                        requestModel.setEmail(email_address.getText().toString());
                        email = requestModel.getEmail();
                        somethingUpdated = true;
                    }
                } else {
                    email_address.setError("Invalid Email address");
                }
            } else
                return;
        }
        if (checkViewsEnableState(phone_number)) {
            if (checkViewsEmptyState(phone_number)) {
                if (!phone_number.getText().toString().equals(userProfileModel.getPhone())) {
                    requestModel.setPhone(phone_number.getText().toString());
                    phone = requestModel.getPhone();
                    somethingUpdated = true;
                }
            } else
                return;
        }
        if (checkViewsEnableState(address_edx)) {
            if (checkViewsEmptyState(address_edx)) {
                if (!address_edx.getText().toString().equals(userProfileModel.getAddress())) {
                    requestModel.setAddress(address_edx.getText().toString());
                    address = requestModel.getAddress();
                    somethingUpdated = true;
                }
            } else
                return;
        }
        if (checkViewsEnableState(password_edx)) {
            if (checkViewsEmptyState(password_edx)) {
                requestModel.setCurrent_password(password_edx.getText().toString());
                current_password = requestModel.getCurrent_password();
                if (checkViewsEmptyState(new_password_edx)) {
                    requestModel.setNew_password(new_password_edx.getText().toString());
                    new_password = requestModel.getNew_password();
                    if (checkViewsEmptyState(confirm_password_edx)) {
                        requestModel.setNew_password(confirm_password_edx.getText().toString());
                        confirm_password = requestModel.getConfirm_password();
                        somethingUpdated = true;
                    } else
                        return;
                } else
                    return;
            } else
                return;
        }
        if (somethingUpdated) {
            presenter.editProfile(requestModel);
            if (!imagePath.isEmpty())
                presenter.editProfile(imagePath, false);
        } else {
            if (!imagePath.isEmpty())
                presenter.editProfile(imagePath, true);
            else
                showSnackBar("You changed nothing", false);
        }
    }

    private boolean checkViewsEnableState(EditText view) {
        if (view.isEnabled()) {
            return true;
        }
        return false;
    }

    private boolean checkViewsEmptyState(EditText view) {
        if (!view.getText().toString().isEmpty()) {
            return true;
        }
        view.setError(EMPTY_FIELD_ERROR);
        return false;
    }

    private void showNewPassword(final View view) {
//        boolean visible = view.getVisibility() == View.VISIBLE ? true : false;
        TransitionManager.beginDelayedTransition(transitionsContainer);
        // it is the same as
        // TransitionManager.beginDelayedTransition(transitionsContainer, new AutoTransition());
        // where AutoTransition is the set of Fade and ChangeBounds transitions
//        new_password_layout.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (view.getVisibility() == View.GONE)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);

    }

    public void showSnackBar(String message, boolean showAction) {
        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();

        if (showAction) {
            snackbar.setAction("Grantee", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getActivity().getPackageName(), null));

                    startActivity(intent);
                }
            });
        }
    }

    private void handleProfilePictureChange() {
        if (checkGPSPermission()) {
            performCameraAndGalleyAction();
        }
    }

    private void performCameraAndGalleyAction() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult");
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
            if (!imagePath.isEmpty()) {
                Log.i(TAG, imagePath);
//                somethingUpdated = true;
                profile_picture.setText(imagePath.substring(imagePath.lastIndexOf("/") + 1));
            }

        }
    }

    private boolean checkGPSPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showExplanation("Permission Needed",
                        "In order to continue and change your picture, We need your permission to access your storage",
                        Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_EXTERNAL_STORAGE);
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_EXTERNAL_STORAGE);
            }
            return false;
        }
        return true;
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        requestPermissions(new String[]{permissionName}, permissionRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission Granted!", Toast.LENGTH_SHORT).show();
                    performCameraAndGalleyAction();
                } else {
                    boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!showRationale) {
                        showSnackBar("Permission Denied!", true);
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permissions[0])) {
                        Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }
}