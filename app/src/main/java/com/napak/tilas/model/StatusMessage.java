package com.napak.tilas.model;

import com.squareup.moshi.Json;

public class StatusMessage {

    @Json(name="status")
    String status;

    @Json(name="message")
    String message;
}
