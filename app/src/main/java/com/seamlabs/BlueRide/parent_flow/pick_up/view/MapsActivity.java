package com.seamlabs.BlueRide.parent_flow.pick_up.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.maps_directions.DrawMarker;
import com.seamlabs.BlueRide.maps_directions.DrawRouteMaps;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;
import com.seamlabs.BlueRide.network.requests.ParentPickUpRequestModel;
import com.seamlabs.BlueRide.network.requests.UpdateLocationRequestModel;
import com.seamlabs.BlueRide.network.response.ParentArrivedResponseModel;
import com.seamlabs.BlueRide.network.response.ParentPickUpResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.model.SchoolModel;
import com.seamlabs.BlueRide.parent_flow.pick_up.presenter.ParentPickUpInteractor;
import com.seamlabs.BlueRide.parent_flow.pick_up.presenter.ParentPickUpPresenter;
import com.seamlabs.BlueRide.parent_flow.waiting_student.presenter.ParentWaitingInteractor;
import com.seamlabs.BlueRide.parent_flow.waiting_student.view.ParentWaitingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.seamlabs.BlueRide.utils.Constants.HELPER_LATITUDE;
import static com.seamlabs.BlueRide.utils.Constants.HELPER_LONGITUDE;
import static com.seamlabs.BlueRide.utils.Constants.LARGE_DISTANCE;
import static com.seamlabs.BlueRide.utils.Constants.PICK_REQUEST_ID;
import static com.seamlabs.BlueRide.utils.Constants.PICK_UP_REQUEST_MODEL;
import static com.seamlabs.BlueRide.utils.Constants.SELECTED_SCHOOL_MODEL;
import static com.seamlabs.BlueRide.utils.Constants.SMALL_DISTANCE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, ParentPickUpView {
    String TAG = MapsActivity.class.getSimpleName();

    @BindView(R.id.cancel_request)
    Button cancel_request;
    @BindView(R.id.pick_up)
    Button pick_up;
    @BindView(R.id.temp_button)
    Button temp_button;
    @BindView(R.id.school_notified)
    TextView school_notified;

    private int LARGE_RADIUSE = 50;
    private int SMALL_RADIUSE = 20;
    private final int ZOOM_LEVEL = 15;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    LatLng currentLocation = null;
    LatLng destinationLocation = null;

    int request_id = -1;
    SchoolModel schoolModel;
    Marker marker;
    ParentPickUpRequestModel parentPickUpRequestModel;
    ParentPickUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        presenter = new ParentPickUpPresenter(this, new ParentPickUpInteractor());
        schoolModel = (SchoolModel) getIntent().getSerializableExtra(SELECTED_SCHOOL_MODEL);
//        LARGE_DISTANCE = schoolModel.getBig_zone();
//        SMALL_DISTANCE = schoolModel.getSmall_zone();
        parentPickUpRequestModel = (ParentPickUpRequestModel) getIntent().getSerializableExtra(PICK_UP_REQUEST_MODEL);
        destinationLocation = new LatLng(Double.parseDouble(schoolModel.getschoolLat()), Double.parseDouble(schoolModel.getschoolLong()));
        Log.i(TAG, "onLocationChanged " + destinationLocation.latitude + " " + destinationLocation.longitude);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.pick_up_map);
        mapFragment.getMapAsync(this);

        saveIsParentMadeRequest(false);
        cancel_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (request_id != -1)
                    presenter.cancelRequest(request_id);
                else {
                    startActivity(new Intent(MapsActivity.this, MainActivity.class));

                }
            }
        });

        // here means that the parent arrived
        pick_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (request_id != -1) {
                    presenter.parent_arrived(request_id);
                }
            }
        });
        temp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, getResources().getString(R.string.far_away_error), Toast.LENGTH_SHORT).show();
            }
        });
        setUpLocationManager();
    }

    private void setUpLocationManager() {
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);


        Location location = null;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            } catch (Exception e) {

            }
