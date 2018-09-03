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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.channel.User;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.MyFragment;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.mentor_home.adapter.MentorSectionedGridRecyclerViewAdapter;
import com.seamlabs.BlueRide.mentor_home.adapter.MentorStudentsRecyclerViewAdapter;
import com.seamlabs.BlueRide.mentor_home.model.MentorStudentModel;
import com.seamlabs.BlueRide.mentor_home.presenter.MentorHomeInteractor;
import com.seamlabs.BlueRide.mentor_home.presenter.MentorHomePresenter;
import com.seamlabs.BlueRide.network.requests.UpdateLocationRequestModel;
import com.seamlabs.BlueRide.network.response.MentorPusherMainResponseModel;
import com.seamlabs.BlueRide.network.response.MentorQueueResponseModel;
import com.seamlabs.BlueRide.network.response.StudentResponseModel;
import com.seamlabs.BlueRide.network.response.UpdateLocationResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.DELIVERD_TO_SUPERVISON;
import static com.seamlabs.BlueRide.utils.Constants.GENERAL_ERROR;
import static com.seamlabs.BlueRide.utils.Constants.MENTOR_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.PARENT_ARRIVED_STATE;
import static com.seamlabs.BlueRide.utils.Constants.PENDING_STATE;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_CLUSTER;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_KEY;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_MENTOR_CHANEL_NAME;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_MENTOR_EVENT_NAME;
import static com.seamlabs.BlueRide.utils.Constants.REPORTED_STATE;
import static com.seamlabs.BlueRide.utils.Constants.TEACHER_USER_TYPE;

public class MentorHomeFragment extends MyFragment implements MentorHomeViewCommunicator {

    Pusher pusher;
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

    @BindView(R.id.profile_toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.user_profile_picture)
    SimpleDraweeView user_profile_picture;
    @BindView(R.id.user_profile_name)
    TextView user_profile_name;
    @BindView(R.id.edit_profile)
    ImageView edit_profile;
    @BindView(R.id.navigation_icon)
    LinearLayout navigation_icon;

