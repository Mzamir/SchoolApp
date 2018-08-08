package com.example.mahmoudsamir.schoolappand.mentor_home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.mentor_home.model.MentorStudentModel;
import com.example.mahmoudsamir.schoolappand.mentor_home.view.MentorHomeViewCommunicator;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.model.StudentModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.PARENT_ARRIVED_STATE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.PENDING_STATE;

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
        holder.student_class.setText(String.valueOf(studentModel.getClassID()));
        holder.student_name.setText(studentModel.getStudentName());
        if (studentModel.getRequestState().equals(PENDING_STATE)) {
            holder.student_state.setTextColor(context.getResources().getColor(R.color.gray));
        } else if (studentModel.getRequestState().equals(PARENT_ARRIVED_STATE)) {
            holder.student_state.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.student_state.setTextColor(context.getResources().getColor(R.color.red));
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
                    studentModel.setMarked(!studentModel.isMarked());
                    showDeliverAction();
                    notifyItemChanged(position);
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
}