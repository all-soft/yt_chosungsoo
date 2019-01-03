package com.chsapps.yt_hongjinyoung.ui.youtube_player;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;

import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;

public class JavaScriptInterface {
    Context context;
    static Handler handlerForJavascriptInterface = new Handler();

    public JavaScriptInterface(YoutubePlayerService youtubePlayerService) {
        this.context = youtubePlayerService;
    }

    @JavascriptInterface
    public void showPlayerState(final int status) {
        handlerForJavascriptInterface.post(new Runnable() {
            @Override
            public void run() {
                YoutubePlayerService.setPlayingStatus(status);
            }
        });
    }

    @JavascriptInterface
    public void showVID(final String vId) {
        handlerForJavascriptInterface.post(new Runnable() {
            @Override
            public void run() {
                YoutubePlayerService.setImageTitleAuthor(vId);
            }
        });
    }

    @JavascriptInterface
    public void playlistItems(final String[] items) {
        YoutubePlayerService.setNoItemsInPlaylist(items.length);
        YoutubePlayerService.compare();
    }

    @JavascriptInterface
    public void currVidIndex(final int index) {
        YoutubePlayerService.setCurrVideoIndex(index);
        YoutubePlayerService.compare();
    }
}