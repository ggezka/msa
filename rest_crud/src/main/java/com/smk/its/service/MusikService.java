package com.smk.its.service;


import com.smk.its.entity.Musik;

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

public interface MusikService {

    @GET("music")
    Call<List<Musik>> getMusikses();

    @POST("music")
    Call<Musik> postMusik(@Body Musik news);

    @DELETE("music/{id}")
    Call<Musik> deleteMusik(@Path("id") int id);

    @GET("music/{id}")
    Call<List<Musik>> getMusikById(@Path("id") int id);

    @PUT("music/{id}")
    Call<Musik> putMusik(@Path("id") int id, @Body Musik news);

}
