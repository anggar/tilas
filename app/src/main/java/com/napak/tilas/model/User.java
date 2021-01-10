package com.napak.tilas.model;

import com.squareup.moshi.Json;

import java.util.Date;

public class User {

    @Json(name="user_id")
    private String user_id;

    @Json(name="created_at")
    Date created_at;
}
