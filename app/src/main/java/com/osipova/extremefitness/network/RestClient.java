package com.osipova.extremefitness.network;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Osipova Ekaterina on 27.01.2016.
 */
public class RestClient {
    public static final String API_BASE_URL = "http://www.extremefitness.ru/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder().baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static VideoService createService() {
        return builder.build().create(VideoService.class);
    }
}
