package com.seamlabs.BlueRide.parent_flow.tracking_helper.view;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.seamlabs.BlueRide.R;
import com.seamlabs.BlueRide.network.requests.UpdateLocationRequestModel;
import com.seamlabs.BlueRide.network.response.UpdateLocationResponseModel;
import com.seamlabs.BlueRide.network.response.UserResponseModel;
import com.seamlabs.BlueRide.utils.UserSettingsPreference;

import org.json.JSONException;
import org.json.JSONObject;

import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_CLUSTER;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_API_KEY;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_CHANEL_NAME;
import static com.seamlabs.BlueRide.utils.Constants.PUSHER_EVENT_NAME;
import static com.seamlabs.BlueRide.utils.Constants.TRACKED_HELPER_ID;

public class TrackingHelperMap extends FragmentActivity implements OnMapReadyCallback {

    String TAG = TrackingHelperMap.class.getSimpleName();
    private GoogleMap googleMap;
    private MarkerOptions markerOptions;
    private Marker marker;
    private CameraPosition cameraPosition;
    double defaultLongitude = -122.088426;
    double defaultLatitude = 37.388064;
    Pusher pusher;

    int trackedHelperId = -1;
    int parentID = -1;
    UserResponseModel userResponseModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_helper_map);

        trackedHelperId = getIntent().getIntExtra(TRACKED_HELPER_ID, -1);
        userResponseModel = UserSettingsPreference.getSavedUserProfile(this);
        if (userResponseModel != null)
            parentID = userResponseModel.getId();
        markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(defaultLatitude, defaultLongitude);
        markerOptions.position(latLng);
        cameraPosition = CameraPosition.builder()
                .target(latLng).zoom(17f).build();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initializePushNotification();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        marker = googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private void initializePushNotification() {
        PusherOptions options = new PusherOptions();
        options.setCluster(PUSHER_API_CLUSTER);
        pusher = new Pusher(PUSHER_API_KEY, options);
        Channel channel = pusher.subscribe(PUSHER_CHANEL_NAME + trackedHelperId + "_" + parentID);
        channel.bind(PUSHER_EVENT_NAME, new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                try {
                    Gson gson = new Gson();
                    UpdateLocationResponseModel locationObject = gson.fromJson(data, UpdateLocationResponseModel.class);
                    UpdateLocationRequestModel location = locationObject.getLocationRequestModel();
                    final Double lat = location.getLat();
                    final Double lon = location.getLongitude();
                    Log.e(TAG, "Lat " + lat + " Long " + lon);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LatLng newLatLng = new LatLng(lat, lon);
                            marker.setPosition(newLatLng);
                            cameraPosition = CameraPosition.builder().target(newLatLng)
                                    .zoom(17f).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        pusher.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pusher.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pusher.disconnect();
    }
}
