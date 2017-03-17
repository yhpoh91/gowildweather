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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kopitech.gowildweather.R;
import com.kopitech.gowildweather.dagger.DaggerApplication;
import com.kopitech.gowildweather.dataobject.LocationDto;
import com.kopitech.gowildweather.dataobject.SpeechInterpretationDto;
import com.kopitech.gowildweather.dataobject.WeatherDto;
import com.kopitech.gowildweather.datasource.geocode.GeocodeDatasource;
import com.kopitech.gowildweather.datasource.geocode.OnGeocodeCallback;
import com.kopitech.gowildweather.datasource.location.LocationDatasource;
import com.kopitech.gowildweather.datasource.location.OnLocationCallback;
import com.kopitech.gowildweather.datasource.weather.WeatherDatasource;
import com.kopitech.gowildweather.speech.interpreter.SpeechInterpreter;
import com.kopitech.gowildweather.speech.interpreter.SpeechInterpreterCallback;
import com.kopitech.gowildweather.viewmodel.MainViewModel;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MA";
    public static final int REQUEST_CODE_LISTEN = 5678;
    public static final int REQUEST_CODE_PERMISSION = 1234;

    private boolean allowWeatherFromCurrentLocation;
    private MainViewModel mainViewModel;

    private Button btnListen;
    private TextView tvQuery;
    private TextView tvLocation;
    private TextView tvWeather;
    private TextView tvTemperature;
    private TextView tvFeelsLikeTemperature;
    private TextView tvWindSpeed;

    @Inject
    WeatherDatasource weatherDatasource;

    @Inject
    LocationDatasource locationDatasource;

    @Inject
    SpeechInterpreter speechInterpreter;

    @Inject
    GeocodeDatasource geocodeDatasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerApplication.component().inject(this);

        setupViews();

        this.mainViewModel = new MainViewModel();
        this.allowWeatherFromCurrentLocation = false;

    }

    private void setupViews() {
        this.btnListen = (Button) findViewById(R.id.btn_listen);
        this.tvQuery = (TextView) findViewById(R.id.tv_query);
        this.tvLocation = (TextView) findViewById(R.id.tv_location);
        this.tvWeather = (TextView) findViewById(R.id.tv_weather);
        this.tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        this.tvFeelsLikeTemperature = (TextView) findViewById(R.id.tv_feels_like);
        this.tvWindSpeed = (TextView) findViewById(R.id.tv_windspeed);

        this.btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListening();
            }
        });
    }

    private void requestRequiredPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSION){
            if(grantResults.length < 1){
                throw new RuntimeException("Permission Request return no result for access fine location");
            }

            int result = grantResults[0];
            if(result == PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "Permission granted for access fine location");
                this.allowWeatherFromCurrentLocation = true;
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
        startActivityForResult(intent, REQUEST_CODE_LISTEN);
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
        if (requestCode == REQUEST_CODE_LISTEN && resultCode == RESULT_OK) {
            List<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for(String match : matches){
                Log.d(TAG, match);
            }

            if(matches.size() < 1){
                return;
            }

            // Take first match (assuming first is the most accurate)
            String matchText = matches.get(0);
            Log.d(TAG, "Choosing " + matchText);

            // Reset View
            this.mainViewModel.reset();
            this.mainViewModel.setQuery(matchText);
            refreshViews();

            this.speechInterpreter.interpretInBackground(matchText, new SpeechInterpreterCallback() {
                @Override
                public void onReceiveResult(SpeechInterpretationDto speechInterpretationDto) {
                    if(!speechInterpretationDto.getAction().toLowerCase().equals("weather")){
                        // Not Weather checking, ignore
                        return;
                    }

                    // Check weather at given location
                    checkWeather(speechInterpretationDto);
                }
            });
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkWeather(SpeechInterpretationDto speechInterpretationDto) {
        String location = speechInterpretationDto.getLocation();

        if(location == null || location.isEmpty()){
            // use current location
            this.mainViewModel.setLocation("Current GPS Location");

            // Request required location permission
            requestRequiredPermission();

            this.locationDatasource.getLocationInBackground(new OnLocationCallback() {
                @Override
                public void onReceiveLocationInformation(LocationDto locationDto) {
                    checkWeatherAtLocation(locationDto);
                }
            });
        }
        else{
            // use queried location
            this.mainViewModel.setLocation(location);

            this.geocodeDatasource.getLocationInBackground(location, new OnGeocodeCallback() {
                @Override
                public void onGeocodeResult(LocationDto locationDto) {
                    checkWeatherAtLocation(locationDto);
                }
            });
        }
    }

    private void checkWeatherAtLocation(LocationDto locationDto){
        // Update View Model
        final WeatherDto weatherDto = this.weatherDatasource.getCurrentWeather(locationDto);
        this.mainViewModel.setWeather(weatherDto.getWeather());
        this.mainViewModel.setTemperature(weatherDto.getActualTemperature());
        this.mainViewModel.setFeelsLikeTemperature(weatherDto.getFeelsLikeTemperature());
        this.mainViewModel.setWindSpeed(weatherDto.getWindSpeed());

        // refresh view
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshViews();
            }
        });
    }

    private void refreshViews(){
        this.tvQuery.setText(this.mainViewModel.getQuery());
        this.tvLocation.setText(this.mainViewModel.getLocation());
        this.tvWeather.setText(this.mainViewModel.getWeather());
        this.tvTemperature.setText(this.mainViewModel.getTemperature().setScale(2, BigDecimal.ROUND_FLOOR).toPlainString() + "C");
        this.tvFeelsLikeTemperature.setText(this.mainViewModel.getFeelsLikeTemperature().setScale(2, BigDecimal.ROUND_FLOOR).toPlainString() + "C");
        this.tvWindSpeed.setText(this.mainViewModel.getWindSpeed().toPlainString() + "km/h");
    }
}
