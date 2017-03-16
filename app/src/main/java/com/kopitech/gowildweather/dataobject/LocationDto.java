package com.kopitech.gowildweather.dataobject;

import java.math.BigDecimal;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public class LocationDto {
    private BigDecimal latitude;
    private BigDecimal longitude;

    public LocationDto(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        String readableString = String.format("lat:%s, lng:%s", latitude.toPlainString(), longitude.toPlainString());
        return readableString;
    }
}
