package com.seamlabs.BlueRide.notification.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seamlabs.BlueRide.MyFragment;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.response.NotificationResponseModel;
import com.seamlabs.BlueRide.notification.adapter.NotificationsRecyclerViewAdapter;
import com.seamlabs.BlueRide.notification.presenter.NotificationInteractor;
import com.seamlabs.BlueRide.notification.presenter.NotificationPresenter;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends MyFragment implements NotificationViewCommunicator {

    @BindView(R.id.notification_recyclerView)
    RecyclerView notification_recyclerView;

    @BindView(R.id.no_notifications_tv)
    TextView no_notifications_tv;
    Activity activity;
    NotificationPresenter presenter;
    NotificationsRecyclerViewAdapter adapter;

    @BindView(R.id.user_profile_name)
    TextView user_profile_name;
    @BindView(R.id.navigation_icon)
    LinearLayout navigation_icon;
    @BindView(R.id.notification_toolbar)
    Toolbar toolbar;

    boolean loadData = true;

    private void bindToolBarData() {
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNavigationIconClickListener() != null) {
                    getNavigationIconClickListener().onNavigationIconClick();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        bindToolBarData();
        activity = getActivity();
        presenter = new NotificationPresenter(this, new NotificationInteractor());
        presenter.getNotifications();
        return view;
    }

    @Override
    public void showProgress() {
        Utility.showProgressDialog(activity);
    }

    @Override
    public void hideProgress() {
        Utility.hideProgressDialog();
    }

    @Override
    public void onErrorGettingNotifications(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessGettingNotifications(ArrayList<NotificationResponseModel> notificationsList) {
        if (notificationsList.size() >= 0) {
            showHideRecyclerView(true);
            adapter = new NotificationsRecyclerViewAdapter(this, activity, notificationsList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
            notification_recyclerView.setLayoutManager(layoutManager);
            notification_recyclerView.setItemAnimator(new DefaultItemAnimator());
            notification_recyclerView.setAdapter(adapter);
        } else {
            showHideRecyclerView(false);
        }
    }

    private void showHideRecyclerView(boolean show) {
        if (show) {
            notification_recyclerView.setVisibility(View.VISIBLE);
            no_notifications_tv.setVisibility(View.GONE);
        } else {
            notification_recyclerView.setVisibility(View.GONE);
            no_notifications_tv.setVisibility(View.VISIBLE);
        }
    }

}
