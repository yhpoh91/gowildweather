package com.kopitech.gowildweather.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.kopitech.gowildweather.R;
import com.kopitech.gowildweather.dagger.DaggerApplication;
import com.kopitech.gowildweather.dataobject.LocationDto;
import com.kopitech.gowildweather.dataobject.SpeechInterpretationDto;
import com.kopitech.gowildweather.dataobject.WeatherDto;
import com.kopitech.gowildweather.datasource.location.LocationDatasource;
import com.kopitech.gowildweather.datasource.location.OnLocationCallback;
import com.kopitech.gowildweather.datasource.weather.WeatherDatasource;
import com.kopitech.gowildweather.speech.interpreter.SpeechInterpreter;
import com.kopitech.gowildweather.speech.interpreter.SpeechInterpreterCallback;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MA";

    private boolean allowWeatherFromCurrentLocation;

    @Inject
    WeatherDatasource weatherDatasource;

    @Inject
    LocationDatasource locationDatasource;

    @Inject
    SpeechInterpreter speechInterpreter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerApplication.component().inject(this);

        this.allowWeatherFromCurrentLocation = false;

        // Request required permission
        requestRequiredPermission();

        startListening();
    }

    private void getWeatherFromCurrentLocation() {
        locationDatasource.getLocationInBackground(new OnLocationCallback() {
            @Override
            public void onReceiveLocationInformation(LocationDto locationDto) {
                WeatherDto weatherDto = weatherDatasource.getCurrentWeather(locationDto);
                Log.i(TAG, weatherDto.toString());
            }
        });
    }

    private void requestRequiredPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1234);
            return;
        }


        getWeatherFromCurrentLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1234){
            if(grantResults.length < 1){
                throw new RuntimeException("Permission Request return no result for access fine location");
            }

            int result = grantResults[0];
            if(result == PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "Permission granted for access fine location");
                this.allowWeatherFromCurrentLocation = true;
                getWeatherFromCurrentLocation();
            }
            else{
                Log.w(TAG, "Permission denied for access fine location");
                Toast.makeText(this, "Location Access Permission Denied", Toast.LENGTH_SHORT).show();
                this.allowWeatherFromCurrentLocation = false;
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startListening(){
        if(!isConnected()){
            Toast.makeText(this, "No internet connectivity", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, 5678);
    }

    public  boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net != null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5678 && resultCode == RESULT_OK) {
            List<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for(String match : matches){
                Log.d(TAG, match);
            }

            if(matches.size() < 1){
                return;
            }

            String matchText = matches.get(0);
            Log.d(TAG, "Choosing " + matchText);

            this.speechInterpreter.interpretInBackground(matchText, new SpeechInterpreterCallback() {
                @Override
                public void onReceiveResult(SpeechInterpretationDto speechInterpretationDto) {
                    Log.d(TAG, speechInterpretationDto.toString());
                }
            });
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
