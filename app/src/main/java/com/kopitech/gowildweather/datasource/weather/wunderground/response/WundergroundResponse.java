
package com.kopitech.gowildweather.datasource.weather.wunderground.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WundergroundResponse {

    @SerializedName("current_observation")
    @Expose
    private CurrentObservation currentObservation;

    public CurrentObservation getCurrentObservation() {
        return currentObservation;
    }

    public void setCurrentObservation(CurrentObservation currentObservation) {
        this.currentObservation = currentObservation;
    }

}
