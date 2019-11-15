package com.chsapps.yt_hongjinyoung.data;

public class GenreLastClickData {
    private final static String TAG = GenreLastClickData.class.getSimpleName();

    public String id;
    public long lastClickTime;

    public GenreLastClickData(String id, long lastClickTime) {
        this.id = id;
        this.lastClickTime = lastClickTime;
    }
}
