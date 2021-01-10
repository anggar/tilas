package com.napak.tilas.model;

import com.squareup.moshi.Json;

import java.util.Date;

public class Photo {
    @Json(name="id")
    int id;

    @Json(name="title")
    String title;

    @Json(name="caption")
    String caption;

    @Json(name="photo_url")
    String photo_url;

    @Json(name="photo_title")
    String photo_title;

    @Json(name="lat")
    float lat;

    @Json(name="lng")
    float lng;

    @Json(name="luminance")
    float luminance;

    @Json(name="author")
    int author;

    @Json(name="created_at")
    Date created_at;

    @Json(name="updated_at")
    Date updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getPhoto_title() {
        return photo_title;
    }

    public void setPhoto_title(String photo_title) {
        this.photo_title = photo_title;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getLuminance() {
        return luminance;
    }

    public void setLuminance(float luminance) {
        this.luminance = luminance;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}