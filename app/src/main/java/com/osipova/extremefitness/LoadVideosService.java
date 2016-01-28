package com.osipova.extremefitness;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.osipova.extremefitness.model.Video;
import com.osipova.extremefitness.model.VideoResponse;
import com.osipova.extremefitness.network.RestClient;
import com.osipova.extremefitness.network.VideoService;

import org.parceler.Parcels;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Osipova Ekaterina on 27.01.2016.
 */
public class LoadVideosService extends IntentService {

    public static final String LOAD_VIDEOS_BROADCAST_ACTION = "LOAD_VIDEOS_BROADCAST_ACTION";
    public static final String KEY_VIDEOS = "KEY_VIDEOS";

    public LoadVideosService() {
        super("LoadVideoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadVideos();
    }

    private void loadVideos() {
        Call<VideoResponse> videoResponseCall = RestClient.createService().getVideos();
        videoResponseCall.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Response<VideoResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    VideoResponse videoResponse = response.body();
                    sendBroadcast(videoResponse.getData());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void sendBroadcast(List<Video> videos) {
        Intent localIntent = new Intent(LOAD_VIDEOS_BROADCAST_ACTION)
                        .putExtra(KEY_VIDEOS, Parcels.wrap(videos));
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
