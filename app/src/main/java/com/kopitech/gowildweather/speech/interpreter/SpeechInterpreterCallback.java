package com.kopitech.gowildweather.speech.interpreter;

import com.kopitech.gowildweather.dataobject.SpeechInterpretationDto;

/**
 * Created by yeehuipoh on 17/3/17.
 */

public interface SpeechInterpreterCallback {
    void onReceiveResult(SpeechInterpretationDto speechInterpretationDto);
}
