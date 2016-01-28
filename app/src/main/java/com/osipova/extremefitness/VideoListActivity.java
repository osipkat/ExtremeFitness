package com.osipova.extremefitness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.osipova.extremefitness.adapter.VideoRecyclerViewAdapter;
import com.osipova.extremefitness.model.Video;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Videos.
 */
public class VideoListActivity extends AppCompatActivity {

    private List<Video> videos;
    private VideoRecyclerViewAdapter adapter;

    private View recyclerView;
    private View loadButton;
    private View progress;

    private StateManager stateManager;
    private BroadcastReceiver loadVideosBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        recyclerView = findViewById(R.id.video_list);
        loadButton = findViewById(R.id.loadButton);
        progress = findViewById(R.id.progress);

        stateManager = new StateManager() {
            @Override
            void showPreLoadState() {
                progress.setVisibility(View.INVISIBLE);
                loadButton.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }

            @Override
            void showLoadingState() {
                progress.setVisibility(View.VISIBLE);
                loadButton.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }

            @Override
            void showPostLoadState() {
                progress.setVisibility(View.INVISIBLE);
                loadButton.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        };

        if (savedInstanceState != null) {
            videos = Parcels.unwrap(savedInstanceState.getParcelable(LoadVideosService.KEY_VIDEOS));
            stateManager.onRestoreInstanceState(savedInstanceState);
        } else {
            videos = new ArrayList<>();
            stateManager.setState(StateManager.STATE_PRELOAD);
        }

        if (stateManager.isLoading()) {
            registerReceiver();
        }

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateManager.setState(StateManager.STATE_LOADING);
                registerReceiver();
                VideoListActivity.this.startService(new Intent(VideoListActivity.this, LoadVideosService.class));
            }
        });
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void registerReceiver() {
        loadVideosBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                videos.clear();
                videos.addAll((List<Video>) Parcels.unwrap(intent
                        .getParcelableExtra(LoadVideosService.KEY_VIDEOS)));
                adapter.notifyDataSetChanged();
                stateManager.setState(StateManager.STATE_POSTLOAD);
                LocalBroadcastManager.getInstance(VideoListActivity.this).unregisterReceiver(loadVideosBroadcastReceiver);
            }
        };
        LocalBroadcastManager.getInstance(VideoListActivity.this).registerReceiver(
                loadVideosBroadcastReceiver,
                new IntentFilter(LoadVideosService.LOAD_VIDEOS_BROADCAST_ACTION));
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new VideoRecyclerViewAdapter(videos);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        stateManager.onSaveInstanceState(outState);
        outState.putParcelable(LoadVideosService.KEY_VIDEOS, Parcels.wrap(videos));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(VideoListActivity.this)
                .unregisterReceiver(loadVideosBroadcastReceiver);
        super.onDestroy();
    }
}
