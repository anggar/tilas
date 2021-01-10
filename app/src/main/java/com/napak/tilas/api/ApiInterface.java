package com.napak.tilas.api;

import com.napak.tilas.model.*;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface ApiInterface {

    @GET("photos/map")
    Call<List<PhotoMap>> listPhotoMap();

    @Multipart
    @POST("photo")
    Call<StatusMessage> uploadPhoto(@PartMap Map<String, RequestBody> params);

    @GET("photos")
    Call<LumenResponse<PhotoListItem>> listPhotos();

    @GET("photo/{id}")
    Call<List<Photo>> listDetailPhoto();
}
