package com.kopitech.gowildweather.viewmodel;

import java.math.BigDecimal;

/**
 * Created by yeehuipoh on 18/3/17.
 */

public class MainViewModel {
    private String query;
    private String location;
    private String weather;
    private BigDecimal temperature;
    private BigDecimal feelsLikeTemperature;
    private BigDecimal windSpeed;

    public MainViewModel() {
        query = "";
        location = "";
        weather = "";
        temperature = BigDecimal.ZERO;
        feelsLikeTemperature = BigDecimal.ZERO;
        windSpeed = BigDecimal.ZERO;
    }

    public void reset(){
        query = "";
        location = "";
        weather = "";
        temperature = BigDecimal.ZERO;
        feelsLikeTemperature = BigDecimal.ZERO;
        windSpeed = BigDecimal.ZERO;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public void setFeelsLikeTemperature(BigDecimal feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(BigDecimal windSpeed) {
        this.windSpeed = windSpeed;
    }
}
