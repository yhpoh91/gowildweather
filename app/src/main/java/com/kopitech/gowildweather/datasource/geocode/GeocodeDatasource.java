package com.kopitech.gowildweather.datasource.geocode;

import com.kopitech.gowildweather.dataobject.LocationDto;
import com.kopitech.gowildweather.datasource.location.OnLocationCallback;

/**
 * Created by yeehuipoh on 18/3/17.
 */

public interface GeocodeDatasource {
    LocationDto getLocation(String query);
    void getLocationInBackground(String query, OnGeocodeCallback onGeocodeCallback);
}
