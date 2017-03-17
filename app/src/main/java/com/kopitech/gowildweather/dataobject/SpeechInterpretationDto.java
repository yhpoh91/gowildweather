package com.kopitech.gowildweather.dataobject;

/**
 * Created by yeehuipoh on 17/3/17.
 */

public class SpeechInterpretationDto {
    private String action;
    private String location;

    public SpeechInterpretationDto(String action, String location) {
        this.action = action;
        this.location = location;
    }

    public String getAction() {
        return action;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        String readableString = String.format("action: %s, location: %s", action, location);
        return readableString;
    }
}
