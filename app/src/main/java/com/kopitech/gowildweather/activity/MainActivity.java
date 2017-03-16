package com.kopitech.gowildweather.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kopitech.gowildweather.R;
import com.kopitech.gowildweather.dagger.DaggerApplication;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerApplication.component().inject(this);
    }
}
