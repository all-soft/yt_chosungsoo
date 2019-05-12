package com.chsapps.yt_hongjinyoung.ui.youtube_player;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.service.PlayAPIService;
import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;

public class WebPlayer {

    static WebView player;
    Context context;

    public WebPlayer(Context context) {
        this.player = new WebView(context);
        this.context = context;
    }

    public void setupPlayer() {
        player.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            player.setWebContentsDebuggingEnabled(true);
        }

        player.setWebChromeClient(new WebChromeClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            player.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        player.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.2; Win64; x64; rv:21.0.0) Gecko/20121011 Firefox/21.0.0");
        player.addJavascriptInterface(new JavaScriptInterface((YoutubePlayerService) context), "Interface");
        player.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        return true;
                                    }

                                    @Override
                                    public void onPageFinished(WebView view, String url) {
                                        YoutubePlayerService.addStateChangeListener();
                                    }
                                }
        );


    }

    public static void loadScript(String s) {
        if(player != null)
            player.loadUrl(s);
    }

    public static WebView getPlayer() {
        return player;
    }

    public static void destroyWebView() {
        if (player != null)
            player.destroy();
        player = null;
    }

    public void destroy() {
        if (player != null)
            player.destroy();
        player = null;
    }

    public void loadDataWithUrl(String baseUrl, String videoHTML, String mimeType, String encoding, String historyUrl) {
        if(player == null) {
            player = new WebView(context);
            setupPlayer();
        }

        sendPlayAPI();
        player.loadDataWithBaseURL(baseUrl, videoHTML, mimeType, encoding, historyUrl);
    }

    private void sendPlayAPI() {
        Intent i = new Intent(context, PlayAPIService.class);
        i.setAction(PlayAPIService.ACTION_TYPE_PLAY);
        i.putExtra(ParamConstants.PARAM_PLAY_SONG_IDX, 0);
        context.startService(i);
    }
}