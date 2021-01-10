package com.napak.tilas.model;

import com.squareup.moshi.Json;

import java.util.Date;

public class PhotoListItem {
    @Json(name="id")
    int id;

    @Json(name="caption")
    String caption;

    @Json(name="title")
    String title;

    @Json(name="created_at")
    Date created_at;

    @Json(name="photo_url")
    String photo_url;

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}