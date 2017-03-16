package com.kopitech.gowildweather.datasource.http;

import java.io.IOException;

/**
 * Created by yeehuipoh on 16/3/17.
 */

public interface HttpDatasource {
    String get(String url) throws IOException;
}
