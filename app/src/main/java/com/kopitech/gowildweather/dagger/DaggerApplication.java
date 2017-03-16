package com.kopitech.gowildweather.dagger;

import android.app.Application;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public class DaggerApplication extends Application {
    private static final String TAG = "DApp";
    private static GraphComponent graphComponent;
    private static DaggerApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        buildComponentGraph();
    }

    public static GraphComponent component(){
        return graphComponent;
    }

    public static void buildComponentGraph(){
        graphComponent = GraphComponent.Initializer.init(instance);
    }
}
