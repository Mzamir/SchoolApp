package com.example.mahmoudsamir.schoolappand.parent_flow.pick_up;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.mahmoudsamir.schoolappand.R;
import com.example.mahmoudsamir.schoolappand.parent_flow.home.model.SchoolModel;
import com.google.android.gms.location.LocationListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mahmoudsamir.schoolappand.utils.Constants.PICK_REQUEST_ID;
import static com.example.mahmoudsamir.schoolappand.utils.Constants.SELECTED_SCHOOL_MODEL;

public class ParentPickUpActivity extends AppCompatActivity implements LocationListener {

    int request_id;
    SchoolModel schoolModel;
    @BindView(R.id.pick_up)
    Button pick_up;

    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_pick_up);
        ButterKnife.bind(this);
        request_id = getIntent().getIntExtra(PICK_REQUEST_ID, -1);
        schoolModel = (SchoolModel) getIntent().getSerializableExtra(SELECTED_SCHOOL_MODEL);
        Toast.makeText(this, "ID " + schoolModel.getschoolID(), Toast.LENGTH_SHORT).show();

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

    @Override
    public void onLocationChanged(Location location) {

    }
}
