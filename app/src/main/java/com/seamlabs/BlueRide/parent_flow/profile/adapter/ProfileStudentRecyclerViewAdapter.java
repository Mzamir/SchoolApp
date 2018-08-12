package com.seamlabs.BlueRide.parent_flow.profile.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.response.StudentResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.view.ParentHomeViewCommunicator;
import com.seamlabs.BlueRide.parent_flow.profile.view.ParentProfileViewCommunicator;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileStudentRecyclerViewAdapter extends RecyclerView.Adapter<ProfileStudentRecyclerViewAdapter.SchoolsViewHolderLayout> {

    Context context;
    ArrayList<StudentResponseModel> students = new ArrayList<>();
    ParentProfileViewCommunicator parentHomeViewCommunicator;

    public ProfileStudentRecyclerViewAdapter(ParentProfileViewCommunicator view, Context context, ArrayList<StudentResponseModel> students) {
        this.context = context;
        this.students = students;
        this.parentHomeViewCommunicator = view;
    }

    @NonNull
    @Override
    public SchoolsViewHolderLayout onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_recyclerview_item, parent, false);
        SchoolsViewHolderLayout schoolsViewHolderLayout = new SchoolsViewHolderLayout(v);
        return schoolsViewHolderLayout;
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolsViewHolderLayout holder, int position) {
        final StudentResponseModel studentModel = students.get(position);

//        if (studentModel.getName().contains(" ")) {
//            holder.student_name.setText(studentModel.getName().substring(0, studentModel.getName().indexOf(" ")));
//        } else {
//            holder.student_name.setText(studentModel.getName());
//        }
        holder.student_name.setText(studentModel.getName());
        try {
            if (studentModel.getImages() != null) {
                if (studentModel.getImages().size() > 0) {
                    Uri uri = Uri.parse(studentModel.getImages().get(0).getPath());
                    holder.profile_picture.setImageURI(uri);
                }
            }
        } catch (NullPointerException npe) {

        }
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

                }
            });
        }
    }

    class SchoolsViewHolderLayout extends ViewHolder {

        @BindView(R.id.profile_picture)
        SimpleDraweeView profile_picture;
        @BindView(R.id.student_name)
        TextView student_name;

        public SchoolsViewHolderLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}



