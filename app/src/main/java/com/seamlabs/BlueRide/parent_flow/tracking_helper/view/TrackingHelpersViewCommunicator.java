package com.seamlabs.BlueRide.parent_flow.tracking_helper.view;

import com.seamlabs.BlueRide.mentor_home.model.MentorStudentModel;
import com.seamlabs.BlueRide.parent_flow.profile.model.HelperModel;

import java.util.ArrayList;

public interface TrackingHelpersViewCommunicator {

    void showProgress();

    void hideProgress();

    void showTrackAction(boolean showDeliveryAction);

    void onSuccessGettingHelpers(ArrayList<HelperModel> helpersList);

    void onError(String errorMessage);
}
