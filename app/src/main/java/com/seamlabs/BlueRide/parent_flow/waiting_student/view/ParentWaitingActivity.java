package com.seamlabs.BlueRide.parent_flow.waiting_student.view;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.MainActivity;
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

    @BindView(R.id.report_btn)
    Button report_btn;

    @BindView(R.id.waiting_timer)
    TextView waiting_timer;
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
        received_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (request_id >= 0) {
                    presenter.delivered(request_id);
                }
            }
        });
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (request_id >= 0) {
                    presenter.report(request_id);
                }
            }
        });
    }

    @Override
    public void showProgress() {
        enableDisableButtons(false);
        Utility.showProgressDialog(ParentWaitingActivity.this);
    }

    @Override
    public void hideProgress() {
        enableDisableButtons(true);
        Utility.hideProgressDialog();
    }

    @Override
    public void onSuccessReport(String successMessage) {
        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
        startCountDownTimer();
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

    private void enableDisableButtons(boolean enable) {
        report_btn.setEnabled(enable);
        received_btn.setEnabled(enable);
    }

    private void startCountDownTimer() {
        report_btn.setEnabled(false);
        new CountDownTimer(1000 * 60 * 2, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                String secondText = (seconds >= 10) ? String.valueOf(seconds) : "0" + String.valueOf(seconds);
                String minutesText = (minutes >= 10) ? String.valueOf(minutes) : "0" + String.valueOf(minutes);
                waiting_timer.setText(minutesText + ":" + secondText);
            }

            public void onFinish() {
                waiting_timer.setText("00:00");
                report_btn.setEnabled(true);
                Toast.makeText(ParentWaitingActivity.this, "Report now if your children is not here.", Toast.LENGTH_SHORT).show();
                waiting_animation.setAnimation(null);
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (report_btn.isEnabled()) {
            startActivity(new Intent(ParentWaitingActivity.this, MainActivity.class));
            finish();
        }
    }

    private void updateHelperLocation(double lan, double longi) {
        presenter.updateHelperLocation(lan, longi);
    }

}
