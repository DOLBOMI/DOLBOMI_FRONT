package com.example.dolbomi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface DolbomiService {

    @GET("/admins/status")
    Call<List<SimpleUser>> listUsers();
}
