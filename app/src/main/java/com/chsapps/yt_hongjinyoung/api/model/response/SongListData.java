package com.chsapps.yt_hongjinyoung.api.model.response;

import com.chsapps.yt_hongjinyoung.data.SongData;

import java.util.List;

public class SongListData extends BaseAPIData {
    private final static String TAG = SongListData.class.getSimpleName();

    public List<SongData> message;
}
