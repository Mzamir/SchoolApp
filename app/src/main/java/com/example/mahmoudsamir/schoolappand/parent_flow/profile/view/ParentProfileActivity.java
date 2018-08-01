package com.example.mahmoudsamir.schoolappand.parent_flow.profile.view;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.network.response.HelperResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.StudentResponseModel;
import com.example.mahmoudsamir.schoolappand.network.response.UserProfileResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.adapter.SchoolsRecyclerAdapter;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.adapter.StudentRecyclerAdapter;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.adapter.ProfileHelpersRecyclerViewAdapter;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.adapter.ProfileStudentRecyclerViewAdapter;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.presenter.ParentProfileInteactor;
import com.example.mahmoudsamir.schoolappand.parent_flow.profile.presenter.ParentProfilePresenter;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;

public class ParentProfileActivity extends AppCompatActivity implements ParentProfileViewCommunicator {


    // TODO  start implementation of this class when finishing of the realm scenario

    ParentProfilePresenter prsenter;
    ArrayList<HelperResponseModel> helperList = new ArrayList<>();
    ArrayList<StudentResponseModel> studentsList = new ArrayList<>();
    @BindView(R.id.user_profile_picture)
    SimpleDraweeView user_profile_picture;
    @BindView(R.id.user_profile_name)
    TextView user_profile_name;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.activity_parent_profile);
        prsenter = new ParentProfilePresenter(this, new ParentProfileInteactor());
        initializeView();
    }

    private void initializeView() {
        helpersRecyclerViewAdapter = new ProfileHelpersRecyclerViewAdapter(this, this, helperList);
        RecyclerView.LayoutManager schools_recyclerView_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        helpers_recyclerView.setLayoutManager(schools_recyclerView_layoutManager);
        helpers_recyclerView.setItemAnimator(new DefaultItemAnimator());
        helpers_recyclerView.setAdapter(helpersRecyclerViewAdapter);

        studentRecyclerViewAdapter = new ProfileStudentRecyclerViewAdapter(this, this, studentsList);
        RecyclerView.LayoutManager students_recyclerView_layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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
        user_profile_name.setText(user.getName());
        //        if (schoolModel.getSchoolCover() != null) {
//            Uri uri = Uri.parse(user.getSchoolCover());
//            user_profile_picture.setImageURI(uri);
//        }

        email_address.setText(user.getEmail());
        phone_number.setText(user.getPhone());
        id_number.setText(user.getNational_id());

        this.studentsList = user.getStudents();
        this.helperList = user.getHelpers();
        studentRecyclerViewAdapter = new ProfileStudentRecyclerViewAdapter(this, this, studentsList);
        students_recyclerView.setAdapter(studentRecyclerViewAdapter);

        helpersRecyclerViewAdapter = new ProfileHelpersRecyclerViewAdapter(this, this, helperList);
        helpers_recyclerView.setAdapter(helpersRecyclerViewAdapter);

        helpersRecyclerViewAdapter.notifyDataSetChanged();
        studentRecyclerViewAdapter.notifyDataSetChanged();
    }
}
