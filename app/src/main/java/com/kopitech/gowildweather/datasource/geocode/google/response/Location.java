
package com.kopitech.gowildweather.datasource.geocode.google.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Location {

    @SerializedName("lat")
    @Expose
    private BigDecimal lat;
    @SerializedName("lng")
    @Expose
    private BigDecimal lng;

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

}
