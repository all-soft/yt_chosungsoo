package com.chsapps.yt_hongjinyoung.data;

public class PlayTimeStatus {
    private final static String TAG = PlayTimeStatus.class.getSimpleName();

    public int totalTime = 0;
    public int currentTime = 0;

    public PlayTimeStatus(int totalTime, int currentTime) {
        this.totalTime = totalTime;
        this.currentTime = currentTime;
    }

}
