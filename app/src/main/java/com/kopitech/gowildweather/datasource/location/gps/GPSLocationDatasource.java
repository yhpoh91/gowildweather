package com.kopitech.gowildweather.datasource.location.gps;

import com.kopitech.gowildweather.dataobject.LocationDto;
import com.kopitech.gowildweather.datasource.http.HttpDatasource;
import com.kopitech.gowildweather.datasource.location.LocationDatasource;
import com.kopitech.gowildweather.datasource.location.OnLocationCallback;

import java.util.concurrent.ExecutorService;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public class GPSLocationDatasource implements LocationDatasource {
    private static final String TAG = "GLD";

    private ExecutorService executorService;

    public GPSLocationDatasource(ExecutorService executorService){
        this.executorService = executorService;
    }

    @Override
    public LocationDto getLocation() {
        return null;
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
