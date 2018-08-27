package com.seamlabs.BlueRide.mentor_home.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.MyFragment;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.mentor_home.adapter.MentorStudentsRecyclerViewAdapter;
import com.seamlabs.BlueRide.mentor_home.model.MentorStudentModel;
import com.seamlabs.BlueRide.mentor_home.presenter.MentorHomeInteractor;
import com.seamlabs.BlueRide.mentor_home.presenter.MentorHomePresenter;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.MENTOR_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.TEACHER_USER_TYPE;
import static com.seamlabs.BlueRide.utils.UserSettingsPreference.getUserType;

public class MentorPendingFragment extends MyFragment implements MentorHomeViewCommunicator {

    Activity activity;
    String TAG = MentorPendingFragment.class.getSimpleName();
    ArrayList<MentorStudentModel> studentList = new ArrayList<>();
    MentorHomePresenter presenter;
    @BindView(R.id.students_recyclerView)
    RecyclerView students_recyclerView;

    @BindView(R.id.deliver_students)
    Button deliver_students;

    @BindView(R.id.number_of_student_requests)
    TextView number_of_student_requests;

    @BindView(R.id.no_students_tv)
    TextView no_students_tv;

    @BindView(R.id.mentor_students_layout)
    LinearLayout mentor_students_layout;

    @BindView(R.id.number_of_student_requests_layout)
    LinearLayout number_of_student_requests_layout;

    MentorStudentsRecyclerViewAdapter studentRecyclerAdapter;

    public ArrayList<MentorStudentModel> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<MentorStudentModel> mentorStudentModels) {
        for (MentorStudentModel mentorStudentModel : mentorStudentModels) {
            if (mentorStudentModel.getRequestState().equals("pending"))
                this.studentList.add(mentorStudentModel);
        }
    }
    @BindView(R.id.profile_toolbar)
    Toolbar toolbar ;

    @BindView(R.id.user_profile_picture)
    SimpleDraweeView user_profile_picture;
    @BindView(R.id.user_profile_name)
    TextView user_profile_name;
    @BindView(R.id.edit_profile)
    ImageView edit_profile;
    @BindView(R.id.navigation_icon)
    LinearLayout navigation_icon;

    private void bindToolBarData() {
        if (UserSettingsPreference.getSavedUserProfile(getActivity()).getImages().get(0) != null) {
            Uri uri = Uri.parse(UserSettingsPreference.getSavedUserProfile(getActivity()).getImages().get(0).getPath());
            user_profile_picture.setImageURI(uri);
        }
        user_profile_name.setText(UserSettingsPreference.getSavedUserProfile(getActivity()).getName());

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showEditProfileFragment();
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mentor_home, container, false);
        activity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        presenter = new MentorHomePresenter(this, new MentorHomeInteractor());
        number_of_student_requests_layout.setVisibility(View.GONE);
        initializeView();
        deliver_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDeliverAction();
            }
        });
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNavigationIconClickListener()!=null)
                    getNavigationIconClickListener().onNavigationIconClick();
            }
        });
        return view;
    }

    private void initializeView() {
        studentList = getStudentList();
        if (studentList.size() > 0) {
            showStudentRecyclerView(true);
            studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
            RecyclerView.LayoutManager students_recyclerView_layoutManager = new LinearLayoutManager(activity);
            students_recyclerView.setLayoutManager(students_recyclerView_layoutManager);
            students_recyclerView.setItemAnimator(new DefaultItemAnimator());
            students_recyclerView.setAdapter(studentRecyclerAdapter);
        } else {
            showStudentRecyclerView(false);
        }

    }

    private void showStudentRecyclerView(boolean show) {
        if (show) {
            mentor_students_layout.setVisibility(View.VISIBLE);
            no_students_tv.setVisibility(View.GONE);
        } else {
            mentor_students_layout.setVisibility(View.GONE);
            no_students_tv.setVisibility(View.VISIBLE);
        }
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
    public void showDeliveryAction(boolean showDeliveryAction) {
        if (showDeliveryAction)
            deliver_students.setVisibility(View.VISIBLE);
        else
            deliver_students.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSuccessGettingStudents(ArrayList<MentorStudentModel> studentList, int requestsCounter) {
        this.studentList = studentList;
        studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
        students_recyclerView.setAdapter(studentRecyclerAdapter);
    }

    @Override
    public void onSuccessDeliverAction() {
        removeDeliveredStudents();
    }

    @Override
    public void onError() {
        Toast.makeText(activity, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
    }

    private void performDeliverAction() {
        if (getUserType(activity).equals(MENTOR_USER_TYPE))
            presenter.deliverStudents(studentRecyclerAdapter.getselectedRequestList());
        else if (getUserType(activity).equals(TEACHER_USER_TYPE))
            presenter.teachDeliverStudents(studentRecyclerAdapter.getselectedTeacherRequestList());

    }

    private void removeDeliveredStudents() {
        presenter.deliverStudents(studentRecyclerAdapter.getselectedRequestList());
    }

    @Override
    public void onResume() {
        super.onResume();
        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(activity, new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                String messagePayload = remoteMessage.getData().get("myMessagePayload");
                if (messagePayload == null) {
                    // Message payload was not set for this notification
                    Log.i(TAG, "Payload was missing");
                } else {
                    Log.i(TAG, messagePayload);
                    // Now update the UI based on your message payload!
                }
                removeDeliveredStudents();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        bindToolBarData();
    }
}