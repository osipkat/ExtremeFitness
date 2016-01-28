package com.osipova.extremefitness.network;

import com.osipova.extremefitness.model.VideoResponse;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Osipova Ekaterina on 26.01.2016.
 */
public interface VideoService {

    @GET("api/videos.json")
    Call<VideoResponse> getVideos();
}
