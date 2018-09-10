package com.seamlabs.BlueRide.mentor_home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.mentor_home.model.MentorStudentModel;
import com.seamlabs.BlueRide.mentor_home.view.MentorHomeViewCommunicator;
import com.seamlabs.BlueRide.network.requests.TeacherDeliverStudentsRequestModel;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;
import com.facebook.drawee.view.SimpleDraweeView;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.DELIVERD_TO_SUPERVISON;
import static com.seamlabs.BlueRide.utils.Constants.MENTOR_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.PARENT_ARRIVED_STATE;
import static com.seamlabs.BlueRide.utils.Constants.PENDING_STATE;
import static com.seamlabs.BlueRide.utils.Constants.REPORTED_STATE;
import static com.seamlabs.BlueRide.utils.Constants.TEACHER_USER_TYPE;

public class MentorStudentsRecyclerViewAdapter extends RecyclerView.Adapter<MentorStudentsRecyclerViewAdapter.StudentsViewHolderLayout> {

    Context context;
    ArrayList<MentorStudentModel> students = new ArrayList<>();
    MentorHomeViewCommunicator communicator;

    public MentorStudentsRecyclerViewAdapter(MentorHomeViewCommunicator communicator, Context context, ArrayList<MentorStudentModel> schools) {
        this.context = context;
        this.students = schools;
        this.communicator = communicator;
    }

    @NonNull
    @Override
    public StudentsViewHolderLayout onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mentor_students_recyclerview_item, parent, false);
        StudentsViewHolderLayout studentsViewHolderLayout = new StudentsViewHolderLayout(v);
        return studentsViewHolderLayout;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewHolderLayout holder, int position) {
        final MentorStudentModel studentModel = students.get(position);

        if (studentModel.isMarked())
            holder.marked_icon.setVisibility(View.VISIBLE);
        else
            holder.marked_icon.setVisibility(View.INVISIBLE);
        if (studentModel.getStudentPicture() != null) {
            Uri uri = Uri.parse(studentModel.getStudentPicture());
            holder.student_picture.setImageURI(uri);
        }
        holder.student_class.setText(String.valueOf(studentModel.getClass_name()));
        holder.student_grade.setText(String.valueOf(studentModel.getGrade_name()));
        holder.student_name.setText(studentModel.getStudentName());
        if (studentModel.getRequestState().equals(PENDING_STATE)) {
            holder.student_state.setTextColor(context.getResources().getColor(R.color.pending_state));
        } else if (studentModel.getRequestState().equals(PARENT_ARRIVED_STATE)) {
            holder.student_state.setTextColor(context.getResources().getColor(R.color.parent_arrived));
        } else if (studentModel.getRequestState().equals(REPORTED_STATE)) {
            holder.student_state.setTextColor(context.getResources().getColor(R.color.report_state));
        } else if (studentModel.getRequestState().equals(REPORTED_STATE)) {
            holder.student_state.setTextColor(context.getResources().getColor(R.color.report_state));
        }
        holder.student_state.setText(studentModel.getRequestState());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    MentorStudentModel studentModel = students.get(position);
                    if (UserSettingsPreference.getUserType(MyApplication.getMyApplicationContext()).equals(MENTOR_USER_TYPE)) {
                        if (studentModel.getRequestState().equals(DELIVERD_TO_SUPERVISON)) {
                            studentModel.setMarked(!studentModel.isMarked());
                            showDeliverAction();
                            notifyItemChanged(position);
                        } else if (studentModel.getRequestState().equals(REPORTED_STATE) && studentModel.isMentorCanDeliver()) {
                            studentModel.setMarked(!studentModel.isMarked());
                            showDeliverAction();
                            notifyItemChanged(position);
                        } else {
                            Toast.makeText(MyApplication.getMyApplicationContext(), context.getResources().getString(R.string.cant_deliver_students), Toast.LENGTH_SHORT).show();
                        }
                    } else if (UserSettingsPreference.getUserType(MyApplication.getMyApplicationContext()).equals(TEACHER_USER_TYPE)) {
                        if (studentModel.getRequestState().equals(PARENT_ARRIVED_STATE) || studentModel.getRequestState().equals(REPORTED_STATE)) {
                            studentModel.setMarked(!studentModel.isMarked());
                            showDeliverAction();
                            notifyItemChanged(position);
                        } else {
                            Toast.makeText(MyApplication.getMyApplicationContext(), context.getResources().getString(R.string.cant_deliver_students), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    class StudentsViewHolderLayout extends ViewHolder {

        @BindView(R.id.student_picture)
        SimpleDraweeView student_picture;

        @BindView(R.id.student_name)
        TextView student_name;

        @BindView(R.id.student_class)
        TextView student_class;

        @BindView(R.id.student_grade)
        TextView student_grade;

        @BindView(R.id.student_state)
        TextView student_state;

        @BindView(R.id.marked_icon)
        ImageView marked_icon;

        public StudentsViewHolderLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    void showDeliverAction() {
        for (MentorStudentModel model : students) {
            if (model.isMarked()) {
                communicator.showDeliveryAction(true);
                return;
            }
        }
        communicator.showDeliveryAction(false);
    }

    public ArrayList<Integer> getselectedRequestList() {
        Set<Integer> selectedRequestList = new HashSet<>();
        for (MentorStudentModel studentModel : students) {
            if (studentModel.isMarked()) {
                selectedRequestList.add(studentModel.getRequestId());
            }
        }
        ArrayList<Integer> temp = new ArrayList<>(selectedRequestList);
        return temp;
    }

    public TeacherDeliverStudentsRequestModel getselectedTeacherRequestList() {
        ArrayList<Integer> request_ids = new ArrayList<>();
        ArrayList<Integer> student_ids = new ArrayList<>();

        TeacherDeliverStudentsRequestModel selectedTeacherRequestList = new TeacherDeliverStudentsRequestModel();
        for (MentorStudentModel studentModel : students) {
            if (studentModel.isMarked()) {
                request_ids.add(studentModel.getRequestId());
                student_ids.add(studentModel.getStudentID());
            }
        }
        selectedTeacherRequestList.setRequest_ids(request_ids);
        selectedTeacherRequestList.setStudent_ids(student_ids);

        return selectedTeacherRequestList;
    }

    public int getNumberOfUniqeRequest() {
        Set<Integer> selectedRequestList = new HashSet<>();
        for (MentorStudentModel studentModel : students) {
            selectedRequestList.add(studentModel.getRequestId());
        }
        ArrayList<Integer> temp = new ArrayList<>(selectedRequestList);
        return temp.size();
    }
}