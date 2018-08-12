package com.seamlabs.BlueRide.parent_flow.helper_profile;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.network.response.StudentResponseModel;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;
import com.seamlabs.BlueRide.parent_flow.profile.adapter.ProfileStudentRecyclerViewAdapter;
import com.seamlabs.BlueRide.parent_flow.profile.view.ParentProfileViewCommunicator;
import com.seamlabs.BlueRide.utils.Utility;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.Body;

import static com.seamlabs.BlueRide.utils.Constants.HELPER_ACCOUNT;
import static com.seamlabs.BlueRide.utils.Constants.STUDENTS_LIST;

public class HelperProfileActivity extends AppCompatActivity implements ParentProfileViewCommunicator {

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

    boolean helperEnabled = true;
    boolean allowToPickStudent = true;
    // 0 for oneTime and 1 for always
    int permissionType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.activity_helper_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

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
            }
        });

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
    public void onSuccessGettingUserProfile(UserProfileResponseModel userProfileResponseModel) {

    }

    @Override
    public void onErrorGettingUserProfile() {

    }

    @Override
    public void onHelperClickListener(HelperResponseModel helperModel) {

    }

    @Override
    public void showPermissionActions(boolean show) {
        if (show)
            permissions_layout.setVisibility(View.VISIBLE);
        else
            permissions_layout.setVisibility(View.INVISIBLE);

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
}
