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
import com.seamlabs.BlueRide.network.response.HelperResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.view.ParentHomeViewCommunicator;
import com.seamlabs.BlueRide.parent_flow.profile.view.ParentProfileViewCommunicator;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileHelpersRecyclerViewAdapter extends RecyclerView.Adapter<ProfileHelpersRecyclerViewAdapter.SchoolsViewHolderLayout> {

    Context context;
    ArrayList<HelperResponseModel> helpers = new ArrayList<>();
    ParentProfileViewCommunicator parentHomeViewCommunicator;

    public ProfileHelpersRecyclerViewAdapter(ParentProfileViewCommunicator view, Context context, ArrayList<HelperResponseModel> helpers) {
        this.context = context;
        this.helpers = helpers;
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
        final HelperResponseModel responseModel = helpers.get(position);

//        if (responseModel.getName().contains(" ")) {
//            holder.student_name.setText(responseModel.getName().substring(0, responseModel.getName().indexOf(" ")));
//        } else {
//        }
        holder.student_name.setText(responseModel.getName());
        try {
            if (responseModel.getImages() != null) {
                if (responseModel.getImages().size() > 0) {
                    Uri uri = Uri.parse(responseModel.getImages().get(0).getPath());
                    holder.profile_picture.setImageURI(uri);
                }
            }
        } catch (NullPointerException npe) {

        }
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
                    parentHomeViewCommunicator.onHelperClickListener(helpers.get(getAdapterPosition()));
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



