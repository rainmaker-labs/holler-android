package com.rainmakerlabs.holler.demo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rainmakerlabs.holler.demo.model.Application;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by thanhtritran on 30/11/16.
 */

public interface HollerService {

    @POST("auth/login/")
    Call<JsonElement> login(@Body JsonObject body);

    @GET("apps")
    Call<JsonElement> applications();

    @POST("subscribers/register/")
    Call<JsonElement> registerSubscriber(@Body JsonElement body);

    @PUT("subscribers/{id}/")
    Call<JsonElement> updateSubscriber(@Path("id") int id, @Body JsonElement body);

    @GET("subscribers/")
    Call<JsonElement> subscribers();

}
