package com.seamlabs.BlueRide.parent_flow.helper_profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.requests.AssignStudentsToHelperRequestModel;
import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.network.response.StudentResponseModel;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;
import com.seamlabs.BlueRide.parent_flow.profile.adapter.ProfileStudentRecyclerViewAdapter;
import com.seamlabs.BlueRide.parent_flow.profile.view.ParentProfileViewCommunicator;
import com.seamlabs.BlueRide.utils.Utility;
import com.facebook.drawee.view.SimpleDraweeView;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Body;

import static com.seamlabs.BlueRide.utils.Constants.FRAGMENT_TO_SHOW;
import static com.seamlabs.BlueRide.utils.Constants.HELPER_ACCOUNT;
import static com.seamlabs.BlueRide.utils.Constants.PROFILE_FRAGMENT;
import static com.seamlabs.BlueRide.utils.Constants.STUDENTS_LIST;

public class HelperProfileActivity extends AppCompatActivity implements HelperProfileViewCommunicator {

    @BindView(R.id.students_recyclerView)
    RecyclerView students_recyclerView;
    @BindView(R.id.students_layout)
    LinearLayout students_layout;

    // toolbar data
    @BindView(R.id.user_profile_picture)
    SimpleDraweeView user_profile_picture;
    @BindView(R.id.user_profile_name)
    TextView user_profile_name;
    @BindView(R.id.helper_profile_toolbar)
    Toolbar toolbar;
    @BindView(R.id.helper_state_switch)
    Switch helper_state_switch;

    // Basic information
    @BindView(R.id.email_address)
    TextView email_address;
    @BindView(R.id.phone_number)
    TextView phone_number;
    @BindView(R.id.id_number)
    TextView id_number;

    @BindView(R.id.permissions_layout)
    ConstraintLayout permissions_layout;

    @BindView(R.id.allowPickUpSwitcher)
    Switch allowHelperToPickStudent;
    @BindView(R.id.save)
    Button saveChanges;

    @BindView(R.id.permission_type_radioGroup)
    RadioGroup permission_type_radioGroup;
    @BindView(R.id.oneTimeRadioButton)
    RadioButton oneTimePermission;
    @BindView(R.id.alwaysRadioButton)
    RadioButton alwaysPermission;

    HelperProfileStudentRecyclerViewAdapter studentRecyclerViewAdapter;
    HelperResponseModel helperResponseModel;
    ArrayList<StudentResponseModel> studentsList = new ArrayList<>();

    HelperProfilePresenter presenter;
    boolean helperEnabled = true;
    boolean allowToPickStudent = true;
    // 0 for oneTime and 1 for always
    int permissionType = 0;

    @BindView(R.id.content_helper_profile)
    ViewGroup transitionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.activity_helper_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        presenter = new HelperProfilePresenter(this, new HelperProfileInteractor());
        helperResponseModel = (HelperResponseModel) getIntent().getSerializableExtra(HELPER_ACCOUNT);
        studentsList = (ArrayList<StudentResponseModel>) getIntent().getSerializableExtra(STUDENTS_LIST);
        if (helperResponseModel != null) {
            initializeView();
        }
    }

    private void initializeView() {
        if (helperResponseModel.getPivot().getStatus() == 0) {
            helper_state_switch.setChecked(false);
        } else {
            helper_state_switch.setChecked(true);
        }
        email_address.setText(helperResponseModel.getEmail());
        user_profile_name.setText(helperResponseModel.getName());
        user_profile_picture.setImageURI(Uri.parse(helperResponseModel.getImages().get(0).getPath()));
        phone_number.setText(helperResponseModel.getPhone());
        id_number.setText(helperResponseModel.getNational_id());

        studentRecyclerViewAdapter = new HelperProfileStudentRecyclerViewAdapter(this, this, convertStudentsResponseToStudentModel(studentsList));
        RecyclerView.LayoutManager students_recyclerView_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        students_recyclerView.setLayoutManager(students_recyclerView_layoutManager);
        students_recyclerView.setItemAnimator(new DefaultItemAnimator());
        students_recyclerView.setAdapter(studentRecyclerViewAdapter);
        studentRecyclerViewAdapter.notifyDataSetChanged();

        allowHelperToPickStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allowHelperToPickStudent.isChecked()) {
                    allowToPickStudent = true;
                } else {
                    allowToPickStudent = false;
                }
            }
        });

        oneTimePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTimePermission.setChecked(true);
                alwaysPermission.setChecked(false);
                permissionType = 0;
            }
        });
        alwaysPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTimePermission.setChecked(false);
                alwaysPermission.setChecked(true);
                permissionType = 1;
            }
        });
        helper_state_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper_state_switch.isChecked()) {
                    helperEnabled = true;
                } else {
                    helperEnabled = false;
                }
                presenter.changeHelperState(helperResponseModel.getId());
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveChange();
            }
        });

    }

    private void handleSaveChange() {
        AssignStudentsToHelperRequestModel requestModel = new AssignStudentsToHelperRequestModel();

        requestModel.setHelper_id(helperResponseModel.getId());
        requestModel.setAllow_pickup(allowToPickStudent);
        requestModel.setOne_time(permissionType);
        requestModel.setStudent_ids(studentRecyclerViewAdapter.getSelectedStudent());

        presenter.assignHelperToStudents(requestModel);
    }

    @Override
    public void showProgress() {
        Utility.showProgressDialog(this);
    }

    @Override
    public void hideProgress() {
        Utility.hideProgressDialog();
    }

    @Override
    public void onSuccessChangeHelperState() {
        String toast = helperEnabled ? "Successfully Enabled" : "Successfully Disabled";
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorChangeHelperState(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessAssigningHelper() {
        Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(HelperProfileActivity.this, MainActivity.class);
//        intent.putExtra(FRAGMENT_TO_SHOW, PROFILE_FRAGMENT);
//        startActivity(intent);
//        finish();
        onBackPressed();
    }

    @Override
    public void onErrorAssigningHelper(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showPermissionActions(boolean show) {
//        if (show)
//            permissions_layout.setVisibility(View.VISIBLE);
//        else
//            permissions_layout.setVisibility(View.INVISIBLE);
        TransitionManager.beginDelayedTransition(transitionsContainer);
        if (show)
            permissions_layout.setVisibility(View.VISIBLE);
        else
            permissions_layout.setVisibility(View.GONE);

    }


    private ArrayList<StudentModel> convertStudentsResponseToStudentModel(ArrayList<StudentResponseModel> studentResponseModels) {
        ArrayList<StudentModel> studentModels = new ArrayList<>();
        for (StudentResponseModel response : studentResponseModels) {
            StudentModel studentModel = new StudentModel();
            if (response.getImages() != null) {
                if (response.getImages().size() > 0) {
                    studentModel.setStudentPicture(response.getImages().get(0).getPath());
                }
            }
            studentModel.setMarked(false);
            studentModel.setStudentID(response.getId());
            studentModel.setStudentName(response.getName());
            studentModel.setStudentNationalID(response.getNational_id());
            studentModel.setSchoolID(response.getSchool_id());
            studentModel.setClassID(response.getClass_id());
            studentModel.setStudentCreatedAt(response.getCreated_at());
            studentModel.setStudentUpdatedAt(response.getUpdated_at());
            studentModels.add(studentModel);
        }
        return studentModels;
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
}
