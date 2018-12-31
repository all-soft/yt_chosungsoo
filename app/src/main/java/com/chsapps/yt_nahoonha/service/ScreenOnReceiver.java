package com.chsapps.yt_nahoonha.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;

import com.chsapps.yt_nahoonha.constants.PlayerConstants;
import com.chsapps.yt_nahoonha.ui.youtube_player.WebPlayer;
import com.chsapps.yt_nahoonha.utils.LogUtil;

public class ScreenOnReceiver extends BroadcastReceiver {
    public static final String TAG = ScreenOnReceiver.class.getSimpleName();
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_ON)) {

        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            LogUtil.e(TAG, "SCREEN OFF.");
            WebView WebYoutubePlayer = WebPlayer.getPlayer();
            if (WebYoutubePlayer != null && YoutubePlayerService.isVideoPlaying) {
                Intent i = new Intent(context, YoutubePlayerService.class);
                i.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE);
                context.startService(i);
            }
        }
    }
}