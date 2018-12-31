package com.chsapps.yt_nahoonha.api.model;

import com.chsapps.yt_nahoonha.data.NewsData;
import com.chsapps.yt_nahoonha.data.SingersData;

import java.util.List;

public class NewsAPIData extends BaseAPIData {
    private final static String TAG = SingersData.class.getSimpleName();

    public List<NewsData> message;
    public List<NewsData> getMessage() {
        return message;
    }
}
