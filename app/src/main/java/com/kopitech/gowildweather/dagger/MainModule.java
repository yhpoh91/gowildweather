package com.kopitech.gowildweather.dagger;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.kopitech.gowildweather.datasource.http.HttpDatasource;
import com.kopitech.gowildweather.datasource.http.okhttp.OkHttpDatasource;
import com.kopitech.gowildweather.datasource.location.LocationDatasource;
import com.kopitech.gowildweather.datasource.location.gps.GPSLocationDatasource;
import com.kopitech.gowildweather.datasource.weather.WeatherDatasource;
import com.kopitech.gowildweather.datasource.weather.wunderground.WundergroundWeatherDatasource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yeehuipoh on 16/3/17.
 */

@Module
public class MainModule {
    private DaggerApplication application;

    public MainModule(DaggerApplication application){
        this.application = application;
    }

    @Provides
    public Application provideApplication(){
        return this.application;
    }

    @Provides
    public Context provideContext(){
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public ExecutorService provideExecutorService(){
        // Cached Thread Pool
        return Executors.newCachedThreadPool();
    }

    @Provides
    @Singleton
    public Gson provideGson(){
        return new Gson();
    }

    @Provides
    @Singleton
    public HttpDatasource provideHttpDatasource(){
        return new OkHttpDatasource();
    }

    @Provides
    @Singleton
    public LocationDatasource provideLocationDatasource(){
        return new GPSLocationDatasource(provideExecutorService(), provideContext());
    }

    @Provides
    @Singleton
    public WeatherDatasource provideWeatherDatasource(){
        return new WundergroundWeatherDatasource(provideExecutorService(), provideContext(), provideHttpDatasource(), provideGson());
    }
}
