package com.seamlabs.BlueRide.parent_flow.tracking_helper.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.seamlabs.BlueRide.MyFragment;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.mentor_home.view.MentorHomeActivity;
import com.seamlabs.BlueRide.parent_flow.pick_up.view.MapsActivity;
import com.seamlabs.BlueRide.parent_flow.profile.model.HelperModel;
import com.seamlabs.BlueRide.parent_flow.tracking_helper.adapter.HelpersRecyclerViewAdapter;
import com.seamlabs.BlueRide.parent_flow.tracking_helper.presenter.TrackingHelperInteractor;
import com.seamlabs.BlueRide.parent_flow.tracking_helper.presenter.TrackingHelperPresenter;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;
import com.seamlabs.BlueRide.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.TRACKED_HELPER_ID;

public class TrackingHelpersFragment extends MyFragment implements TrackingHelpersViewCommunicator {

    String TAG = MentorHomeActivity.class.getSimpleName();
    ArrayList<HelperModel> helperList = new ArrayList<>();

    @BindView(R.id.students_recyclerView)
    RecyclerView helpers_recyclerView;

    @BindView(R.id.track_helper)
    Button track_helper;

    @BindView(R.id.parent_helpers_layout)
    LinearLayout parent_helpers_layout;

    @BindView(R.id.no_students_tv)
    TextView no_students_tv;

    @BindView(R.id.profile_toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_icon)
    LinearLayout navigation_icon;
    HelpersRecyclerViewAdapter helpersRecyclerViewAdapter;
    private Activity activity;
    TrackingHelperPresenter presenter;

    @BindView(R.id.user_profile_picture)
    SimpleDraweeView user_profile_picture;
    @BindView(R.id.user_profile_name)
    TextView user_profile_name;
    @BindView(R.id.edit_profile)
    ImageView edit_profile;

    private void bindToolBarData() {
        if (UserSettingsPreference.getSavedUserProfile(getActivity()).getImages().size() >0) {
            Uri uri = Uri.parse(UserSettingsPreference.getSavedUserProfile(getActivity()).getImages().get(0).getPath());
            user_profile_picture.setImageURI(uri);
        }
        user_profile_name.setText(UserSettingsPreference.getSavedUserProfile(getActivity()).getName());

        edit_profile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                showEditProfileFragment();
            }
        });
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getNavigationIconClickListener() != null)
                    getNavigationIconClickListener().onNavigationIconClick();
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_parent_helpers, container, false);
        activity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        presenter = new TrackingHelperPresenter(this, new TrackingHelperInteractor());
        initializeView();
        track_helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TrackingHelperMap.class);
                intent.putExtra(TRACKED_HELPER_ID, helpersRecyclerViewAdapter.getSelectedHelperId());
                startActivity(intent);
            }
        });
        return view;
    }

    private void initializeView() {
        helpersRecyclerViewAdapter = new HelpersRecyclerViewAdapter(this, activity, helperList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        helpers_recyclerView.setLayoutManager(layoutManager);
        helpers_recyclerView.setItemAnimator(new DefaultItemAnimator());
        helpers_recyclerView.setAdapter(helpersRecyclerViewAdapter);

        presenter.getHelpers();
//        addDummyData();
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
    public void showTrackAction(boolean showDeliveryAction) {
        if (showDeliveryAction)
            track_helper.setVisibility(View.VISIBLE);
        else
            track_helper.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessGettingHelpers(ArrayList<HelperModel> helpersList) {
        if (helpersList.size() == 0) {
            parent_helpers_layout.setVisibility(View.GONE);
            no_students_tv.setVisibility(View.VISIBLE);
        } else {
            this.helperList = helpersList;
            helpersRecyclerViewAdapter = new HelpersRecyclerViewAdapter(this, activity, helperList);
            helpers_recyclerView.setAdapter(helpersRecyclerViewAdapter);
            helpersRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        bindToolBarData();
    }
}
