package com.kopitech.gowildweather.datasource.geocode;

import com.kopitech.gowildweather.dataobject.LocationDto;

/**
 * Created by yeehuipoh on 18/3/17.
 */

public interface OnGeocodeCallback {
    void onGeocodeResult(LocationDto locationDto);
}
