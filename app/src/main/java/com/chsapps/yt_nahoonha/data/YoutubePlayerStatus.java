package com.chsapps.yt_nahoonha.data;

public class YoutubePlayerStatus {
    private final static String TAG = YoutubePlayerStatus.class.getSimpleName();

    //flase : 플레이어 실행 중.
    //true : 플레이어 실행 아님.
    public boolean playStatus = false;

    public YoutubePlayerStatus(boolean playerStatus) {
        this.playStatus = playerStatus;
    }

}