package com.seamlabs.BlueRide.parent_flow.profile.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seamlabs.BlueRide.MyFragment;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.network.response.StudentResponseModel;
import com.seamlabs.BlueRide.network.response.UserProfileResponseModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.parent_flow.helper_profile.HelperProfileActivity;
import com.seamlabs.BlueRide.parent_flow.profile.adapter.ProfileHelpersRecyclerViewAdapter;
import com.seamlabs.BlueRide.parent_flow.profile.adapter.ProfileStudentRecyclerViewAdapter;
import com.seamlabs.BlueRide.parent_flow.profile.presenter.ParentProfileInteactor;
import com.seamlabs.BlueRide.parent_flow.profile.presenter.ParentProfilePresenter;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.HELPER_ACCOUNT;
import static com.seamlabs.BlueRide.utils.Constants.STUDENTS_LIST;

public class ParentProfileFragment extends MyFragment implements ParentProfileViewCommunicator {


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
    @BindView(R.id.profile_toolbar)
    Toolbar toolbar;

    public interface onEditProfileClickListener {
        void onEditProfileClick();

//        void onNavigationIconClick();
    }

    @BindView(R.id.user_profile_picture)
    SimpleDraweeView user_profile_picture;
    @BindView(R.id.user_profile_name)
    TextView user_profile_name;
    @BindView(R.id.edit_profile)
    ImageView edit_profile;
    @BindView(R.id.navigation_icon)
    LinearLayout navigation_icon;

    private void bindToolBarData() {
        edit_profile.setVisibility(View.VISIBLE);
        if (UserSettingsPreference.getSavedUserProfile(getActivity()).getImages().get(0) != null) {
            Uri uri = Uri.parse(UserSettingsPreference.getSavedUserProfile(getActivity()).getImages().get(0).getPath());
            user_profile_picture.setImageURI(uri);
        }
        user_profile_name.setText(UserSettingsPreference.getSavedUserProfile(getActivity()).getName());

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditProfileClickListener != null)
                    onEditProfileClickListener.onEditProfileClick();
            }
        });
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNavigationIconClickListener() != null){
                    getNavigationIconClickListener().onNavigationIconClick();
                }
//                    onEditProfileClickListener.onNavigationIconClick();
            }
        });
    }

    onEditProfileClickListener onEditProfileClickListener;

    public onEditProfileClickListener getOnEditProfileClickListener() {
        return onEditProfileClickListener;
    }

    public void setOnEditProfileClickListener(onEditProfileClickListener onEditProfileClickListener) {
        this.onEditProfileClickListener = onEditProfileClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_parent_profile, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
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
    public void onSuccessGettingUserProfile(UserProfileResponseModel userProfileResponseModel) {

        bindDateToViews(userProfileResponseModel);
    }

    @Override
    public void onErrorGettingUserProfile() {

    }

    @Override
    public void onHelperClickListener(HelperResponseModel helperModel) {
        Intent intent = new Intent(getActivity(), HelperProfileActivity.class);
        intent.putExtra(HELPER_ACCOUNT, helperModel);
        intent.putExtra(STUDENTS_LIST, studentsList);
        startActivity(intent);
    }

    @Override
    public void showPermissionActions(boolean show) {

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

    @Override
    public void onStart() {
        super.onStart();
        bindToolBarData();
        prsenter.getUserProfile();
    }
}