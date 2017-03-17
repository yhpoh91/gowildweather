package com.kopitech.gowildweather.speech.interpreter.apiai;

import android.content.Context;
import android.util.Log;

import com.kopitech.gowildweather.R;
import com.kopitech.gowildweather.dataobject.SpeechInterpretationDto;
import com.kopitech.gowildweather.speech.interpreter.SpeechInterpreter;
import com.kopitech.gowildweather.speech.interpreter.SpeechInterpreterCallback;

import java.util.concurrent.ExecutorService;

import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

/**
 * Created by yeehuipoh on 17/3/17.
 */

public class ApiAiSpeechInterpreter implements SpeechInterpreter {
    private static final String TAG = "AASI";

    private Context context;
    private ExecutorService executorService;
    private AIDataService dataService;

    public ApiAiSpeechInterpreter(ExecutorService executorService, Context context){
        this.context = context;
        this.executorService = executorService;

        String accessToken = context.getString(R.string.api_ai_api_key);

        AIConfiguration configuration = new AIConfiguration(accessToken,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        this.dataService = new AIDataService(context, configuration);
    }

    @Override
    public SpeechInterpretationDto interpret(String text) {

        AIRequest aiRequest = new AIRequest(text);
        AIResponse aiResponse = null;
        try {
            aiResponse = dataService.request(aiRequest);
        } catch (AIServiceException e) {
            Log.e(TAG, "AIServiceException when interpreting", e);
            return null;
        }

        // Null Check
        if(aiResponse == null){
            Log.e(TAG, "Null AI Response");
            throw new NullPointerException("Null AI Response");
        }

        // Parse
        Result result = aiResponse.getResult();
        String action = result.getAction();
        String location = result.getStringParameter("location", "");

        SpeechInterpretationDto speechInterpretationDto = new SpeechInterpretationDto(action, location);
        Log.d(TAG, speechInterpretationDto.toString());
        return speechInterpretationDto;
    }

    @Override
    public void interpretInBackground(final String text, final SpeechInterpreterCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SpeechInterpretationDto speechInterpretationDto = interpret(text);

                if(callback != null){
                    callback.onReceiveResult(speechInterpretationDto);
                }
            }
        };

        // Submit to background
        this.executorService.submit(runnable);
    }
}
