package com.seamlabs.BlueRide.parent_flow.pick_up.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.seamlabs.BlueRide.MainActivity;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.maps_directions.DrawMarker;
import com.seamlabs.BlueRide.maps_directions.DrawRouteMaps;
import com.seamlabs.BlueRide.network.requests.ParentPickUpRequestModel;
import com.seamlabs.BlueRide.network.response.ParentArrivedResponseModel;
import com.seamlabs.BlueRide.network.response.ParentPickUpResponseModel;
import com.seamlabs.BlueRide.parent_flow.home.model.SchoolModel;
import com.seamlabs.BlueRide.parent_flow.pick_up.presenter.ParentPickUpInteractor;
import com.seamlabs.BlueRide.parent_flow.pick_up.presenter.ParentPickUpPresenter;
import com.seamlabs.BlueRide.parent_flow.waiting_student.view.ParentWaitingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seamlabs.BlueRide.utils.Constants.HELPER_LATITUDE;
import static com.seamlabs.BlueRide.utils.Constants.HELPER_LONGITUDE;
import static com.seamlabs.BlueRide.utils.Constants.LARGE_DISTANCE;
import static com.seamlabs.BlueRide.utils.Constants.PICK_REQUEST_ID;
import static com.seamlabs.BlueRide.utils.Constants.PICK_UP_REQUEST_MODEL;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_CLUSTER;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_KEY;
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

    private final int LARGE_RADIUSE = 50;
    private final int SMALL_RADIUSE = 20;
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
        parentPickUpRequestModel = (ParentPickUpRequestModel) getIntent().getSerializableExtra(PICK_UP_REQUEST_MODEL);
        destinationLocation = new LatLng(Double.parseDouble(schoolModel.getschoolLat()), Double.parseDouble(schoolModel.getschoolLong()));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        cancel_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (request_id != -1)
                    presenter.cancelRequest(request_id);
                else {
                    startActivity(new Intent(MapsActivity.this, MainActivity.class));
//                    Toast.makeText(MapsActivity.this, "Request hasn't created yet", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MapsActivity.this, "You are far a way to pick up", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpLocationManager() {
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        Location location = null;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(provider);
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
        mMap = googleMap;
//        mMap.getUiSettings().setTiltGesturesEnabled(true);

        drawCircles();
        setUpLocationManager();
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged " + location.getLatitude() + " " + location.getLongitude());
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (currentLocation != null) {
//            if (marker != null) {
//                marker.setPosition(currentLocation);
//                marker.setTitle("Lat " + currentLocation.latitude +
//                        "\n Long " + currentLocation.longitude);
//            } else {
//                marker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Lat " + currentLocation.latitude +
//                        "\n Long " + currentLocation.longitude));
//            }


//            CircleOptions circleOptions = new CircleOptions();
//            circleOptions.center(destinationLocation);
//            mMap.addCircle(circleOptions);
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
            double remainingDistance = distance(
                    Double.parseDouble(schoolModel.getschoolLat()),
                    Double.parseDouble(schoolModel.getschoolLong()),
                    location.getLatitude(),
                    location.getLongitude());

            Log.i(TAG, "onLocationChanged " + "Distance " + remainingDistance);
            if (remainingDistance <= SMALL_DISTANCE) {
                temp_button.setVisibility(View.GONE);
                pick_up.setVisibility(View.VISIBLE);
            } else if (remainingDistance <= LARGE_DISTANCE) {
                presenter.parentPickUpRequest(parentPickUpRequestModel);
                school_notified.setVisibility(View.VISIBLE);
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
        DrawRouteMaps.getInstance(this)
                .draw(currentLocation, destinationLocation, mMap);
        DrawMarker.getInstance(this).draw(mMap, currentLocation, R.drawable.map_car_top_view, "Origin Location");
        DrawMarker.getInstance(this).draw(mMap, destinationLocation, R.drawable.map_destination, "Destination Location");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(currentLocation)
                .include(destinationLocation).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, displaySize.y / 2, 30));

    }

    private void drawCircles() {
        mMap.addCircle(new CircleOptions().center(destinationLocation)
                .fillColor(getResources().getColor(R.color.map_yellow))
                .radius(LARGE_RADIUSE)
                .strokeColor(Color.BLACK)
                .strokeWidth(5));

        mMap.addCircle(new CircleOptions().center(destinationLocation)
                .fillColor(getResources().getColor(R.color.map_green))
                .radius(SMALL_RADIUSE)
                .strokeColor(Color.BLACK)
                .strokeWidth(5));

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    // onSuccess parent arriving request
    @Override
    public void onSuccess(ParentArrivedResponseModel parentArrivedResponseModel) {
        Intent intent = new Intent(MapsActivity.this, ParentWaitingActivity.class);
        intent.putExtra(PICK_REQUEST_ID, request_id);
        intent.putExtra(HELPER_LATITUDE, currentLocation.latitude);
        intent.putExtra(HELPER_LONGITUDE, currentLocation.longitude);
        startActivity(intent);
    }

    @Override
    public void onSuccessPickUpRequest(ParentPickUpResponseModel responseModel) {
        request_id = responseModel.getid();
        Toast.makeText(this, "Pick request successfully sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {

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

}
