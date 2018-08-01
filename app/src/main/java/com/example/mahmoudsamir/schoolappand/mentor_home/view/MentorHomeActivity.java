package com.example.mahmoudsamir.schoolappand.mentor_home.view;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.mentor_home.adapter.MentorStudentsRecyclerViewAdapter;
import com.example.mahmoudsamir.schoolappand.mentor_home.model.MentorStudentModel;
import com.example.mahmoudsamir.schoolappand.mentor_home.presenter.MentorHomeInteractor;
import com.example.mahmoudsamir.schoolappand.mentor_home.presenter.MentorHomePresenter;
import com.example.mahmoudsamir.schoolappand.network.response.MentorQueueResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.adapter.SchoolsRecyclerAdapter;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.adapter.StudentRecyclerAdapter;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.model.StudentModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.ERROR;

public class MentorHomeActivity extends AppCompatActivity implements MentorHomeViewCommunicator {

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
    public void onSuccessGettingStudents(ArrayList<MentorStudentModel> studentList) {
        this.studentList = studentList;
        studentRecyclerAdapter = new MentorStudentsRecyclerViewAdapter(this, this, studentList);
        students_recyclerView.setAdapter(studentRecyclerAdapter);
    }

    @Override
    public void onSuccessDeliverAction() {

    }

    @Override
    public void onError() {
        Toast.makeText(this, ERROR, Toast.LENGTH_SHORT).show();
    }

    private void performDeliverAction() {
        presenter.deliverStudents(studentRecyclerAdapter.getselectedRequestList());
    }

    private void removeDeliveredStudents() {

    }
}
