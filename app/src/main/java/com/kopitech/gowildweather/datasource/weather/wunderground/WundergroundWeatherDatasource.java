package com.kopitech.gowildweather.datasource.weather.wunderground;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.kopitech.gowildweather.R;
import com.kopitech.gowildweather.dataobject.LocationDto;
import com.kopitech.gowildweather.dataobject.WeatherDto;
import com.kopitech.gowildweather.datasource.http.HttpDatasource;
import com.kopitech.gowildweather.datasource.weather.OnWeatherCallback;
import com.kopitech.gowildweather.datasource.weather.WeatherDatasource;
import com.kopitech.gowildweather.datasource.weather.wunderground.response.CurrentObservation;
import com.kopitech.gowildweather.datasource.weather.wunderground.response.WundergroundResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public class WundergroundWeatherDatasource implements WeatherDatasource {
    private static final String TAG = "WWD";
    private static final String GEO_TO_WEATHER_API_URL_FORMAT = "http://api.wunderground.com/api/%s/conditions/q/%s,%s.json";

    private ExecutorService executorService;
    private Context context;
    private HttpDatasource httpDatasource;
    private Gson gson;

    public WundergroundWeatherDatasource(ExecutorService executorService, Context context, HttpDatasource httpDatasource, Gson gson){
        this.executorService = executorService;
        this.context = context;
        this.httpDatasource = httpDatasource;
        this.gson = gson;
    }

    @Override
    public WeatherDto getCurrentWeather(LocationDto locationDto) {
        // Validate input
        validateInput(locationDto);

        // Get URL
        String url = getUrlFromGeolocation(locationDto);

        // Load URL
        String body = null;
        try {
            body = this.httpDatasource.get(url);
        } catch (IOException e) {
            Log.e(TAG, "IOException when getting weather", e);
            return null;
        }

        if(body == null){
            return null;
        }
        else if(body.isEmpty()){
            return null;
        }

        // Parse Response
        WundergroundResponse response = this.gson.fromJson(body, WundergroundResponse.class);

        // Map to DTO
        WeatherDto weatherDto = map(response);

        return weatherDto;
    }

    private void validateInput(LocationDto locationDto) {
        if(locationDto == null){
            throw new NullPointerException("Null Location DTO");
        }
        else if(locationDto.getLatitude() == null){
            throw new IllegalStateException("Null Latitude in location dto");
        }
        else if(locationDto.getLongitude() == null){
            throw new IllegalStateException("Null Longitude in location dto");
        }
    }

    @Override
    public void getCurrentWeatherInBackground(final LocationDto locationDto, final OnWeatherCallback onWeatherCallback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                WeatherDto weatherDto = getCurrentWeather(locationDto);

                // Callback
                if(onWeatherCallback != null){
                    onWeatherCallback.onReceiveWeatherInformation(weatherDto);
                }
            }
        };

        // Submit to executor service
        this.executorService.submit(runnable);
    }

    private String getUrlFromGeolocation(LocationDto locationDto){
        String apiKey = context.getString(R.string.wunderground_api_key);
        String latitude = locationDto.getLatitude().toPlainString();
        String longitude = locationDto.getLongitude().toPlainString();

        String url = String.format(GEO_TO_WEATHER_API_URL_FORMAT, apiKey, latitude, longitude);

        Log.d(TAG, url);
        return url;
    }

    private WeatherDto map(WundergroundResponse wundergroundResponse){
        CurrentObservation currentObservation = wundergroundResponse.getCurrentObservation();

        String weather = currentObservation.getWeather();
        BigDecimal windSpeed = currentObservation.getWindKph();
        BigDecimal feelsLikeTemperature = currentObservation.getFeelslikeC();
        BigDecimal actualTemperature = currentObservation.getTempC();

        WeatherDto weatherDto = new WeatherDto(feelsLikeTemperature, actualTemperature, weather, windSpeed);
        Log.d(TAG, weatherDto.toString());
        return weatherDto;
    }
}
