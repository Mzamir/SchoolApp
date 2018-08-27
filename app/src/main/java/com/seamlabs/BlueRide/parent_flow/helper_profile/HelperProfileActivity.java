package com.seamlabs.BlueRide.parent_flow.helper_profile;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.requests.AssignStudentsToHelperRequestModel;
import com.seamlabs.BlueRide.network.response.HelperProfileResponseModel;
import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.network.response.StudentPivotResponseModel;
import com.seamlabs.BlueRide.network.response.StudentResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;
import com.seamlabs.BlueRide.utils.Utility;
import com.facebook.drawee.view.SimpleDraweeView;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.ALWAYS_PICK_UP;
import static com.seamlabs.BlueRide.utils.Constants.HELPER_ACCOUNT;
import static com.seamlabs.BlueRide.utils.Constants.NOT_ALLOWED_TO_PICK_UP;
import static com.seamlabs.BlueRide.utils.Constants.ONE_TIME_PICK_UP;
import static com.seamlabs.BlueRide.utils.Constants.STUDENTS_LIST;

public class HelperProfileActivity extends AppCompatActivity implements HelperProfileViewCommunicator {

    private static final String TAG = HelperProfileActivity.class.getSimpleName();
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

    @BindView(R.id.permission_type_layout)
    LinearLayout permission_type_layout;
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
        if (helperResponseModel != null) {
            initializeView();
            presenter.getHelperProfile(helperResponseModel.getId());
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
                    permissionType = ONE_TIME_PICK_UP;
                    permission_type_layout.setVisibility(View.VISIBLE);
                } else {
                    permissionType = NOT_ALLOWED_TO_PICK_UP;
                    allowToPickStudent = false;
                    permission_type_layout.setVisibility(View.INVISIBLE);
                }
            }
        });

        oneTimePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTimePermission.setChecked(true);
                alwaysPermission.setChecked(false);
                permissionType = ONE_TIME_PICK_UP;
            }
        });
        alwaysPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTimePermission.setChecked(false);
                alwaysPermission.setChecked(true);
                permissionType = ALWAYS_PICK_UP;
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
//        onBackPressed();
        try {
            presenter.getHelperProfile(helperResponseModel.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorAssigningHelper(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showPermissionActions(boolean show, StudentModel studentModel) {
        TransitionManager.beginDelayedTransition(transitionsContainer);
        if (show) {
            StudentPivotResponseModel pivot = studentModel.getPivot();
            if (pivot != null) {
                Log.i(TAG, "Pivot state " + pivot.getOne_time());
                if (pivot.getOne_time() == 0) {
                    allowHelperToPickStudent.setChecked(false);
                    permission_type_layout.setVisibility(View.INVISIBLE);
                    permissionType = NOT_ALLOWED_TO_PICK_UP;
                } else if (pivot.getOne_time() == 1) {
                    permission_type_layout.setVisibility(View.VISIBLE);
                    allowHelperToPickStudent.setChecked(true);
                    oneTimePermission.setChecked(true);
                    alwaysPermission.setChecked(false);
                    permissionType = ONE_TIME_PICK_UP;
                } else if (pivot.getOne_time() == 2) {
                    permission_type_layout.setVisibility(View.VISIBLE);
                    allowHelperToPickStudent.setChecked(true);
                    oneTimePermission.setChecked(false);
                    alwaysPermission.setChecked(true);
                    permissionType = ALWAYS_PICK_UP;
                }
            }
            permissions_layout.setVisibility(View.VISIBLE);

        } else
            permissions_layout.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGettingHelperProfile(HelperProfileResponseModel helperProfileResponse) {
        if (helperProfileResponse.getStudents() != null) {
            this.studentsList.clear();
            this.studentsList.addAll(helperProfileResponse.getStudents());
            studentRecyclerViewAdapter = new HelperProfileStudentRecyclerViewAdapter(this, this, convertStudentsResponseToStudentModel(studentsList));
            RecyclerView.LayoutManager students_recyclerView_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            students_recyclerView.setLayoutManager(students_recyclerView_layoutManager);
            students_recyclerView.setItemAnimator(new DefaultItemAnimator());
            students_recyclerView.setAdapter(studentRecyclerViewAdapter);
            studentRecyclerViewAdapter.notifyDataSetChanged();
            permissions_layout.setVisibility(View.GONE);

        }
    }

    @Override
    public void onErrorGettingHelperProfile(String message) {

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
            studentModel.setPivot(response.getPivot());
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
