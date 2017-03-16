package com.kopitech.gowildweather.dagger;

import com.kopitech.gowildweather.activity.MainActivity;

import dagger.Component;

/**
 * Created by yeehuipoh on 16/3/17.
 */

@Component(modules = { MainModule.class })
public interface GraphComponent {
    void inject(MainActivity mainActivity);

    static final class Initializer {
        private Initializer(){
            // Empty Private Constructor
        }

        public static GraphComponent init(DaggerApplication application){
            return DaggerGraphComponent.builder().mainModule(new MainModule(application)).build();
        }
    }
}
