package com.kopitech.gowildweather.datasource.location.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.kopitech.gowildweather.dataobject.LocationDto;
import com.kopitech.gowildweather.datasource.location.LocationDatasource;
import com.kopitech.gowildweather.datasource.location.OnLocationCallback;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public class GPSLocationDatasource implements LocationDatasource {
    private static final String TAG = "GLD";

    private ExecutorService executorService;
    private Context context;

    public GPSLocationDatasource(ExecutorService executorService, Context context) {
        this.executorService = executorService;
        this.context = context;
    }

    @Override
    public LocationDto getLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String locationProvider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // No Permission
            throw new RuntimeException("No Access Fine Location Permission");
        }

        // Get Location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if(location == null){
            Log.w(TAG, "Null Location");
            return null;
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LocationDto locationDto = new LocationDto(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude));

        Log.d(TAG, locationDto.toString());
        return locationDto;
    }

    @Override
    public void getLocationInBackground(final OnLocationCallback onLocationCallback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LocationDto locationDto = getLocation();

                // Callback
                if(onLocationCallback != null){
                    onLocationCallback.onReceiveLocationInformation(locationDto);
                }
            }
        };

        // Submit to executor service
        this.executorService.submit(runnable);
    }
}
