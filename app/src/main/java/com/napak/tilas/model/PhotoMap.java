package com.napak.tilas.model;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.moshi.Json;

public class PhotoMap {
    @Json(name="id")
    int id;

    @Json(name="photo_url")
    String photo_url;

    @Json(name="lat")
    float lat;

    @Json(name="lng")
    float lng;

    public int getId() {
        return id;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }
}
