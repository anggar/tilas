package com.napak.tilas.api;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiClient {

    public static Retrofit getClient() {

        return new Retrofit.Builder()
                .baseUrl("http://192.168.42.60:8000/api")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }
}
