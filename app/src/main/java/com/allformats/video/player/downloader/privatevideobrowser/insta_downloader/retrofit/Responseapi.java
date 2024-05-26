package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.retrofit;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Responseapi {
    @FormUrlEncoded
    @POST("convert")
    Call<JsonElement> responseapi(@Field("url") String str);
}
