package com.chsapps.yt_nahoonha.api.model;

import com.chsapps.yt_nahoonha.data.SingersData;

import java.util.List;

public class SingersAPIData extends BaseAPIData {
    private final static String TAG = SingersData.class.getSimpleName();

    public List<SingersData> message;
    public List<SingersData> getMessage() {
        return message;
    }
}
