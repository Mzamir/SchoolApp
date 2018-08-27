package com.seamlabs.BlueRide.parent_flow.helper_profile;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.parent_flow.home.model.StudentModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelperProfileStudentRecyclerViewAdapter extends RecyclerView.Adapter<HelperProfileStudentRecyclerViewAdapter.SchoolsViewHolderLayout> {

    Context context;
    ArrayList<StudentModel> students = new ArrayList<>();
    HelperProfileViewCommunicator parentHomeViewCommunicator;

    public HelperProfileStudentRecyclerViewAdapter(HelperProfileViewCommunicator view, Context context, ArrayList<StudentModel> students) {
        this.context = context;
        this.students = students;
        this.parentHomeViewCommunicator = view;
    }

    @NonNull
    @Override
    public SchoolsViewHolderLayout onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_profile_recyclerview_item, parent, false);
        SchoolsViewHolderLayout schoolsViewHolderLayout = new SchoolsViewHolderLayout(v);
        return schoolsViewHolderLayout;
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolsViewHolderLayout holder, int position) {
        final StudentModel studentModel = students.get(position);

        holder.student_name.setText(studentModel.getStudentName());
        if (studentModel.getStudentPicture() != null) {
            Uri uri = Uri.parse(studentModel.getStudentPicture());
            holder.profile_picture.setImageURI(uri);
        }
        if (studentModel.isMarked())
            holder.marked_icon.setVisibility(View.VISIBLE);
        else
            holder.marked_icon.setVisibility(View.INVISIBLE);
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
                    StudentModel studentModel = students.get(position);
                    markSelectedStudent(position);
                    showPermissionActions(studentModel);
                    notifyItemChanged(position);
                }
            });
        }
    }

    class SchoolsViewHolderLayout extends ViewHolder {

        @BindView(R.id.profile_picture)
        SimpleDraweeView profile_picture;
        @BindView(R.id.student_name)
        TextView student_name;
        @BindView(R.id.marked_icon)
        ImageView marked_icon;

        public SchoolsViewHolderLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public ArrayList<Integer> getSelectedStudent() {
        ArrayList<Integer> selectedStudentList = new ArrayList<>();
        for (StudentModel studentModel : students) {
            if (studentModel.isMarked()) {
                selectedStudentList.add(studentModel.getStudentID());
            }
        }
        return selectedStudentList;
    }

    void showPermissionActions(StudentModel studentModel) {
        for (StudentModel model : students) {
            if (model.isMarked()) {
                parentHomeViewCommunicator.showPermissionActions(true , studentModel);
                return;
            }
        }
        parentHomeViewCommunicator.showPermissionActions(false , studentModel);
    }

    private void markSelectedStudent(int selectedSchoolPosition) {
        for (int i = 0; i < students.size(); i++) {
            if (i == selectedSchoolPosition) {
                students.get(i).setMarked(!students.get(i).isMarked());
            } else {
                students.get(i).setMarked(false);
            }
            notifyItemChanged(i);
        }
    }
}
