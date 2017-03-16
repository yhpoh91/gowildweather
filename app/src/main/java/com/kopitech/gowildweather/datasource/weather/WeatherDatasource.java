package com.kopitech.gowildweather.datasource.weather;

import com.kopitech.gowildweather.dataobject.LocationDto;
import com.kopitech.gowildweather.dataobject.WeatherDto;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public interface WeatherDatasource {
    WeatherDto getCurrentWeather(LocationDto locationDto);
    void getCurrentWeatherInBackground(LocationDto locationDto, OnWeatherCallback onWeatherCallback);
}
