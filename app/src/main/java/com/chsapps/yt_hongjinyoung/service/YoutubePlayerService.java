package com.chsapps.yt_hongjinyoung.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.data.PlayTimeStatus;
import com.chsapps.yt_hongjinyoung.data.PlayerStatus;
import com.chsapps.yt_hongjinyoung.ui.view.player.ConstantStrings;
import com.chsapps.yt_hongjinyoung.ui.view.player.JavaScript;
import com.chsapps.yt_hongjinyoung.ui.view.player.WebPlayer;
import com.chsapps.yt_hongjinyoung.ui.view.player.asynctask.ImageLoadTask;
import com.chsapps.yt_hongjinyoung.ui.view.player.asynctask.LoadDetailsTask;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class YoutubePlayerService extends Service implements View.OnClickListener {


    private static final String TAG = YoutubePlayerService.class.getSimpleName();

    private static Context context;


    /**
     * Static var.
     **/
    private static YoutubePlayerService playerService;

    public static boolean isVideoPlaying = true;
    public static boolean nextVid = false;

    public static boolean replayVid = false;
    public static boolean replayPlaylist = false;

    static boolean isLoopSetPlayList = false;

    private static int noItemsInPlaylist;
    private static int currVideoIndex;

    public static String VID_ID = "";
    public static String PLIST_ID = "";
    public static String PLIST_SONG_IDX = "";
    public static String title, author;

    private static WebPlayer webPlayer;

    private static Intent fullScreenIntent;
    private static Handler secondsHandler = new Handler();

    /**
     * None static var.
     **/
    private WindowManager.LayoutParams param_player, params, parWebView, param_min_player;

    private boolean visible = true;
    private boolean isEntireWidth = false;
    private boolean updateHead = true;

    private int LAYOUT_FLAG;


    public static YoutubePlayerService getInstance() {
        return playerService;
    }

    //function.
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate() {
        LogUtil.i(TAG, "func. onCreate");
        context = this.getApplicationContext();
        super.onCreate();

        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playerService = this;

//        try {
        LogUtil.i(TAG, "func. onStartCommand. action : " + intent.getAction());

        String actionType = intent.getAction();
        if (TextUtils.isEmpty(actionType)) {
            return START_NOT_STICKY;
        }

        if (webPlayer == null) {
            webPlayer = new WebPlayer(this);
            webPlayer.setupPlayer();
        }

        if (intent.getAction().equals(PlayerConstants.ACTION.ACTION_START_FORGROUNT_WEB)) {
            LogUtil.i(TAG, "func. 1");
            secondsHandler.removeCallbacks(seekRunnable);
            //play.
            LogUtil.i(TAG, "func. 2");
            startPlayerService(intent);
            LogUtil.i(TAG, "func. 3");
            setTotalTime();
            LogUtil.i(TAG, "func. 4");
            currentTime = 0;
        } else if (intent.getAction().equals(PlayerConstants.ACTION.ACTION_STOP_FORGROUNT_WEB)) {
            secondsHandler.removeCallbacks(seekRunnable);
            //stop.
            destroyPlayerService();
        } else if (intent.getAction().equals(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE)) {
            secondsHandler.removeCallbacks(seekRunnable);
            setPlayerPlayOrPause();
        } else if (intent.getAction().equals(PlayerConstants.ACTION.ACTION_PLAY_NEXT_SONG)) {
            secondsHandler.removeCallbacks(seekRunnable);
            playNextSong();
            setTotalTime();
            currentTime = 0;
        } else if (intent.getAction().equals(PlayerConstants.ACTION.ACTION_PLAY_PREV_SONG)) {
            secondsHandler.removeCallbacks(seekRunnable);
            playPrevSong();
            setTotalTime();
            currentTime = 0;
        } else if (intent.getAction().equals(PlayerConstants.ACTION.ACTION_CLOSE_PLAYER)) {
            isVideoPlaying = true;
            if (webPlayer != null) {
                webPlayer.destroy();
            }
            webPlayer = null;
        }

//        } catch (Exception e) {
//        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtil.i(TAG, "func. onDestroy");

        EventBus.getDefault().unregister(this);

        isVideoPlaying = true;
        if (webPlayer != null) {
            webPlayer.destroy();
        }
        webPlayer = null;
        //ActivityStack.getInstance().finishActivityStack();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePlayerStatus(PlayerStatus status) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void playTime(PlayTimeStatus status) {
    }

    static int currentTime = 0;
    static int totalTime = 0;
    public static Runnable seekRunnable = new Runnable() {
        @Override
        public void run() {
            secondsHandler.removeCallbacks(seekRunnable);
            secondsHandler.postDelayed(seekRunnable, 1000);
            currentTime++;
            EventBus.getDefault().post(new PlayTimeStatus(totalTime, currentTime));
        }
    };

    public static void setPlayingStatus(int playingStatus) {
        Global.getInstance().setPlayerState(playingStatus);
        EventBus.getDefault().post(new PlayerStatus(playingStatus));

        switch (playingStatus) {
            case -1:
                currentTime = 0;
                secondsHandler.removeCallbacks(seekRunnable);
                playerService.playNextSong();
                break;
            case 0:
                currentTime = 0;
                secondsHandler.removeCallbacks(seekRunnable);
//                LogUtil.e("PLAY.", "setPlayingStatus func) playingStatus. 0");
                playerService.playNextSong();
                break;
            case 1:
                isVideoPlaying = true;
                if (nextVid) {
                    nextVid = false;
                    webPlayer.loadScript(JavaScript.getVidUpdateNotiContent());
                }
                if (VID_ID.length() < 1) {
                    webPlayer.loadScript(JavaScript.getVidUpdateNotiContent());
                }

                //Also Update if playlist is set for loop
                if (!isLoopSetPlayList) {
                    webPlayer.loadScript(JavaScript.setLoopPlaylist());
                    isLoopSetPlayList = true;
                }
                secondsHandler.removeCallbacks(seekRunnable);
                secondsHandler.postDelayed(seekRunnable, 1000);
                break;
            case 2:
                secondsHandler.removeCallbacks(seekRunnable);

                isVideoPlaying = false;
                break;
            case 3:
                secondsHandler.removeCallbacks(seekRunnable);
                webPlayer.loadScript(JavaScript.isPlaylistEnded());
                String quality = PlayerConstants.getPlaybackQuality();
                webPlayer.loadScript(JavaScript.resetPlaybackQuality(quality));
                break;
        }
    }

    public static void isPlaylistEnded() {
        webPlayer.loadScript(JavaScript.isPlaylistEnded());
    }

    public static void setNoItemsInPlaylist(int noItemsInPlaylist) {
        YoutubePlayerService.noItemsInPlaylist = noItemsInPlaylist;
    }

    public static void setCurrVideoIndex(int currVideoIndex) {
        YoutubePlayerService.currVideoIndex = currVideoIndex;
    }

    public static void setTotalTime() {
        totalTime = Global.getInstance().getDuration(ConstantStrings.getCurrentSongVideoId());
    }

    public static Context getAppContext() {
        return context;
    }

    public static void compare() {
        if (YoutubePlayerService.currVideoIndex == YoutubePlayerService.noItemsInPlaylist - 1) {
            replayPlaylist = true;
        }
    }

    public static void startVid(String vId, String pId) {
        YoutubePlayerService.VID_ID = vId;
        YoutubePlayerService.PLIST_ID = pId;
        if (pId == null) {
            setImageTitleAuthor(vId);
            webPlayer.loadScript(JavaScript.loadVideoScript(vId));
        } else {
            Log.d("Starting ", "Playlist.");
            webPlayer.loadScript(JavaScript.loadPlaylistScript(pId));
            setImageTitleAuthor(vId);
        }
    }

    public static void seek(float seconds, boolean allowSeekAhead) {
        secondsHandler.removeCallbacks(seekRunnable);
        currentTime = (int) seconds;

        webPlayer.loadScript(JavaScript.seekTo(seconds, allowSeekAhead));
    }

    /////-----------------*****************----------------onStartCommand---------------*****************-----------


    private void startPlayerService(Intent intent) {
        VID_ID = intent.getStringExtra("VID_ID");
        PLIST_ID = intent.getStringExtra("PLAY_LIST_IDS");
        PLIST_SONG_IDX = intent.getStringExtra("PLAY_LIST_SONG_INDEX");

        Map hashMap = new HashMap();
        hashMap.put("Referer", "http://www.youtube.com");
        ConstantStrings.setPList(VID_ID, PLIST_ID, PLIST_SONG_IDX);
        webPlayer.loadDataWithUrl("https://www.youtube.com/player_api", ConstantStrings.getPlayListHTML(),
                "text/html", null, null);
    }

    private void destroyPlayerService() {
        stopForeground(true);
        stopSelf();
    }

    private void setPlayerPlayOrPause() {
        if (isVideoPlaying) {
            //Pause
            if (replayVid || replayPlaylist) {
                webPlayer.loadScript(JavaScript.replayPlaylistScript());
                replayPlaylist = false;
            } else {
                //Pause Video
                webPlayer.loadScript(JavaScript.pauseVideoScript());
            }
        } else {
            //Play Video
            webPlayer.loadScript(JavaScript.playVideoScript());
        }
    }

    private void playNextSong() {
        //Play Next
        if (!ConstantStrings.setNextSongIndex()) {
            return;
        }
        try {
            webPlayer.loadDataWithUrl("https://www.youtube.com/player_api", ConstantStrings.getPlayListHTML(),
                    "text/html", null, null);
            nextVid = true;
        } catch (Exception e) {

        }

    }

    private void playPrevSong() {
        //Play Previous
        ConstantStrings.setPrevSongIndex();

        try {
            webPlayer.loadDataWithUrl("https://www.youtube.com/player_api", ConstantStrings.getPlayListHTML(),
                    "text/html", null, null);
            nextVid = true;
        } catch (Exception e) {

        }
    }

    //Set Image and Headings in Notification
    public static void setImageTitleAuthor(String videoId) {
        try {
            Bitmap bitmap = new ImageLoadTask("https://i.ytimg.com/vi/" + videoId + "/mqdefault.jpg").execute().get();
            String details = new LoadDetailsTask(
                    "https://www.youtube.com/oembed?url=http://www.youtu.be/watch?v=" + videoId + "&format=json")
                    .execute().get();

            JSONObject detailsJson = new JSONObject(details);
            title = detailsJson.getString("title");
            author = detailsJson.getString("author_name");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void addStateChangeListener() {
        webPlayer.loadScript(JavaScript.onPlayerStateChangeListener());
    }

    private int getStatusBarHeight() {
        int statusBarHeight = (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density);
        return statusBarHeight;
    }

    //Play video again on exit full screen
    public static void startAgain() {
        webPlayer.loadScript(JavaScript.playVideoScript());
    }

    //Clicks Handled
    @Override
    public void onClick(View v) {
    }

    private int playerType = -1;
}