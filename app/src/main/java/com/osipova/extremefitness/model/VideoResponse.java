package com.osipova.extremefitness.model;

import java.util.List;

/**
 * Created by Osipova Ekaterina on 26.01.2016.
 */
public class VideoResponse {
    String status;
    List<Video> data;

    public String getStatus() {
        return status;
    }

    public List<Video> getData() {
        return data;
    }
}