//            location = locationManager.getLastKnownLocation(provider);
        }
        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        } else {
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
//        mMap.getUiSettings().setTiltGesturesEnabled(true);

//        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Map is ready");
        drawCircles();

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged " + location.getLatitude() + " " + location.getLongitude());
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (currentLocation != null) {
            double remainingDistance = distance(
                    Double.parseDouble(schoolModel.getschoolLat()),
                    Double.parseDouble(schoolModel.getschoolLong()),
                    location.getLatitude(),
                    location.getLongitude());

            Log.i(TAG, "onLocationChanged " + "Distance " + remainingDistance);

            if (remainingDistance <= schoolModel.getSmall_zone()) {
                if (!IsParentMadeRequest()) {
                    presenter.parentPickUpRequest(parentPickUpRequestModel);
                    school_notified.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            temp_button.setVisibility(View.GONE);
                            pick_up.setVisibility(View.VISIBLE);
                        }
                    }, 500);
                } else {
                    temp_button.setVisibility(View.GONE);
                    pick_up.setVisibility(View.VISIBLE);
                }
            } else if (remainingDistance <= schoolModel.getBig_zone()) {
                if (!IsParentMadeRequest()) {
                    presenter.parentPickUpRequest(parentPickUpRequestModel);
                    school_notified.setVisibility(View.VISIBLE);
                }
            }
            DrawDirection();
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
            if (locationManager != null) {
                locationManager.requestLocationUpdates(provider, 400, 1, this);
            }
        }

    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
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

    public void DrawDirection() {
        Log.i(TAG, "DrawDirection");
        DrawRouteMaps.getInstance(this)
                .draw(currentLocation, destinationLocation, mMap);

        if (marker != null) {
            marker.setPosition(currentLocation);
            marker.setTitle("Lat " + currentLocation.latitude +
                    "\n Long " + currentLocation.longitude);
        } else {
            marker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_car_top_view))
                    .position(currentLocation)
                    .title("Lat " + currentLocation.latitude + "\n Long " + currentLocation.longitude));
        }

    }

    private void drawCircles() {

        mMap.addCircle(new CircleOptions().center(destinationLocation)
                .fillColor(getResources().getColor(R.color.map_yellow))
                .radius(schoolModel.getBig_zone())
                .strokeColor(getResources().getColor(R.color.big_zone_stoke))
                .strokeWidth(5));

        mMap.addCircle(new CircleOptions().center(destinationLocation)
                .fillColor(getResources().getColor(R.color.map_green))
                .radius(schoolModel.getSmall_zone())
                .strokeColor(getResources().getColor(R.color.small_zone_stoke))
                .strokeWidth(5));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 14));
        Log.i(TAG, "drawCircles");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    // onSuccessParentArrived parent arriving request
    @Override
    public void onSuccessParentArrived(ParentArrivedResponseModel parentArrivedResponseModel) {
        Intent intent = new Intent(MapsActivity.this, ParentWaitingActivity.class);
        intent.putExtra(PICK_REQUEST_ID, request_id);
        intent.putExtra(HELPER_LATITUDE, currentLocation.latitude);
        intent.putExtra(HELPER_LONGITUDE, currentLocation.longitude);
        startActivity(intent);
    }

    @Override
    public void onSuccessPickUpRequest(ParentPickUpResponseModel responseModel) {
        this.request_id = responseModel.getid();
        saveIsParentMadeRequest(true);
        Toast.makeText(this, getResources().getString(R.string.request_sent), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        saveIsParentMadeRequest(false);
    }

    @Override
    public void onSuccessCancelingRequest(String success) {
        if (success.isEmpty()) {
            Toast.makeText(this, getString(R.string.request_canceld), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, success, Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(MapsActivity.this, MainActivity.class));
        finish();
    }

    // To animate view slide out from right to left
    public void slideToLeft(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, -view.getWidth(), 0, 0);
        animate.setDuration(800);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private SharedPreferences getRequestSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("is_parent_made_request", MODE_PRIVATE);
        return sharedPreferences;
    }

    private void saveIsParentMadeRequest(boolean isMadeRequest) {
        SharedPreferences.Editor editor = getRequestSharedPreferences().edit();
        editor.putBoolean("request_made", isMadeRequest);
        editor.commit();
    }

    private boolean IsParentMadeRequest() {

        return getRequestSharedPreferences().getBoolean("request_made", false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
