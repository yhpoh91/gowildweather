package com.kopitech.gowildweather.dagger;

import dagger.Module;

/**
 * Created by yeehuipoh on 16/3/17.
 */

@Module
public class MainModule {
    private DaggerApplication application;

    public MainModule(DaggerApplication application){
        this.application = application;
    }
}
