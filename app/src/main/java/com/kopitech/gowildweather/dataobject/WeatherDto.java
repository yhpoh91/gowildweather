package com.kopitech.gowildweather.dataobject;

import java.math.BigDecimal;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public class WeatherDto {
    private BigDecimal feelsLikeTemperature;
    private BigDecimal actualTemperature;
    private String weather;
    private BigDecimal windSpeed;

    public WeatherDto(BigDecimal feelsLikeTemperature, BigDecimal actualTemperature, String weather, BigDecimal windSpeed) {
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.actualTemperature = actualTemperature;
        this.weather = weather;
        this.windSpeed = windSpeed;
    }

    public BigDecimal getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public BigDecimal getActualTemperature() {
        return actualTemperature;
    }

    public String getWeather() {
        return weather;
    }

    public BigDecimal getWindSpeed() {
        return windSpeed;
    }

    @Override
    public String toString() {
        String readableString = String.format("%s, %s, %s, %s", weather, feelsLikeTemperature.toPlainString(), actualTemperature.toPlainString(), windSpeed.toPlainString());
        return readableString;
    }
}
