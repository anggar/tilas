package com.napak.tilas.model;

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
}
