package com.kopitech.gowildweather.datasource.weather;

import com.kopitech.gowildweather.dataobject.WeatherDto;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public interface OnWeatherCallback {
    void onReceiveWeatherInformation(WeatherDto weatherDto);
}
