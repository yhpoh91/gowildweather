
package com.kopitech.gowildweather.datasource.weather.wunderground.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CurrentObservation {

    @SerializedName("weather")
    @Expose
    private String weather;
    @SerializedName("temperature_string")
    @Expose
    private String temperatureString;
    @SerializedName("temp_f")
    @Expose
    private BigDecimal tempF;
    @SerializedName("temp_c")
    @Expose
    private BigDecimal tempC;
    @SerializedName("wind_kph")
    @Expose
    private BigDecimal windKph;
    @SerializedName("feelslike_string")
    @Expose
    private String feelslikeString;
    @SerializedName("feelslike_c")
    @Expose
    private BigDecimal feelslikeC;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public BigDecimal getTempC() {
        return tempC;
    }

    public void setTempC(BigDecimal tempC) {
        this.tempC = tempC;
    }

    public BigDecimal getWindKph() {
        return windKph;
    }

    public void setWindKph(BigDecimal windKph) {
        this.windKph = windKph;
    }

    public BigDecimal getFeelslikeC() {
        return feelslikeC;
    }

    public void setFeelslikeC(BigDecimal feelslikeC) {
        this.feelslikeC = feelslikeC;
    }


}
