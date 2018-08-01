package com.example.mahmoudsamir.schoolappand.parent_flow.pick_up.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mahmoudsamir.schoolappand.utils.LocalNoificationCreator;
import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.network.response.ParentArrivedResponseModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.model.SchoolModel;
import com.example.mahmoudsamir.schoolappand.parent_flow.pick_up.presenter.ParentPickUpInteractor;
import com.example.mahmoudsamir.schoolappand.parent_flow.pick_up.presenter.ParentPickUpPresenter;
import com.example.mahmoudsamir.schoolappand.parent_flow.waiting_student.view.ParentWaitingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.ALLOWED_DISTANCE;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.ERROR;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.PICK_REQUEST_ID;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.SELECTED_SCHOOL_MODEL;

public class ParentPickUpActivity extends AppCompatActivity implements LocationListener, ParentPickUpView {

    String TAG = ParentPickUpActivity.class.getSimpleName();
    int request_id;
    SchoolModel schoolModel;
    @BindView(R.id.pick_up)
    Button pick_up;

    private LocationManager locationManager;
    private String provider;

    ParentPickUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.activity_parent_pick_up);
        ButterKnife.bind(this);
        request_id = getIntent().getIntExtra(PICK_REQUEST_ID, -1);
        schoolModel = (SchoolModel) getIntent().getSerializableExtra(SELECTED_SCHOOL_MODEL);
//        Toast.makeText(this, "ID " + schoolModel.getschoolID(), Toast.LENGTH_SHORT).show();

        presenter = new ParentPickUpPresenter(this, new ParentPickUpInteractor());
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        Location location = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(provider);
        }
        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        } else {
        }
        pick_up.setEnabled(true);
        if (pick_up.isEnabled()) {
            pick_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.parent_arrived(request_id);
                }
            });
        }
    }

    private void promptEnabledGPS() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 400, 1, this);
        }

    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged " + location.getLatitude() + " " + location.getLongitude());
        double remainingDistance = distance(
                Double.parseDouble(schoolModel.getschoolLat()),
                Double.parseDouble(schoolModel.getschoolLong()),
                location.getLatitude(),
                location.getLongitude());

        Log.i(TAG, "onLocationChanged " + "Distance " + remainingDistance);
        if (remainingDistance <= ALLOWED_DISTANCE) {
            pick_up.setEnabled(true);
            LocalNoificationCreator.notifyParrentArrived();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private boolean checkGPSEnabled() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);

        return dist;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onSuccess(ParentArrivedResponseModel parentArrivedResponseModel) {
        Intent intent = new Intent(ParentPickUpActivity.this, ParentWaitingActivity.class);
        intent.putExtra(PICK_REQUEST_ID, request_id);
        startActivity(intent);
    }

    @Override
    public void onError() {
        Toast.makeText(this, ERROR, Toast.LENGTH_SHORT).show();
    }
}
