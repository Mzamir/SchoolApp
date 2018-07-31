package com.example.mahmoudsamir.schoolappand.mentor_home;

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
import com.example.mahmoudsamir.schoolappand.parent_flow.home.model.StudentModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MentorStudentsRecyclerViewAdapter extends RecyclerView.Adapter<MentorStudentsRecyclerViewAdapter.StudentsViewHolderLayout> {

    Context context;
    ArrayList<StudentModel> students = new ArrayList<>();

    public MentorStudentsRecyclerViewAdapter(Context context, ArrayList<StudentModel> schools) {
        this.context = context;
        this.students = schools;
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
        final StudentModel studentModel = students.get(position);

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
                    studentModel.setMarked(!studentModel.isMarked());
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

}