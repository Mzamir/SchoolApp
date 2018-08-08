package com.example.mahmoudsamir.schoolappand.mentor_home.view;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class MentorHomeActivity extends AppCompatActivity implements MentorHomeViewCommunicator {

    String TAG = MentorHomeActivity.class.getSimpleName();
    ArrayList<MentorStudentModel> studentList = new ArrayList<>();
    MentorHomePresenter presenter;
    @BindView(R.id.students_recyclerView)
    RecyclerView students_recyclerView;

    @BindView(R.id.deliver_students)
    Button deliver_students;

    @BindView(R.id.number_of_student_requests)
    TextView number_of_student_requests;


    MentorStudentsRecyclerViewAdapter studentRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.activity_mentor_home);
        ButterKnife.bind(this);
        presenter = new MentorHomePresenter(this, new MentorHomeInteractor());
        initializeView();
        deliver_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDeliverAction();
            }
        });
    }

    private void initializeView() {

        studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, this, studentList);
        RecyclerView.LayoutManager students_recyclerView_layoutManager = new LinearLayoutManager(this);
        students_recyclerView.setLayoutManager(students_recyclerView_layoutManager);
        students_recyclerView.setItemAnimator(new DefaultItemAnimator());
        students_recyclerView.setAdapter(studentRecyclerAdapter);
        presenter.getMentorStudent();

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
        this.studentList = studentList;
        studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, this, studentList);
        students_recyclerView.setAdapter(studentRecyclerAdapter);
        number_of_student_requests.setText(String.valueOf(requestsCounter));
    }

    @Override
    public void onSuccessDeliverAction() {

    }

    @Override
    public void onError() {
        Toast.makeText(this, GENERAL_ERROR, Toast.LENGTH_SHORT).show();
    }

    private void performDeliverAction() {
        presenter.deliverStudents(studentRecyclerAdapter.getselectedRequestList());
    }

    private void removeDeliveredStudents() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
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
            }
        });
    }
    //    private void initializePushNotification() {
//        PusherOptions options = new PusherOptions();
//        options.setCluster(PUSHER_API_CLUSTER);
//        Pusher pusher = new Pusher(PUSHER_API_KEY, options);
//        Channel channel = pusher.subscribe("my-channel");
//
//        channel.bind("my-event", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(String channelName, String eventName, final String data) {
////                System.out.println(data);
//            }
//        });
//        pusher.connect();
//    }

}
