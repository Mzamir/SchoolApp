package com.seamlabs.BlueRide.parent_flow.profile.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;

import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileFragment extends Fragment implements EditProfileViewCommunicator {
    @BindView(R.id.email_address)
    EditText email_address;
    @BindView(R.id.edit_email_address)
    ImageView edit_email_address;

    @BindView(R.id.phone_number)
    EditText phone_number;
    @BindView(R.id.edit_phone_number)
    ImageView edit_phone_number;

    @BindView(R.id.password_edx)
    EditText password_edx;
    @BindView(R.id.edit_password)
    ImageView edit_password;

    @BindView(R.id.address)
    EditText address_edx;
    @BindView(R.id.edit_address)
    ImageView edit_address;

    @BindView(R.id.edit_profile_picture)
    ImageView edit_profile_picture;

    UserResponseModel userProfileModel;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_profile, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        userProfileModel = UserSettingsPreference.getSavedUserProfile(activity);
        if (userProfileModel != null)
            bindBasicDateToViews();
        handleViewsListener();
        return view;
    }

    private void bindBasicDateToViews() {
        email_address.setText(userProfileModel.getEmail());
        phone_number.setText(userProfileModel.getPhone());
        if (userProfileModel.getAddress() != null) {
            address_edx.setText(userProfileModel.getAddress());
        }
    }

    private void handleViewsListener() {
        edit_email_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_address.setEnabled(true);
            }
        });
        edit_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_number.setEnabled(true);
            }
        });
        edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_edx.setEnabled(true);
            }
        });
        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_edx.setEnabled(true);
            }
        });


    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onSuccessEditProfile() {

    }

    @Override
    public void onErrorEditProfile() {

    }
}
