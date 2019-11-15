package com.chsapps.yt_hongjinyoung.api.model.response;

import com.chsapps.yt_hongjinyoung.data.SongData;

import java.util.List;

public class SearchData extends BaseAPIData {
    private final static String TAG = SearchData.class.getSimpleName();

    public List<SongData> message;
}
