package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit = null;
    private static final String url = "https://ssyoutube.com/api/";

    public static Retrofit getRetrofitInstance() {
        Gson create = new GsonBuilder().setLenient().create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(create)).build();
        }
        return retrofit;
    }
}
