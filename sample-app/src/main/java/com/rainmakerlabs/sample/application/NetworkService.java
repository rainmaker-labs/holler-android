package com.rainmakerlabs.sample.application;

import com.google.gson.JsonObject;
import com.rainmakerlabs.networking.model.NetworkResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by thanhtritran on 17/10/16.
 */

public interface NetworkService {

    @POST("user/update/location/{id}")
    Call<NetworkResponse> updateUserLocation(@Path("id") long id, @Body JsonObject body);

    @GET("user/{id}")
    Call<NetworkResponse> getUserDetails(@Path("id") long id);

    @GET("users/{page}")
    Call<NetworkResponse> getUsers(@Path("page") int page);
}
