package com.seamlabs.BlueRide.maps_directions;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import static com.seamlabs.BlueRide.utils.Constants.MAPS_SERVER_KEY;

/**
 * Created by ocittwo on 11/14/16.
 *
 * @Author Ahmad Rosid
 * @Email ocittwo@gmail.com
 * @Github https://github.com/ar-android
 * @Web http://ahmadrosid.com
 */

public class DrawRouteMaps {

    private static DrawRouteMaps instance;
    private Context context;

    public static DrawRouteMaps getInstance(Context context) {
        instance = new DrawRouteMaps();
        instance.context = context;
        return instance;
    }

    public DrawRouteMaps draw(LatLng origin, LatLng destination, GoogleMap googleMap) {
        // as the new update of google maps require to include the key in url
        String url_route = FetchUrl.getUrl(origin, destination) + MAPS_SERVER_KEY;

        DrawRoute drawRoute = new DrawRoute(googleMap);
        drawRoute.execute(url_route);
        return instance;
    }

    public static Context getContext() {
        return instance.context;
    }
}
