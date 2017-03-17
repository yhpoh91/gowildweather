package com.kopitech.gowildweather.datasource.geocode.google;

import android.util.Log;

import com.google.gson.Gson;
import com.kopitech.gowildweather.dataobject.LocationDto;
import com.kopitech.gowildweather.datasource.geocode.GeocodeDatasource;
import com.kopitech.gowildweather.datasource.geocode.OnGeocodeCallback;
import com.kopitech.gowildweather.datasource.geocode.google.response.Geometry;
import com.kopitech.gowildweather.datasource.geocode.google.response.GoogleGeocodeResponse;
import com.kopitech.gowildweather.datasource.geocode.google.response.Location;
import com.kopitech.gowildweather.datasource.geocode.google.response.Result;
import com.kopitech.gowildweather.datasource.http.HttpDatasource;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by yeehuipoh on 18/3/17.
 */

public class GoogleGeocodeDatasource implements GeocodeDatasource {
    private static final String TAG = "GGD";
    private static final String GOOGLE_MAP_GEOCODE_API_URL_FORMAT = "https://maps.googleapis.com/maps/api/geocode/json?address=%s";

    private ExecutorService executorService;
    private HttpDatasource httpDatasource;
    private Gson gson;

    public GoogleGeocodeDatasource(ExecutorService executorService, HttpDatasource httpDatasource, Gson gson){
        this.executorService = executorService;
        this.httpDatasource = httpDatasource;
        this.gson = gson;
    }

    @Override
    public LocationDto getLocation(String query) {
        // Get URL
        String url = getUrlFromQuery(query);

        // Load URL
        String body = null;
        try {
            body = this.httpDatasource.get(url);
        } catch (IOException e) {
            Log.e(TAG, "IOException when geocoding location", e);
            return null;
        }

        if(body == null){
            return null;
        }
        else if(body.isEmpty()){
            return null;
        }

        // Parse Response
        GoogleGeocodeResponse response = this.gson.fromJson(body, GoogleGeocodeResponse.class);

        // Map to DTO
        List<Result> results = response.getResults();
        if(results.size() < 1){
            return null;
        }

        Result result = results.get(0);
        String formattedAddress = result.getFormattedAddress();
        Log.d(TAG, "Geocoded location: " + formattedAddress);

        Geometry geometry = result.getGeometry();
        Location geometryLocation = geometry.getLocation();

        BigDecimal latitude = geometryLocation.getLat();
        BigDecimal longitude = geometryLocation.getLng();

        LocationDto locationDto = new LocationDto(latitude, longitude);
        return locationDto;
    }

    private String getUrlFromQuery(String query){
        String url = String.format(GOOGLE_MAP_GEOCODE_API_URL_FORMAT, query);
        return url;
    }

    @Override
    public void getLocationInBackground(final String query, final OnGeocodeCallback onGeocodeCallback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LocationDto locationDto = getLocation(query);

                // Callback
                if(onGeocodeCallback != null){
                    onGeocodeCallback.onGeocodeResult(locationDto);
                }
            }
        };

        // Submit to executor service
        this.executorService.submit(runnable);
    }
}