    MentorSectionedGridRecyclerViewAdapter.Section[] dummy;
    MentorSectionedGridRecyclerViewAdapter mSectionedAdapter;
    List<MentorSectionedGridRecyclerViewAdapter.Section> sections;


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
        initializeView();
        deliver_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performDeliverAction();
            }
        });
        initializePushNotification();
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNavigationIconClickListener() != null)

                    getNavigationIconClickListener().onNavigationIconClick();
            }
        });
        sections = new ArrayList<>();
        return view;
    }


    private void bindToolBarData() {
        if (UserSettingsPreference.getSavedUserProfile(getActivity()).getImages().size() > 0) {
            Uri uri = Uri.parse(UserSettingsPreference.getSavedUserProfile(getActivity()).getImages().get(0).getPath());
            user_profile_picture.setImageURI(uri);
        }
        user_profile_name.setText(UserSettingsPreference.getSavedUserProfile(getActivity()).getName());

        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNavigationIconClickListener() != null) {
                    getNavigationIconClickListener().onNavigationIconClick();
                }
            }
        });
    }

    private void initializeView() {

        initializeAdapter();
        if (UserSettingsPreference.getUserType(activity).equals(TEACHER_USER_TYPE))
            presenter.getTeacherStudents();
        else if (UserSettingsPreference.getUserType(activity).equals(MENTOR_USER_TYPE))
            presenter.getMentorStudent();

    }

    private void initializeAdapter() {
        studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
        RecyclerView.LayoutManager students_recyclerView_layoutManager = new LinearLayoutManager(activity);
        students_recyclerView.setLayoutManager(students_recyclerView_layoutManager);
        students_recyclerView.setItemAnimator(new DefaultItemAnimator());
        students_recyclerView.setAdapter(studentRecyclerAdapter);
    }

    private void showStudentRecyclerView(boolean show) {
        if (show) {
            mentor_students_layout.setVisibility(View.VISIBLE);
            no_students_tv.setVisibility(View.GONE);
        } else {
            number_of_student_requests.setText(String.valueOf("0"));
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
    public void onSuccessGettingStudents(ArrayList<MentorStudentModel> studentModels, int requestsCounter) {
        this.studentList.clear();
        this.studentList = new ArrayList<>(studentModels);
        if (studentModels.size() > 0) {
            if (UserSettingsPreference.getUserType(activity).equals(MENTOR_USER_TYPE)) {
                studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
                students_recyclerView.setAdapter(studentRecyclerAdapter);
//                sectionTheStudentsBasedOnRequests();
            } else {
                studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
                students_recyclerView.setAdapter(studentRecyclerAdapter);
            }
            setStudentList(studentList);
            showStudentRecyclerView(true);
        } else {
            showStudentRecyclerView(false);
        }
        savePendingStudentsToShared();
        updateRequestCounter();
    }

    private void updateRequestCounter() {
        if (UserSettingsPreference.getUserType(activity).equals(TEACHER_USER_TYPE))
            number_of_student_requests.setText(String.valueOf(studentRecyclerAdapter.getItemCount()));
        else if (UserSettingsPreference.getUserType(activity).equals(MENTOR_USER_TYPE))
            number_of_student_requests.setText(String.valueOf(studentRecyclerAdapter.getNumberOfUniqeRequest()));
    }

    private void savePendingStudentsToShared() {
        ArrayList<MentorStudentModel> pendingStudents = new ArrayList<>();
        for (MentorStudentModel studentModel : studentList) {
            if (studentModel.getRequestState().equals(PENDING_STATE)) {
                pendingStudents.add(studentModel);
            }
        }
        UserSettingsPreference.savePendingStudentsToShare(pendingStudents);
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
        if (UserSettingsPreference.getUserType(activity).equals(TEACHER_USER_TYPE))
            presenter.teachDeliverStudents(studentRecyclerAdapter.getselectedTeacherRequestList());
        else if (UserSettingsPreference.getUserType(activity).equals(MENTOR_USER_TYPE))
            presenter.deliverStudents(studentRecyclerAdapter.getselectedRequestList());


    }

    private void removeDeliveredStudents() {
        if (UserSettingsPreference.getUserType(activity).equals(TEACHER_USER_TYPE))
            presenter.getTeacherStudents();
        else if (UserSettingsPreference.getUserType(activity).equals(MENTOR_USER_TYPE))
            presenter.getMentorStudent();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pusher != null)
            pusher.connect();
    }

    public ArrayList<MentorStudentModel> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<MentorStudentModel> studentList) {
        this.studentList = studentList;
    }

    private void initializePushNotification() {
        PusherOptions options = new PusherOptions();
        options.setCluster(PUSHER_API_CLUSTER);
        pusher = new Pusher(PUSHER_API_KEY, options);
        Channel channel = pusher.subscribe(PUSHER_MENTOR_CHANEL_NAME + UserSettingsPreference.getSavedUserProfile(activity).getId());
        channel.bind(PUSHER_MENTOR_EVENT_NAME, new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                try {
                    final JSONObject jsonObject = new JSONObject(data);
                    if (jsonObject.get("request").getClass() == Integer.class) {
                        final int requestID = jsonObject.getInt("request");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                deleteRequestFromTheList(requestID);
                            }
                        });
                    } else {
                        Gson gson = new Gson();
                        final MentorPusherMainResponseModel responseModel = gson.fromJson(data, MentorPusherMainResponseModel.class);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (UserSettingsPreference.getUserType(activity).equals(MENTOR_USER_TYPE))
                                    updateList(responseModel);
                                else if (UserSettingsPreference.getUserType(activity).equals(TEACHER_USER_TYPE))
                                    updateTeacherList(responseModel);
                                Log.i(TAG, "Pusher response " + new Gson().toJson(responseModel));
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pusher.connect();
    }

    private void updateList(MentorPusherMainResponseModel responseModel) {
        boolean found = false;
        for (MentorStudentModel studentModel : studentList) {
            if (studentModel.getRequestId() == responseModel.getMentorPusherEventResponseModel().getId()) {
                String requestState = responseModel.getMentorPusherEventResponseModel().getStatus();
                if (requestState.equals("pending"))
                    studentModel.setRequestState(PENDING_STATE);
                else if (requestState.equals("parent_arrived")) {
                    studentModel.setRequestState(PARENT_ARRIVED_STATE);
                } else if (requestState.equals("reported")) {
                    studentModel.setRequestState(REPORTED_STATE);
                } else if (requestState.equals("delivered_to_supervisor")) {
                    studentModel.setRequestState(DELIVERD_TO_SUPERVISON);
                }
                found = true;
            }
        }
        if (!found) {
            this.studentList = convertStudentsResponseToStudentModel(responseModel);
        } else {
            this.studentList = presenter.sortStudentsBasedOnPriority(studentList);
        }
        if (studentList.size() > 0) {
            showStudentRecyclerView(true);
            studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
            students_recyclerView.setAdapter(studentRecyclerAdapter);
            setStudentList(studentList);
        } else {
            showStudentRecyclerView(false);
        }
        updateRequestCounter();
    }

    private void updateTeacherList(MentorPusherMainResponseModel responseModel) {
        ArrayList<MentorStudentModel> newRequests = new ArrayList<>();
        boolean found = false;
        if (studentList.size() > 0) {
            for (StudentResponseModel studentResponseModel : responseModel.getMentorPusherEventResponseModel().getStudents()) {
                for (MentorStudentModel studentModel : studentList) {
                    if (studentModel.getStudentID() == studentResponseModel.getId()) {
                        String requestState = responseModel.getMentorPusherEventResponseModel().getStatus();
                        if (requestState.equals("pending"))
                            studentModel.setRequestState(PENDING_STATE);
                        else if (requestState.equals("parent_arrived")) {
                            studentModel.setRequestState(PARENT_ARRIVED_STATE);
                        } else if (requestState.equals("reported")) {
                            studentModel.setRequestState(REPORTED_STATE);
                        } else if (requestState.equals("delivered_to_supervisor")) {
                            studentModel.setRequestState(DELIVERD_TO_SUPERVISON);
                        }
                        found = true;
                    }
                }
                if (!found) {
                    found = false;
                    newRequests.add(convertStudentResponseToStudentModelObject(responseModel));
                }
            }
            studentList.addAll(newRequests);
            this.studentList = presenter.sortStudentsBasedOnPriority(studentList);
            newRequests.clear();
        } else {
            this.studentList = convertStudentsResponseToStudentModel(responseModel);
        }
        if (studentList.size() > 0) {
            showStudentRecyclerView(true);
            studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
            students_recyclerView.setAdapter(studentRecyclerAdapter);
            setStudentList(studentList);
        } else {
            showStudentRecyclerView(false);
        }
        updateRequestCounter();
    }

    private void deleteRequestFromTheList(int requestId) {
        try {
            ArrayList<MentorStudentModel> notDeletedStudents = new ArrayList<>();
            for (MentorStudentModel studentModel : studentList) {
                if (studentModel.getRequestId() == requestId) {
                } else {
                    notDeletedStudents.add(studentModel);
                }
            }
            studentList.clear();
            studentList = new ArrayList<>();
            studentList.addAll(notDeletedStudents);
            if (studentList.size() > 0) {
                showStudentRecyclerView(true);
                this.studentList = presenter.sortStudentsBasedOnPriority(studentList);
                studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
                students_recyclerView.setAdapter(studentRecyclerAdapter);
                setStudentList(studentList);
                updateRequestCounter();
            } else {
                showStudentRecyclerView(false);
            }
        } catch (Exception e) {

        }
    }

    public ArrayList<MentorStudentModel> convertStudentsResponseToStudentModel(MentorPusherMainResponseModel responseModel) {
        ArrayList<MentorStudentModel> studentModels = new ArrayList<>();
        for (StudentResponseModel studentResponseModel : responseModel.getMentorPusherEventResponseModel().getStudents()) {
            MentorStudentModel studentModel = new MentorStudentModel();
            studentModel.setMarked(false);
            studentModel.setMentorCanDeliver(responseModel.getMentorPusherEventResponseModel().isMentor_can_deliver());
            studentModel.setStudentID(studentResponseModel.getId());
            studentModel.setStudentName(studentResponseModel.getName());
            studentModel.setStudentNationalID(studentResponseModel.getNational_id());
            studentModel.setSchoolID(studentResponseModel.getSchool_id());
            studentModel.setClassID(studentResponseModel.getClass_id());
            studentModel.setClass_name(studentResponseModel.getClass_name());
            studentModel.setGrade_name(studentResponseModel.getGrade_name());
            studentModel.setStudentCreatedAt(studentResponseModel.getCreated_at());
            studentModel.setStudentUpdatedAt(studentResponseModel.getUpdated_at());
            studentModel.setRequestId(responseModel.getMentorPusherEventResponseModel().getId());
            String requestState = responseModel.getMentorPusherEventResponseModel().getStatus();
            if (requestState.equals("pending"))
                studentModel.setRequestState(PENDING_STATE);
            else if (requestState.equals("parent_arrived")) {
                studentModel.setRequestState(PARENT_ARRIVED_STATE);
            } else if (requestState.equals("reported")) {
                studentModel.setRequestState(REPORTED_STATE);
            } else if (requestState.equals("delivered_to_supervisor")) {
                studentModel.setRequestState(DELIVERD_TO_SUPERVISON);
            }
            if (studentResponseModel.getImages().size() > 0) {
                studentModel.setStudentPicture(studentResponseModel.getImages().get(0).getPath());
            }
            studentModels.add(studentModel);
        }
        this.studentList.addAll(studentModels);
        return presenter.sortStudentsBasedOnPriority(studentList);
    }

    public MentorStudentModel convertStudentResponseToStudentModelObject(MentorPusherMainResponseModel responseModel) {
        MentorStudentModel studentModel = new MentorStudentModel();
        for (StudentResponseModel studentResponseModel : responseModel.getMentorPusherEventResponseModel().getStudents()) {
            studentModel.setMarked(false);
            studentModel.setMentorCanDeliver(responseModel.getMentorPusherEventResponseModel().isMentor_can_deliver());
            studentModel.setStudentID(studentResponseModel.getId());
            studentModel.setStudentName(studentResponseModel.getName());
            studentModel.setStudentNationalID(studentResponseModel.getNational_id());
            studentModel.setSchoolID(studentResponseModel.getSchool_id());
            studentModel.setClassID(studentResponseModel.getClass_id());
            studentModel.setClass_name(studentResponseModel.getClass_name());
            studentModel.setGrade_name(studentResponseModel.getGrade_name());
            studentModel.setStudentCreatedAt(studentResponseModel.getCreated_at());
            studentModel.setStudentUpdatedAt(studentResponseModel.getUpdated_at());
            studentModel.setRequestId(responseModel.getMentorPusherEventResponseModel().getId());
            String requestState = responseModel.getMentorPusherEventResponseModel().getStatus();
            if (requestState.equals("pending"))
                studentModel.setRequestState(PENDING_STATE);
            else if (requestState.equals("parent_arrived")) {
                studentModel.setRequestState(PARENT_ARRIVED_STATE);
            } else if (requestState.equals("reported")) {
                studentModel.setRequestState(REPORTED_STATE);
            } else if (requestState.equals("delivered_to_supervisor")) {
                studentModel.setRequestState(DELIVERD_TO_SUPERVISON);
            }
            if (studentResponseModel.getImages().size() > 0) {
                studentModel.setStudentPicture(studentResponseModel.getImages().get(0).getPath());
            }
        }
        return studentModel;
    }

    @Override
    public void onStart() {
        super.onStart();
        bindToolBarData();
        if (pusher != null)
            pusher.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pusher != null)
            pusher.disconnect();
    }


    private void sectionTheStudentsBasedOnRequests() {
        MentorStudentsRecyclerViewAdapter studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, activity, studentList);
        createSections();
        dummy = new MentorSectionedGridRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new
                MentorSectionedGridRecyclerViewAdapter(activity, R.layout.mentor_grid_sections, R.id.section_text, students_recyclerView, studentRecyclerAdapter, activity);
        mSectionedAdapter.setSections(sections.toArray(dummy));
        students_recyclerView.setAdapter(mSectionedAdapter);
    }

    private void createSections() {
        ArrayList<Integer> uniqueRequestsList = getUniqueRequestsCounter();
        for (int requestID : uniqueRequestsList) {
            sections.add(new MentorSectionedGridRecyclerViewAdapter.Section(requestID, String.valueOf(requestID)));
        }
    }

    private ArrayList<Integer> getUniqueRequestsCounter() {
        Set<Integer> selectedRequestList = new HashSet<>();
        for (MentorStudentModel studentModel : studentList) {
            selectedRequestList.add(studentModel.getRequestId());
        }
        ArrayList<Integer> temp = new ArrayList<>(selectedRequestList);
        return temp;
    }

}
