package com.example.mahmoudsamir.schoolappand.parent_flow.home.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.network.requests.ParentPickUpRequestModel;
import com.example.mahmoudsamir.schoolappand.network.response.ParentPickUpResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.adapter.SchoolsRecyclerAdapter;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.adapter.StudentRecyclerAdapter;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.model.SchoolModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.model.StudentModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.presenter.ParentHomeInteractor;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.presenter.ParentHomePresenter;
import com.example.mahmoudsamir.schoolappand.parent_flow.pick_up.view.ParentPickUpActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.ERROR;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.PICK_REQUEST_ID;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.SELECTED_SCHOOL_MODEL;

public class ParentHomeFragment extends Fragment implements ParentHomeViewCommunicator {

    private static final int REQUEST_PERMISSION_GPS_STATE = 1;
    String TAG = ParentHomeFragment.class.getSimpleName();
    int selectedSchoolID;
    ArrayList<Integer> selecteStudents = new ArrayList<>();

    @BindView(R.id.schools_recyclerView)
    RecyclerView schools_recyclerView;

    @BindView(R.id.students_recyclerView)
    RecyclerView students_recyclerView;

    @BindView(R.id.startPickup)
    Button startPickup;

    ParentHomePresenter presenter;
    SchoolsRecyclerAdapter schoolsRecyclerAdapter;
    StudentRecyclerAdapter studentRecyclerAdapter;
    ArrayList<SchoolModel> schoolsList = new ArrayList<>();
    ArrayList<StudentModel> studentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_home, container, false);
        ButterKnife.bind(this, view);
        initializeView();
        presenter = new ParentHomePresenter(this, new ParentHomeInteractor());
        presenter.getParentSchools();

        startPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (schoolsRecyclerAdapter.getSelecteSchoolID() >= 0 && studentRecyclerAdapter.getSelectedStudent().size() >= 0) {
                    if (checkGPSPermission()) {
                        navigateToPickingScreen();
                    }
                } else {
                    showSnackBar("Select the school and your students first", false);
                }
            }
        });
        return view;
    }

    private void initializeView() {
        schoolsRecyclerAdapter = new SchoolsRecyclerAdapter(this, getContext(), schoolsList);
        RecyclerView.LayoutManager schools_recyclerView_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        schools_recyclerView.setLayoutManager(schools_recyclerView_layoutManager);
        schools_recyclerView.setItemAnimator(new DefaultItemAnimator());
        schools_recyclerView.setAdapter(schoolsRecyclerAdapter);

        studentRecyclerAdapter = new StudentRecyclerAdapter(getContext(), studentList);
        RecyclerView.LayoutManager students_recyclerView_layoutManager = new LinearLayoutManager(getContext());
        students_recyclerView.setLayoutManager(students_recyclerView_layoutManager);
        students_recyclerView.setItemAnimator(new DefaultItemAnimator());
        students_recyclerView.setAdapter(studentRecyclerAdapter);

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onErrorGettingSchools(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccessGettingSchool(ArrayList<SchoolModel> schoolList) {
        this.schoolsList = schoolList;
        schoolsRecyclerAdapter = new SchoolsRecyclerAdapter(this, getContext(), schoolList);
        schools_recyclerView.setAdapter(schoolsRecyclerAdapter);
    }

    @Override
    public void onSuccessGettingStudentForASchool(ArrayList<StudentModel> studentList) {
        this.studentList = studentList;
        studentRecyclerAdapter = new StudentRecyclerAdapter(getContext(), studentList);
        students_recyclerView.setAdapter(studentRecyclerAdapter);
    }

    @Override
    public void onSuccessPickUpRequest(ParentPickUpResponseModel responseModel) {
        if (responseModel.getid() >= 0) {
            Intent intent = new Intent(getActivity(), ParentPickUpActivity.class);
            intent.putExtra(PICK_REQUEST_ID, responseModel.getid());
            intent.putExtra(SELECTED_SCHOOL_MODEL, schoolsRecyclerAdapter.getSelectedSchoolModel());
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), ERROR, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getStudentsForASchool(String schoolId) {
        presenter.getParentStudentForASchool(schoolId);
    }

    private boolean checkGPSPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation("Permission Needed", "Location", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_GPS_STATE);
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_GPS_STATE);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_GPS_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission Granted!", Toast.LENGTH_SHORT).show();
                    navigateToPickingScreen();
                } else {
                    boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!showRationale) {
                        showSnackBar("Permission Denied!", true);
                    } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0])) {
                        Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                    }
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

    private void navigateToPickingScreen() {
        ParentPickUpRequestModel parentPickUpRequestModel = new ParentPickUpRequestModel();
        parentPickUpRequestModel.setSchool_id(schoolsRecyclerAdapter.getSelecteSchoolID());
        parentPickUpRequestModel.setStudent_ids(studentRecyclerAdapter.getSelectedStudent());

        presenter.parentPickUpRequest(parentPickUpRequestModel);
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
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
    }
}
