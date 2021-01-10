package com.napak.tilas.api;

import com.napak.tilas.model.LumenResponse;
import com.napak.tilas.model.Photo;
import com.napak.tilas.model.PhotoListItem;
import com.napak.tilas.model.PhotoMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApiInterface {

    @GET("/photos/map")
    Call<List<PhotoMap>> listPhotoMap();

    @POST("/photo")

    @GET("/photos")
    Call<List<PhotoListItem>> listPhotos();

    @GET("/photo/{id}")
    Call<List<Photo>> listPhoto();
}
