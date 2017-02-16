package com.smk.its.service;

import com.smk.its.entity.Genre;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by smkpc9 on 22/12/16.
 */

public interface GenreService {

    @GET("genre")
    Call<List<Genre>> getCategory();

    @POST("genre")
    Call<Genre> postCategory(@Body Genre genre);

    @DELETE("genre/{id")
    Call<Genre> deleteCategory(@Path("id") int id);

    @GET("genre/{id}")
    Call<List<Genre>> getCategoryById(@Path("id") int id);

    @PUT("genre/{id}")
    Call<Genre> putCategory(@Path("id") int id, @Body Genre genre);

}
