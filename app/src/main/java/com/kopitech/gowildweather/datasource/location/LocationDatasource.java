package com.kopitech.gowildweather.datasource.location;

import com.kopitech.gowildweather.dataobject.LocationDto;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public interface LocationDatasource {
    LocationDto getLocation();
    void getLocationInBackground(OnLocationCallback onLocationCallback);
}
