package com.seamlabs.BlueRide.notification.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.response.NotificationResponseModel;
import com.seamlabs.BlueRide.notification.view.NotificationViewCommunicator;
import com.seamlabs.BlueRide.parent_flow.profile.model.HelperModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsRecyclerViewAdapter extends RecyclerView.Adapter<NotificationsRecyclerViewAdapter.StudentsViewHolderLayout> {

    Context context;
    ArrayList<NotificationResponseModel> notifications = new ArrayList<>();
    NotificationViewCommunicator communicator;

    int selectedHelperId = -1;

    public NotificationsRecyclerViewAdapter(NotificationViewCommunicator communicator, Context context, ArrayList<NotificationResponseModel> schools) {
        this.context = context;
        this.notifications = schools;
        this.communicator = communicator;
    }

    @NonNull
    @Override
    public StudentsViewHolderLayout onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recyclerview_item, parent, false);
        StudentsViewHolderLayout studentsViewHolderLayout = new StudentsViewHolderLayout(v);
        return studentsViewHolderLayout;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewHolderLayout holder, int position) {
        final NotificationResponseModel notification = notifications.get(position);

        holder.school_title.setText(notification.getSchool());
        holder.notification_body.setText(notification.getMessage());
        holder.notification_time.setText(notification.getTime());
        if (notification.getIs_read() == 1) {
            holder.school_title.setTextColor(Color.GRAY);
            holder.school_title.setTypeface(Typeface.DEFAULT);

            holder.notification_body.setTextColor(Color.GRAY);
            holder.notification_body.setTypeface(Typeface.DEFAULT);

            holder.notification_time.setTextColor(Color.GRAY);
            holder.notification_time.setTypeface(Typeface.DEFAULT);
        } else {
            holder.school_title.setTextColor(Color.BLACK);
            holder.school_title.setTypeface(Typeface.DEFAULT_BOLD);

            holder.notification_body.setTextColor(Color.BLACK);
            holder.notification_body.setTypeface(Typeface.DEFAULT_BOLD);

            holder.notification_time.setTextColor(Color.BLACK);
            holder.notification_time.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    NotificationResponseModel notification = notifications.get(position);
                    notifyItemChanged(position);
                }
            });
        }
    }

    class StudentsViewHolderLayout extends ViewHolder {

        @BindView(R.id.school_title)
        TextView school_title;

        @BindView(R.id.notification_body)
        TextView notification_body;

        @BindView(R.id.notification_time)
        TextView notification_time;

        public StudentsViewHolderLayout(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}