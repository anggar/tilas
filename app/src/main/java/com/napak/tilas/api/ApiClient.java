package com.napak.tilas.api;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.ToJson;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ApiClient {

    public final static String baseUrl = "http://192.168.42.60:8000";

    public static Retrofit getClient() {

        Object customDateAdapter = new Object() {
            final DateFormat dateFormat;
            {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.getDefault());
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            }

            @ToJson
            synchronized String dateToJson(Date d) {
                return dateFormat.format(d);
            }

            @FromJson
            synchronized Date dateToJson(String s) throws ParseException {
                return dateFormat.parse(s);
            }
        };

        Moshi moshi = new Moshi.Builder().add(customDateAdapter).build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl + "/api/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();
    }
}
