package com.kopitech.gowildweather.datasource.http.okhttp;

import com.kopitech.gowildweather.datasource.http.HttpDatasource;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public class OkHttpDatasource implements HttpDatasource {
    @Override
    public String get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        String body = response.body().string();
        return body;
    }
}
