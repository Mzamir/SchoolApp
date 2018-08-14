package com.seamlabs.BlueRide.parent_flow.tracking_helper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.parent_flow.profile.model.HelperModel;
import com.seamlabs.BlueRide.parent_flow.tracking_helper.view.TrackingHelpersViewCommunicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpersRecyclerViewAdapter extends RecyclerView.Adapter<HelpersRecyclerViewAdapter.StudentsViewHolderLayout> {

    Context context;
    ArrayList<HelperModel> helpers = new ArrayList<>();
    TrackingHelpersViewCommunicator communicator;

    int selectedHelperId = -1;

    public HelpersRecyclerViewAdapter(TrackingHelpersViewCommunicator communicator, Context context, ArrayList<HelperModel> schools) {
        this.context = context;
        this.helpers = schools;
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
        final HelperModel helperModel = helpers.get(position);

        if (helperModel.isMarked())
            holder.marked_icon.setVisibility(View.VISIBLE);
        else
            holder.marked_icon.setVisibility(View.INVISIBLE);
//        if (studentModel.getStudentPicture() != null) {
//            Uri uri = Uri.parse(studentModel.getStudentPicture());
//            holder.student_picture.setImageURI(uri);
//        }
        holder.helper_layout.setVisibility(View.VISIBLE);
        holder.mentor_students_layout.setVisibility(View.GONE);
        holder.helper_name.setText(helperModel.getName());
    }

    @Override
    public int getItemCount() {
        return helpers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    HelperModel helperModel = helpers.get(position);
                    markSelectedHelpers(position);
                    showTrackAction();
                    setSelectedHelperId(helperModel.getId());
                    notifyItemChanged(position);
                }
            });
        }
    }

    class StudentsViewHolderLayout extends ViewHolder {

        @BindView(R.id.helper_layout)
        LinearLayout helper_layout;

        @BindView(R.id.mentor_students_layout)
        LinearLayout mentor_students_layout;
        @BindView(R.id.helper_name)
        TextView helper_name;

        @BindView(R.id.helper_marked_icon)
        ImageView marked_icon;

        public StudentsViewHolderLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    void showTrackAction() {
        for (HelperModel model : helpers) {
            if (model.isMarked()) {
                communicator.showTrackAction(true);
                return;
            }
        }
        communicator.showTrackAction(false);
    }

    // this function is used to mark selected school and unmark the others
    private void markSelectedHelpers(int selectedSchoolPosition) {
        for (int i = 0; i < helpers.size(); i++) {
            if (i == selectedSchoolPosition) {
                helpers.get(i).setMarked(!helpers.get(i).isMarked());
            } else {
                helpers.get(i).setMarked(false);
            }
            notifyItemChanged(i);
        }
    }

    public int getSelectedHelperId() {
        return selectedHelperId;
    }

    public void setSelectedHelperId(int selectedHelperId) {
        this.selectedHelperId = selectedHelperId;
    }
}