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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.MyFragment;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.network.response.StudentResponseModel;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.parent_flow.helper_profile.HelperProfileActivity;
import com.seamlabs.BlueRide.parent_flow.profile.adapter.ProfileHelpersRecyclerViewAdapter;
import com.seamlabs.BlueRide.parent_flow.profile.adapter.ProfileStudentRecyclerViewAdapter;
import com.seamlabs.BlueRide.parent_flow.profile.presenter.ParentProfileInteactor;
import com.seamlabs.BlueRide.parent_flow.profile.presenter.ParentProfilePresenter;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.parent_flow.profile.view.EditProfileFragment.REQUEST_PERMISSION_EXTERNAL_STORAGE;
import static com.seamlabs.BlueRide.parent_flow.profile.view.EditProfileFragment.RESULT_LOAD_IMAGE;
import static com.seamlabs.BlueRide.utils.Constants.HELPER_ACCOUNT;
import static com.seamlabs.BlueRide.utils.Constants.MENTOR_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.STUDENTS_LIST;
import static com.seamlabs.BlueRide.utils.Constants.TEACHER_USER_TYPE;

public class ParentProfileFragment extends MyFragment implements ParentProfileViewCommunicator {

    String TAG = ParentProfileFragment.class.getSimpleName();
    ParentProfilePresenter prsenter;
    ArrayList<HelperResponseModel> helperList = new ArrayList<>();
    ArrayList<StudentResponseModel> studentsList = new ArrayList<>();


    @BindView(R.id.email_address)
    TextView email_address;
    @BindView(R.id.phone_number)
    TextView phone_number;
    @BindView(R.id.id_number)
    TextView id_number;

    @BindView(R.id.helpers_layout)
    LinearLayout helpers_layout;
    @BindView(R.id.students_layout)
    LinearLayout students_layout;

    @BindView(R.id.students_recyclerView)
    RecyclerView students_recyclerView;
    @BindView(R.id.helpers_recyclerView)
    RecyclerView helpers_recyclerView;

    ProfileHelpersRecyclerViewAdapter helpersRecyclerViewAdapter;
    ProfileStudentRecyclerViewAdapter studentRecyclerViewAdapter;
    UserResponseModel userProfileModel;

    Activity activity;
    @BindView(R.id.profile_toolbar)
    Toolbar toolbar;

    public interface onEditProfileClickListener {
        void onEditProfileClick();

//        void onNavigationIconClick();
    }

    @BindView(R.id.user_profile_picture)
    SimpleDraweeView user_profile_picture;
    @BindView(R.id.user_profile_name)
    TextView user_profile_name;
    @BindView(R.id.edit_profile)
    ImageView edit_profile;
    @BindView(R.id.navigation_icon)
    LinearLayout navigation_icon;

    boolean loadData = true;

    private void bindToolBarData() {
        edit_profile.setVisibility(View.VISIBLE);
        if (UserSettingsPreference.getSavedUserProfile(getActivity()).getImages().size() > 0) {
            Uri uri = Uri.parse(UserSettingsPreference.getSavedUserProfile(getActivity()).getImages().get(0).getPath());
            user_profile_picture.setImageURI(uri);
        }
        user_profile_name.setText(UserSettingsPreference.getSavedUserProfile(getActivity()).getName());

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditProfileClickListener != null)
                    onEditProfileClickListener.onEditProfileClick();
            }
        });
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNavigationIconClickListener() != null) {
                    getNavigationIconClickListener().onNavigationIconClick();
                }
//                    onEditProfileClickListener.onNavigationIconClick();
            }
        });
    }

    onEditProfileClickListener onEditProfileClickListener;

    public onEditProfileClickListener getOnEditProfileClickListener() {
        return onEditProfileClickListener;
    }

    public void setOnEditProfileClickListener(onEditProfileClickListener onEditProfileClickListener) {
        this.onEditProfileClickListener = onEditProfileClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_parent_profile, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        activity = getActivity();
        prsenter = new ParentProfilePresenter(this, new ParentProfileInteactor());
        initializeView();
        userProfileModel = UserSettingsPreference.getSavedUserProfile(activity);
        if (UserSettingsPreference.getUserType(activity).equals(TEACHER_USER_TYPE)
                || UserSettingsPreference.getUserType(activity).equals(MENTOR_USER_TYPE)) {
            helpers_layout.setVisibility(View.GONE);
            students_layout.setVisibility(View.GONE);
        }
        bindBasicDateToViews(userProfileModel);

        return view;
    }


    private void bindBasicDateToViews(UserResponseModel userProfileModel) {
        email_address.setText(userProfileModel.getEmail());
        phone_number.setText(userProfileModel.getPhone());
        id_number.setText(userProfileModel.getNational_id());
    }

    private void initializeView() {
        helpersRecyclerViewAdapter = new ProfileHelpersRecyclerViewAdapter(this, activity, helperList);
        RecyclerView.LayoutManager schools_recyclerView_layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        helpers_recyclerView.setLayoutManager(schools_recyclerView_layoutManager);
        helpers_recyclerView.setItemAnimator(new DefaultItemAnimator());
        helpers_recyclerView.setAdapter(helpersRecyclerViewAdapter);

        studentRecyclerViewAdapter = new ProfileStudentRecyclerViewAdapter(this, activity, studentsList);
        RecyclerView.LayoutManager students_recyclerView_layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        students_recyclerView.setLayoutManager(students_recyclerView_layoutManager);
        students_recyclerView.setItemAnimator(new DefaultItemAnimator());
        students_recyclerView.setAdapter(studentRecyclerViewAdapter);
        prsenter.getUserProfile();
    }

    @Override
    public void showProgress() {
        Utility.showProgressDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        Utility.hideProgressDialog();
    }

    @Override
    public void onSuccessGettingUserProfile(UserProfileResponseModel userProfileResponseModel) {

        bindDateToViews(userProfileResponseModel);
    }

    @Override
    public void onErrorGettingUserProfile() {

    }

    @Override
    public void onHelperClickListener(HelperResponseModel helperModel) {
        Intent intent = new Intent(getActivity(), HelperProfileActivity.class);
        intent.putExtra(HELPER_ACCOUNT, helperModel);
        intent.putExtra(STUDENTS_LIST, studentsList);
        startActivity(intent);
    }

    @Override
    public void showPermissionActions(boolean show) {

    }

    StudentResponseModel selectedStudent;

    @Override
    public void onStudentClickListner(StudentResponseModel selectedStudent) {
        this.selectedStudent = selectedStudent;
        handleProfilePictureChange();

    }

    @Override
    public void onSuccessEditingStudentImage() {
        prsenter.getUserProfile();
    }

    @Override
    public void onErrorEditingStudentImage(String message) {
        showSnackBar(message, false);
    }

    private void bindDateToViews(UserProfileResponseModel user) {

        this.studentsList = user.getStudents();
        this.helperList = user.getHelpers();
        studentRecyclerViewAdapter = new ProfileStudentRecyclerViewAdapter(this, activity, studentsList);
        students_recyclerView.setAdapter(studentRecyclerViewAdapter);

        helpersRecyclerViewAdapter = new ProfileHelpersRecyclerViewAdapter(this, activity, helperList);
        helpers_recyclerView.setAdapter(helpersRecyclerViewAdapter);

        helpersRecyclerViewAdapter.notifyDataSetChanged();
        studentRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        bindToolBarData();

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

    String imagePath = "";

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
                if (selectedStudent != null)
                    prsenter.updateStudentPicture(imagePath, selectedStudent.getNational_id());
            }

        }
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
}