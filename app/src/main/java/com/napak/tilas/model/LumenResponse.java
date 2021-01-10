package com.napak.tilas.model;

import androidx.annotation.Nullable;
import com.squareup.moshi.Json;

import java.util.*;

public class LumenResponse<T> {
    @Json(name="current_page")
    private int current_page;

    @Json(name="data")
    private List<T> data;

    @Json(name="first_page_url")
    private String first_page_url;

    @Nullable
    @Json(name="from")
    private String from;

    @Nullable
    @Json(name="path")
    private String path;

    @Json(name="per_page")
    private int per_page;

    @Nullable
    @Json(name="prev_page_url")
    private String prev_page_url;

    @Nullable
    @Json(name="to")
    private String to;

}
