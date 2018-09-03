package com.seamlabs.BlueRide.parent_flow.waiting_student.view;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.seamlabs.BlueRide.parent_flow.tracking_helper.HelperUpdateLocationService;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.network.response.CheckRequestStateResponseModel;
import com.seamlabs.BlueRide.parent_flow.waiting_student.presenter.ParentWaitingInteractor;
import com.seamlabs.BlueRide.parent_flow.waiting_student.presenter.ParentWaitingPresenter;
import com.seamlabs.BlueRide.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.HELPER_LATITUDE;
import static com.seamlabs.BlueRide.utils.Constants.HELPER_LONGITUDE;
import static com.seamlabs.BlueRide.utils.Constants.HELPER_USER_TYPE;
import static com.seamlabs.BlueRide.utils.Constants.PICK_REQUEST_ID;
import static com.seamlabs.BlueRide.utils.UserSettingsPreference.getUserType;

public class ParentWaitingActivity extends AppCompatActivity implements ParentWaitingView {

    @BindView(R.id.received_btn)
    Button received_btn;

    @BindView(R.id.waiting_animation)
    ImageView waiting_animation;

    ParentWaitingPresenter presenter;
    int request_id = -1;

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_acrivity);
        ButterKnife.bind(this);
        Animation startRotateAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        waiting_animation.startAnimation(startRotateAnimation);

        latitude = getIntent().getDoubleExtra(HELPER_LATITUDE, 0.0);
        longitude = getIntent().getDoubleExtra(HELPER_LONGITUDE, 0.0);

        presenter = new ParentWaitingPresenter(this, new ParentWaitingInteractor());
        startCountDownTimer();
        request_id = getIntent().getIntExtra(PICK_REQUEST_ID, -1);
        Log.i("Waiting", "request_id " + String.valueOf(request_id));
        received_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (request_id != -1) {
                    presenter.checkIfCanReceive(request_id);
                }
            }
        });
    }

    @Override
    public void showProgress() {
        Utility.showProgressDialog(ParentWaitingActivity.this);
    }

    @Override
    public void hideProgress() {
        Utility.hideProgressDialog();
    }

    @Override
    public void onSuccessReport(String successMessage) {
        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessDelivery(String successMessage) {
        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
        if (getUserType(this).equals(HELPER_USER_TYPE)) {
            updateHelperLocation(latitude, longitude);
        }
        startActivity(new Intent(ParentWaitingActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessCheckingRequestState(CheckRequestStateResponseModel responseModel) {
        if (responseModel.getSecurity_id() != null) {
            if (responseModel.getStatus().equals("delivered_to_security") || responseModel.getStatus().equals("returned"))
                if (request_id != -1)
                    presenter.delivered(request_id);
        } else {
            Toast.makeText(ParentWaitingActivity.this, "You are far a way to pick up", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorCheckingRequestState(String errorMessage) {

    }


    CountDownTimer countDownTimer;

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(1000 * 60 * 5, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                waiting_animation.setAnimation(null);
                if (request_id != -1) {
                    presenter.report(request_id);
                }
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
//        if (report_btn.isEnabled()) {
//            startActivity(new Intent(ParentWaitingActivity.this, MainActivity.class));
//            finish();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void updateHelperLocation(double lan, double longi) {
        presenter.updateHelperLocation(lan, longi);
        startService(new Intent(this, HelperUpdateLocationService.class));
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
