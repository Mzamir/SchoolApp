package com.example.mahmoudsamir.schoolappand.mentor_home.view;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.mentor_home.adapter.MentorStudentsRecyclerViewAdapter;
import com.example.mahmoudsamir.schoolappand.mentor_home.model.MentorStudentModel;
import com.example.mahmoudsamir.schoolappand.mentor_home.presenter.MentorHomeInteractor;
import com.example.mahmoudsamir.schoolappand.mentor_home.presenter.MentorHomePresenter;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.GENERAL_ERROR;

public class MentorHomeFragment extends Fragment implements MentorHomeViewCommunicator {

    Activity activity;
    String TAG = MentorHomeFragment.class.getSimpleName();
    ArrayList<MentorStudentModel> studentList = new ArrayList<>();
    MentorHomePresenter presenter;
    @BindView(R.id.students_recyclerView)
    RecyclerView students_recyclerView;

    @BindView(R.id.deliver_students)
    Button deliver_students;

    @BindView(R.id.number_of_student_requests)
    TextView number_of_student_requests;


    MentorStudentsRecyclerViewAdapter studentRecyclerAdapter;

    @BindView(R.id.mentor_students_layout)
    LinearLayout mentor_students_layout;

    @BindView(R.id.number_of_student_requests_layout)
    LinearLayout number_of_student_requests_layout;

    @BindView(R.id.no_students_tv)
    TextView no_students_tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mentor_home, container, false);
        activity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }
        ButterKnife.bind(this, view);
        presenter = new MentorHomePresenter(this, new MentorHomeInteractor());
        initializeView();
        deliver_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDeliverAction();
            }
        });
        return view;
    }

    private void initializeView() {

        studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
        RecyclerView.LayoutManager students_recyclerView_layoutManager = new LinearLayoutManager(activity);
        students_recyclerView.setLayoutManager(students_recyclerView_layoutManager);
        students_recyclerView.setItemAnimator(new DefaultItemAnimator());
        students_recyclerView.setAdapter(studentRecyclerAdapter);
        presenter.getMentorStudent();

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

    }

    @Override
    public void hideProgress() {

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
        if (studentList.size() > 0) {
            showStudentRecyclerView(true);
            this.studentList = studentList;
            studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
            students_recyclerView.setAdapter(studentRecyclerAdapter);
            setStudentList(studentList);
            number_of_student_requests.setText(String.valueOf(requestsCounter));
        } else {
            showStudentRecyclerView(false);
        }
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
        presenter.deliverStudents(studentRecyclerAdapter.getselectedRequestList());
    }

    private void removeDeliveredStudents() {
        presenter.getMentorStudent();
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

    public ArrayList<MentorStudentModel> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<MentorStudentModel> studentList) {
        this.studentList = studentList;
    }
}
