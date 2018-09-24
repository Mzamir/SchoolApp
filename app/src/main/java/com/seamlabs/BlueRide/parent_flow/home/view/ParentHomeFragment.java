package com.seamlabs.BlueRide.parent_flow.home.view;

import android.Manifest;
import android.app.Activity;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.seamlabs.BlueRide.MyFragment;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.parent_flow.pick_up.view.MapsActivity;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.requests.ParentPickUpRequestModel;
import com.seamlabs.BlueRide.parent_flow.home.adapter.SchoolsRecyclerAdapter;
import com.seamlabs.BlueRide.parent_flow.home.adapter.StudentRecyclerAdapter;
import com.seamlabs.BlueRide.parent_flow.home.model.SchoolModel;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;
import com.seamlabs.BlueRide.parent_flow.home.presenter.ParentHomeInteractor;
import com.seamlabs.BlueRide.parent_flow.home.presenter.ParentHomePresenter;
import com.seamlabs.BlueRide.parent_flow.profile.view.ParentProfileFragment;
import com.seamlabs.BlueRide.utils.MessageEvent;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.EVENT_NOTIFICATION_RECEIVED;
import static com.seamlabs.BlueRide.utils.Constants.EVENT_PICTURE_CHANGED;
import static com.seamlabs.BlueRide.utils.Constants.PICK_UP_REQUEST_MODEL;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_CLUSTER;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_KEY;
import static com.seamlabs.BlueRide.utils.Constants.SELECTED_SCHOOL_MODEL;

public class ParentHomeFragment extends MyFragment implements ParentHomeViewCommunicator {

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

    @BindView(R.id.notification_count)
    TextView notification_count;
    @BindView(R.id.notification_icon_layout)
    FrameLayout notification_icon_layout;

    @BindView(R.id.parent_home_toolbar)
    Toolbar parent_home_toolbar;
    @BindView(R.id.navigation_icon)
    LinearLayout navigation_icon;

    ParentHomePresenter presenter;
    SchoolsRecyclerAdapter schoolsRecyclerAdapter;
    StudentRecyclerAdapter studentRecyclerAdapter;
    ArrayList<SchoolModel> schoolsList = new ArrayList<>();
    ArrayList<StudentModel> studentList = new ArrayList<>();

    public interface onNotificationIconClickListener {
        void onNotificationIconClick();

    }

    onNotificationIconClickListener onNotificationIconClickListener;

    public onNotificationIconClickListener getOnNotificationIconClickListener() {
        return onNotificationIconClickListener;
    }

    public void setOnNotificationIconClickListener(onNotificationIconClickListener onNotificationIconClickListener) {
        this.onNotificationIconClickListener = onNotificationIconClickListener;
    }

    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_home, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(parent_home_toolbar);
        initializeView();
        activity = getActivity();
        presenter = new ParentHomePresenter(this, new ParentHomeInteractor());
        EventBus.getDefault().register(this);
        presenter.getParentSchools();
        presenter.updateUserData();
        startPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (schoolsRecyclerAdapter.getSelecteSchoolID() >= 0 && studentRecyclerAdapter.getSelectedStudent().size() > 0) {
                    if (checkGPSPermission()) {
                        navigateToPickingScreen();
                    }
                } else {
                    showSnackBar(getResources().getString(R.string.deliver_non_student_error), false);
                }
            }
        });
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNavigationIconClickListener() != null) {
                    getNavigationIconClickListener().onNavigationIconClick();
                }
            }
        });

        notification_icon_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNotificationIconClickListener != null)
                    onNotificationIconClickListener.onNotificationIconClick();
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
        Utility.showProgressDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        Utility.hideProgressDialog();
    }

    @Override
    public void onErrorGettingSchools(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccessGettingSchool(ArrayList<SchoolModel> schoolList) {
        if (schoolList.size() > 0) {
            this.schoolsList = schoolList;
            schoolsRecyclerAdapter = new SchoolsRecyclerAdapter(this, getContext(), schoolList);
            schools_recyclerView.setAdapter(schoolsRecyclerAdapter);
            startPickup.setVisibility(View.VISIBLE);
        } else {
            startPickup.setVisibility(View.INVISIBLE);
            showSnackBar("You don't have schools right now", false);
        }
    }

    @Override
    public void onSuccessGettingStudentForASchool(ArrayList<StudentModel> studentList) {
        this.studentList = studentList;
        studentRecyclerAdapter = new StudentRecyclerAdapter(getContext(), studentList);
        students_recyclerView.setAdapter(studentRecyclerAdapter);
    }

    @Override
    public void getStudentsForASchool(String schoolId) {
        presenter.getParentStudentForASchool(schoolId);
    }

    @Override
    public void onSuccessGettingUserData() {
        try {
            UserResponseModel userProfileModel = UserSettingsPreference.getSavedUserProfile(getActivity());
            Log.i(TAG, "notification_count " + userProfileModel.getUnreadNotifications());
            if (userProfileModel.getUnreadNotifications() > 0) {
                notification_count.setText(String.valueOf(userProfileModel.getUnreadNotifications()));
                notification_count.setVisibility(View.VISIBLE);
            } else {
                notification_count.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "onSuccessGettingUserData " + e.getMessage().toString());
        }
    }

    private boolean checkGPSPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation(getResources().getString(R.string.permission_needed),
                        getString(R.string.location_permission_explanation), Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_GPS_STATE);
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
                    Toast.makeText(getContext(), getResources().getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                    navigateToPickingScreen();
                } else {
                    boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!showRationale) {
                        showSnackBar(getResources().getString(R.string.permission_denied), true);
                    } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0])) {
                        Toast.makeText(getContext(), getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
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

        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.putExtra(PICK_UP_REQUEST_MODEL, parentPickUpRequestModel);
        intent.putExtra(SELECTED_SCHOOL_MODEL, schoolsRecyclerAdapter.getSelectedSchoolModel());
        startActivity(intent);
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

    @Subscribe
    public void onEvent(final MessageEvent event) {
        Log.i(TAG, "MessageEvent" + event.getMessage());
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (event.getMessage().equals(EVENT_NOTIFICATION_RECEIVED)) {
                        int notificationCounter = Integer.parseInt(notification_count.getText().toString());
                        notification_count.setText(String.valueOf(notificationCounter + 1));
                        notification_count.setVisibility(View.VISIBLE);
                    }
                }
            });

        } catch (Exception e) {
            Log.i(TAG, "Change picture" + e.getMessage().toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EventBus.getDefault().isRegistered(this) == false)
            EventBus.getDefault().register(this);
    }
}
