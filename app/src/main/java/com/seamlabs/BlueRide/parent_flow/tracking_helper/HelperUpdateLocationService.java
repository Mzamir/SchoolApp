package com.seamlabs.BlueRide.parent_flow.tracking_helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.seamlabs.BlueRide.MyApplication;
import com.seamlabs.BlueRide.network.ApiClient;
import com.seamlabs.BlueRide.network.ApiService;
import com.seamlabs.BlueRide.network.BaseResponse;
import com.seamlabs.BlueRide.network.requests.UpdateLocationRequestModel;
import com.seamlabs.BlueRide.parent_flow.waiting_student.presenter.ParentWaitingInteractor;
import com.seamlabs.BlueRide.parent_flow.waiting_student.presenter.ParentWaitingPresenter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.seamlabs.BlueRide.utils.Constants.BASE_URL;

public class HelperUpdateLocationService extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    ParentWaitingPresenter presenter;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            try {
                updateLocationServer(location.getLatitude(), location.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Exception: " + e.getMessage().toString());
            }
            mLastLocation.set(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        presenter = new ParentWaitingPresenter(new ParentWaitingInteractor());
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) MyApplication.getMyApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

//    public void updateServer(double lat, double longitude) {
//        UpdateLocationRequestModel locationRequestModel = new UpdateLocationRequestModel(lat, longitude);
//        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
//                .create(ApiService.class);
//        try {
//            apiService.updateLocationFromService(locationRequestModel, new Callback<Response>() {
//                @Override
//                public void onResponse(Call<Response> call, Response<Response> response) {
//                    Log.i(TAG, "onResponse: called...");
//                    Log.i(TAG, "Update location successfully");
//                }
//
//                @Override
//                public void onFailure(Call<Response> call, Throwable t) {
//                    t.printStackTrace();
//                }
//            });
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public void updateLocationServer(double lat, double longitude) {
        UpdateLocationRequestModel locationRequestModel = new UpdateLocationRequestModel(lat, longitude);
        ApiService apiService = ApiClient.getClient(MyApplication.getMyApplicationContext())
                .create(ApiService.class);
        Call<Response<BaseResponse>> call = apiService.updateLocationFromService(locationRequestModel);
        call.enqueue(new Callback<Response<BaseResponse>>() {
            @Override
            public void onResponse(Call<Response<BaseResponse>> call, Response<Response<BaseResponse>> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Update location successfully");
                } else {
                    Log.i(TAG, "Error Update location");
                }
            }

            @Override
            public void onFailure(Call<Response<BaseResponse>> call, Throwable t) {
                Log.i(TAG, "Exception Update location " + t.getMessage().toString());
            }
        });
    }

}
