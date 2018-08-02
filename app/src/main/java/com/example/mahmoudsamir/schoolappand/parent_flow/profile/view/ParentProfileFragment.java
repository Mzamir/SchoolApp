package com.example.mahmoudsamir.schoolappand.parent_flow.profile.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.network.response.HelperResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.StudentResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.UserProfileResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.UserResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.adapter.ProfileHelpersRecyclerViewAdapter;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.adapter.ProfileStudentRecyclerViewAdapter;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.presenter.ParentProfileInteactor;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.presenter.ParentProfilePresenter;
import com.example.mahmoudsamir.schoolappand.utils.UserSettingsPreference;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentProfileFragment extends Fragment implements ParentProfileViewCommunicator {


    // TODO  start implementation of this class when finishing of the realm scenario

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_parent_profile, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        prsenter = new ParentProfilePresenter(this, new ParentProfileInteactor());
        initializeView();
        userProfileModel = UserSettingsPreference.getSavedUserProfile(activity);
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

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onSuccessGettingUserProfile(UserProfileResponseModel userProfileResponseModel) {

        bindDateToViews(userProfileResponseModel);
    }

    @Override
    public void onErrorGettingUserProfile() {

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
}