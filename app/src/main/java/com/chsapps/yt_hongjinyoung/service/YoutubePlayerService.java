package com.chsapps.yt_hongjinyoung.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.ActivityStack;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.data.PlayTimeStatus;
import com.chsapps.yt_hongjinyoung.data.PlayerStatus;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.db.StorageDBHelper;
import com.chsapps.yt_hongjinyoung.ui.activity.BatterySaveActivity;
import com.chsapps.yt_hongjinyoung.ui.activity.SingerMainActivity;
import com.chsapps.yt_hongjinyoung.ui.view.popup.PlayerMorePopup;
import com.chsapps.yt_hongjinyoung.ui.youtube_player.ConstantStrings;
import com.chsapps.yt_hongjinyoung.ui.youtube_player.JavaScript;
import com.chsapps.yt_hongjinyoung.ui.youtube_player.WebPlayer;
import com.chsapps.yt_hongjinyoung.ui.youtube_player.asynctask.ImageLoadTask;
import com.chsapps.yt_hongjinyoung.ui.youtube_player.asynctask.LoadDetailsTask;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class YoutubePlayerService extends Service implements View.OnClickListener {


    private static final String TAG = YoutubePlayerService.class.getSimpleName();

    private static Context context;


    /**
     * Static var.
     **/
    private static YoutubePlayerService playerService;

    private static WindowManager windowManager;
    private static LayoutInflater inflater;
    private static WindowManager.LayoutParams playerViewParams;

//    private static NotificationManager notificationManager;
//    private static Notification notification;

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

    private static LinearLayout playerView, webPlayerLL;
    private static RemoteViews viewBig;
    private static RemoteViews viewSmall;

    private static Intent fullScreenIntent;
    private static Handler secondsHandler = new Handler();

    /**
     * None static var.
     **/
    private WindowManager.LayoutParams param_player, params, parWebView, param_min_player;

    private boolean visible = true;
    private boolean isEntireWidth = false;
    private boolean updateHead = true;

    private int scrnWidth, scrnHeight, playerWidth, xAtHiding, yAtHiding, xOnAppear, yOnAppear = 0;
    private int LAYOUT_FLAG;

    private FrameLayout webPlayerFrame;
    private RelativeLayout viewToHide;
    private ViewGroup topControl, bottomControl;
    private View btn_to_normal;
    private View btn_home, btn_minimum, btn_close;
    private View btn_share, btn_prev, btn_play, btn_next, btn_save;
    private View layer_white, layer_buttons;
    private View btn_more, btn_favorite;
    private ImageView iv_play;
    private SeekBar seekBar;

    private ScreenOnReceiver screenOnReceiver;
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
        context = this.getApplicationContext();
        super.onCreate();

        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        screenOnReceiver = new ScreenOnReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(screenOnReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playerService = this;
        LogUtil.i(TAG, "func. onStartCommand. action : " + intent.getAction());

        String actionType = intent.getAction();
        if (TextUtils.isEmpty(actionType)) {
            return START_NOT_STICKY;
        }

        if (intent.getAction().equals(PlayerConstants.ACTION.ACTION_START_FORGROUNT_WEB)) {
            secondsHandler.removeCallbacks(seekRunnable);
            //play.
            startPlayerService(intent);
            setTotalTime();
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
        } else if(intent.getAction().equals(PlayerConstants.ACTION.ACTION_CLOSE_PLAYER)) {
            stopSelf();
        }

        //SET LAYER BY PLAYER TYPE.
        int playerLayoutType = intent.getIntExtra("ACTION_SET_YOUTUBE_PLAYER_TYPE", -1);
        if (playerLayoutType != -1) {
            setPlayerType(playerLayoutType);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(screenOnReceiver);
        EventBus.getDefault().unregister(this);

        isVideoPlaying = true;
        if (playerView != null) {
            try {
                windowManager.removeView(playerView);
            } catch (Exception e) {

            }
            webPlayer.destroy();
            webPlayer = null;
        }

        windowManager = null;

        ActivityStack.getInstance().finishActivityStack();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePlayerStatus(PlayerStatus status) {
        if (status != null) {
            if (status.playStatus == 1) {
                iv_play.setBackgroundResource(R.drawable.pause);
            } else {
                iv_play.setBackgroundResource(R.drawable.play);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void playTime(PlayTimeStatus status) {
        int current = 0;
        if(status.totalTime > 0) {
            current = status.currentTime == 0 ? 0 : status.currentTime * 100 / status.totalTime;
        }
        seekBar.setProgress(current);
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
                viewBig.setImageViewResource(R.id.pause_play_video, R.drawable.pause);
                viewSmall.setImageViewResource(R.id.pause_play_video, R.drawable.pause);
//                notificationManager.notify(PlayerConstants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
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
                viewBig.setImageViewResource(R.id.pause_play_video, R.drawable.play);
                viewSmall.setImageViewResource(R.id.pause_play_video, R.drawable.play);
//                notificationManager.notify(PlayerConstants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
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
            viewBig.setImageViewResource(R.id.pause_play_video, R.drawable.ic_replay);
            viewSmall.setImageViewResource(R.id.pause_play_video, R.drawable.ic_replay);
//            notificationManager.notify(PlayerConstants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
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
        currentTime = (int)seconds;

        webPlayer.loadScript(JavaScript.seekTo(seconds, allowSeekAhead));
    }

    /////-----------------*****************----------------onStartCommand---------------*****************-----------
    private void initLayer() {
        if (windowManager == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            InitParams();

            //Player View
            playerView = (LinearLayout) inflater.inflate(R.layout.player_webview, null, false);
            viewToHide = playerView.findViewById(R.id.view_to_hide);
            webPlayerFrame = playerView.findViewById(R.id.web_player_frame);
            webPlayerLL = playerView.findViewById(R.id.web_player_ll);

            webPlayer = new WebPlayer(this);
            webPlayer.setupPlayer();

            viewToHide.addView(webPlayer.getPlayer(), parWebView);

            param_player.gravity = Gravity.TOP | Gravity.LEFT;
            param_player.x = 0;
            param_player.y = 0;
            windowManager.addView(playerView, param_player);

            topControl = playerView.findViewById(R.id.top_control);
            bottomControl = playerView.findViewById(R.id.bottom_control);

            btn_home = playerView.findViewById(R.id.btn_home);
            btn_home.setOnTouchListener(new View.OnTouchListener() {
                private long downtime = 0;
                private int initialX, initialY;
                private float initialTouchX, initialTouchY, finalTouchX, finalTouchY;

                @Override
                public boolean onTouch(View v, final MotionEvent event) {
                    if (isEntireWidth) {
                        playerWidth = scrnWidth;
                    } else {
                        playerWidth = Utils.dp(120);
                    }

                    WindowManager.LayoutParams param_player = (WindowManager.LayoutParams) playerView.getLayoutParams();

                    final Handler handleLongTouch = new Handler();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            downtime = System.currentTimeMillis();

                            initialX = param_min_player.x;
                            initialY = param_min_player.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_UP:
                            finalTouchX = event.getRawX();
                            finalTouchY = event.getRawY();
                            handleLongTouch.removeCallbacksAndMessages(null);
                            if (isClicked(initialTouchX, finalTouchX, initialTouchY, finalTouchY)) {
                                Intent i = new Intent(context, SingerMainActivity.class);
                                i.putExtra(ParamConstants.PARAM_SINGER_DATA, Global.staticSingersData);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            int playerW = Utils.dp(playerType == PlayerConstants.PLAYER_TYPE_FLOATING ? 300 : 120);
                            int playerH = Utils.dp(playerType == PlayerConstants.PLAYER_TYPE_FLOATING ? 250 : 80);

                            int newX, newY;
                            newX = initialX + (int) (event.getRawX() - initialTouchX);
                            newY = initialY + (int) (event.getRawY() - initialTouchY);
                            if (visible) {
                                if (newX < 0) {
                                    param_player.x = param_min_player.x = 0;
                                } else if (playerW + newX > scrnWidth) {
                                    param_player.x = param_min_player.x = scrnWidth - playerW;
                                } else {
                                    param_player.x = param_min_player.x = newX;
                                }
                                if (newY < 0) {
                                    param_player.y = param_min_player.y = 0;
                                } else if (newY + playerH > scrnHeight) {
                                    param_player.y = param_min_player.y = scrnHeight - playerH;
                                } else {
                                    param_player.y = param_min_player.y = newY;
                                }
                                if (visible)
                                    windowManager.updateViewLayout(playerView, param_player);
                            } else {
                            }
                            return true;
                    }
                    return false;
                }

                private boolean isClicked(float startX, float endX, float startY, float endY) {
                    float differenceX = Math.abs(startX - endX);
                    float differenceY = Math.abs(startY - endY);
                    long termTime = System.currentTimeMillis() - downtime;
                    LogUtil.e("HSSEO", "TERMTIME : " + termTime);
                    if (differenceX >= 10 || differenceY >= 10) {
                        return false;
                    }
                    else if(termTime > 300) {
                        return false;
                    }
                    return true;
                }
            });
            btn_minimum = playerView.findViewById(R.id.btn_minimum);
            btn_minimum.setOnClickListener(this);
            btn_close = playerView.findViewById(R.id.btn_close);
            btn_close.setOnClickListener(this);

            View layer_top_move = playerView.findViewById(R.id.layer_top_move);
            layer_top_move.setOnTouchListener(new View.OnTouchListener() {
                private int initialX, initialY;
                private float initialTouchX, initialTouchY, finalTouchX, finalTouchY;

                @Override
                public boolean onTouch(View v, final MotionEvent event) {
                    if (isEntireWidth) {
                        playerWidth = scrnWidth;
                    } else {
                        playerWidth = Utils.dp(120);
                    }

                    WindowManager.LayoutParams param_player = (WindowManager.LayoutParams) playerView.getLayoutParams();

                    final Handler handleLongTouch = new Handler();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = param_min_player.x;
                            initialY = param_min_player.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_UP:
                            finalTouchX = event.getRawX();
                            finalTouchY = event.getRawY();
                            handleLongTouch.removeCallbacksAndMessages(null);
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            int playerW = Utils.dp(playerType == PlayerConstants.PLAYER_TYPE_FLOATING ? 300 : 120);
                            int playerH = Utils.dp(playerType == PlayerConstants.PLAYER_TYPE_FLOATING ? 250 : 80);

                            int newX, newY;
                            newX = initialX + (int) (event.getRawX() - initialTouchX);
                            newY = initialY + (int) (event.getRawY() - initialTouchY);
                            if (visible) {
                                if (newX < 0) {
                                    param_player.x = param_min_player.x = 0;
                                } else if (playerW + newX > scrnWidth) {
                                    param_player.x = param_min_player.x = scrnWidth - playerW;
                                } else {
                                    param_player.x = param_min_player.x = newX;
                                }
                                if (newY < 0) {
                                    param_player.y = param_min_player.y = 0;
                                } else if (newY + playerH > scrnHeight) {
                                    param_player.y = param_min_player.y = scrnHeight - playerH;
                                } else {
                                    param_player.y = param_min_player.y = newY;
                                }
                                if (visible)
                                    windowManager.updateViewLayout(playerView, param_player);
                            } else {
                            }
                            return true;
                    }
                    return false;
                }
            });



            btn_to_normal = playerView.findViewById(R.id.btn_to_normal);
            btn_to_normal.setOnClickListener(this);
            btn_to_normal.setOnTouchListener(new View.OnTouchListener() {
                private int initialX, initialY;
                private float initialTouchX, initialTouchY, finalTouchX, finalTouchY;

                @Override
                public boolean onTouch(View v, final MotionEvent event) {
                    if (isEntireWidth) {
                        playerWidth = scrnWidth;
                    } else {
                        playerWidth = Utils.dp(120);
                    }

                    WindowManager.LayoutParams param_player = (WindowManager.LayoutParams) playerView.getLayoutParams();

                    final Handler handleLongTouch = new Handler();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = param_min_player.x;
                            initialY = param_min_player.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_UP:
                            finalTouchX = event.getRawX();
                            finalTouchY = event.getRawY();
                            handleLongTouch.removeCallbacksAndMessages(null);
                            if (isClicked(initialTouchX, finalTouchX, initialTouchY, finalTouchY)) {
                                btn_to_normal.performClick();
                            }
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            int playerW = Utils.dp(playerType == PlayerConstants.PLAYER_TYPE_FLOATING ? 300 : 120);
                            int playerH = Utils.dp(playerType == PlayerConstants.PLAYER_TYPE_FLOATING ? 250 : 80);

                            int newX, newY;
                            newX = initialX + (int) (event.getRawX() - initialTouchX);
                            newY = initialY + (int) (event.getRawY() - initialTouchY);
                            if (visible) {
                                if (newX < 0) {
                                    param_player.x = param_min_player.x = 0;
                                } else if (playerW + newX > scrnWidth) {
                                    param_player.x = param_min_player.x = scrnWidth - playerW;
                                } else {
                                    param_player.x = param_min_player.x = newX;
                                }
                                if (newY < 0) {
                                    param_player.y = param_min_player.y = 0;
                                } else if (newY + playerH > scrnHeight) {
                                    param_player.y = param_min_player.y = scrnHeight - playerH;
                                } else {
                                    param_player.y = param_min_player.y = newY;
                                }
                                if (visible)
                                    windowManager.updateViewLayout(playerView, param_player);
                            } else {
                            }
                            return true;
                    }
                    return false;
                }

                private boolean isClicked(float startX, float endX, float startY, float endY) {
                    float differenceX = Math.abs(startX - endX);
                    float differenceY = Math.abs(startY - endY);
                    if (differenceX >= 5 || differenceY >= 5) {
                        return false;
                    }
                    return true;
                }
            });
            layer_buttons = playerView.findViewById(R.id.layer_buttons);
            layer_white = playerView.findViewById(R.id.layer_white);
            seekBar = playerView.findViewById(R.id.seekBar);
            btn_share = playerView.findViewById(R.id.btn_share);
            btn_share.setOnClickListener(this);
            btn_prev = playerView.findViewById(R.id.btn_prev);
            btn_prev.setOnClickListener(this);
            btn_play = playerView.findViewById(R.id.btn_play);
            btn_play.setOnClickListener(this);
            iv_play = playerView.findViewById(R.id.iv_play);
            btn_next = playerView.findViewById(R.id.btn_next);
            btn_next.setOnClickListener(this);
            btn_save = playerView.findViewById(R.id.btn_save);
            btn_save.setOnClickListener(this);
            btn_more = playerView.findViewById(R.id.btn_more);
            btn_more.setOnClickListener(this);
            btn_favorite = playerView.findViewById(R.id.btn_favorite);
            btn_favorite.setOnClickListener(this);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser) {
                        YoutubePlayerService.seek(totalTime * progress / 100, true);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    private void initNotification() {
        setImageTitleAuthor(VID_ID);

        //Notification
        viewBig = new RemoteViews(
                this.getPackageName(),
                R.layout.notification_large
        );

        viewSmall = new RemoteViews(
                this.getPackageName(),
                R.layout.notification_small
        );

        //Intent to do things
        Intent doThings = new Intent(this, YoutubePlayerService.class);

        //Notification
//        notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_status_bar)
//                .setVisibility(Notification.VISIBILITY_PUBLIC)
//                .setContent(viewSmall)
//                .setAutoCancel(false);
//        notification = builder.build();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            notification.bigContentView = viewBig;
//        }
//
//        //Set Image and Headings
//        setImageTitleAuthor(VID_ID);
//
//        //stop Service using doThings Intent
//        viewSmall.setOnClickPendingIntent(R.id.stop_service,
//                PendingIntent.getService(getApplicationContext(), 0,
//                        doThings.setAction(PlayerConstants.ACTION.ACTION_STOP_FORGROUNT_WEB), 0));
//
//        viewBig.setOnClickPendingIntent(R.id.stop_service,
//                PendingIntent.getService(getApplicationContext(), 0,
//                        doThings.setAction(PlayerConstants.ACTION.ACTION_STOP_FORGROUNT_WEB), 0));
//
//        //Pause, Play Video using doThings Intent
//        viewSmall.setOnClickPendingIntent(R.id.pause_play_video,
//                PendingIntent.getService(getApplicationContext(), 0,
//                        doThings.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE), 0));
//
//        viewBig.setOnClickPendingIntent(R.id.pause_play_video,
//                PendingIntent.getService(getApplicationContext(), 0,
//                        doThings.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE), 0));
//
//        //Next Video using doThings Intent
//        viewSmall.setOnClickPendingIntent(R.id.next_video,
//                PendingIntent.getService(getApplicationContext(), 0,
//                        doThings.setAction(PlayerConstants.ACTION.ACTION_PLAY_NEXT_SONG), 0));
//
//        viewBig.setOnClickPendingIntent(R.id.next_video,
//                PendingIntent.getService(getApplicationContext(), 0,
//                        doThings.setAction(PlayerConstants.ACTION.ACTION_PLAY_NEXT_SONG), 0));
//
//        //Previous Video using doThings Intent
//        viewBig.setOnClickPendingIntent(R.id.previous_video,
//                PendingIntent.getService(getApplicationContext(), 0,
//                        doThings.setAction(PlayerConstants.ACTION.ACTION_PLAY_PREV_SONG), 0));

        //Start Foreground Service
        //startForeground(PlayerConstants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
    }


    private void startPlayerService(Intent intent) {
        VID_ID = intent.getStringExtra(ParamConstants.PARAM_VID_ID);
        PLIST_ID = intent.getStringExtra(ParamConstants.PARAM_LIST_IDS);
        PLIST_SONG_IDX = intent.getStringExtra(ParamConstants.PARAM_LIST_SONG_INDEX);

        initNotification();
        initLayer();

        Map hashMap = new HashMap();
        hashMap.put("Referer", "http://www.youtube.com");
        ConstantStrings.setPList(VID_ID, PLIST_ID, PLIST_SONG_IDX);
        try {
            webPlayer.loadDataWithUrl("https://www.youtube.com/player_api", ConstantStrings.getPlayListHTML(),
                    "text/html", null, null);
        } catch (Exception e) {
            return;
        }

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        scrnWidth = size.x;
        scrnHeight = size.y;
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
        if(!ConstantStrings.setNextSongIndex()) {
            return;
        }
        try {
            webPlayer.loadDataWithUrl("https://www.youtube.com/player_api", ConstantStrings.getPlayListHTML(),
                    "text/html", null, null);
            nextVid = true;
        } catch (Exception e) {
            return;
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
            return;
        }
    }

    //Set Image and Headings in Notification
    public static void setImageTitleAuthor(String videoId) {
        LogUtil.e("HSSEO", "setImageTitleAuthor videoId : " + videoId);
        try {
            Bitmap bitmap = new ImageLoadTask("https://i.ytimg.com/vi/" + videoId + "/mqdefault.jpg").execute().get();
            String details = new LoadDetailsTask(
                    "https://www.youtube.com/oembed?url=http://www.youtu.be/watch?v=" + videoId + "&format=json")
                    .execute().get();

            LogUtil.e("HSSEO", "setImageTitleAuthor : " + details);
            JSONObject detailsJson = new JSONObject(details);
            title = detailsJson.getString("title");
            author = detailsJson.getString("author_name");

//            viewBig.setImageViewBitmap(R.id.thumbnail, bitmap);
//            viewSmall.setImageViewBitmap(R.id.thumbnail, bitmap);
//
//            viewBig.setTextViewText(R.id.title, title);
//
//            viewBig.setTextViewText(R.id.author, author);
//            viewSmall.setTextViewText(R.id.author, author);

//            notificationManager.notify(PlayerConstants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
        } catch (InterruptedException e) {
            LogUtil.e("HSSEO", "setImageTitleAuthor exception : 1");
            e.printStackTrace();
        } catch (ExecutionException e) {
            LogUtil.e("HSSEO", "setImageTitleAuthor exception : 2");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            LogUtil.e("HSSEO", "setImageTitleAuthor exception : 3");
            e.printStackTrace();
        } catch (JSONException e) {
            LogUtil.e("HSSEO", "setImageTitleAuthor exception : 4");
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
        windowManager.addView(playerView, playerViewParams);
        webPlayer.loadScript(JavaScript.playVideoScript());
    }

    //Clicks Handled
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_normal:
                if (playerType == PlayerConstants.PLAYER_TYPE_FLOATING_MINIMUM) {
                    setPlayerType(PlayerConstants.PLAYER_TYPE_FLOATING);
                }
                break;
            case R.id.btn_home:
                Intent i = new Intent(context, SingerMainActivity.class);
                i.putExtra(ParamConstants.PARAM_SINGER_DATA, Global.staticSingersData);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
            case R.id.btn_minimum:
                setPlayerType(PlayerConstants.PLAYER_TYPE_FLOATING_MINIMUM);
                break;
            case R.id.btn_close:
                destroyPlayerService();
                break;
            case R.id.btn_share:
                Utils.showShare(context,
                        Utils.getText(R.string.title_share_application),
                        Utils.getText(R.string.message_share_application));
                break;
            case R.id.btn_prev:
                playPrevSong();
                break;
            case R.id.btn_play:
                setPlayerPlayOrPause();
                break;
            case R.id.btn_next:
                playNextSong();
                break;
            case R.id.btn_save:
                Intent intent = new Intent(context, BatterySaveActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.song_icon:
                if (visible) {
                    updateHead = true;
                    hidePlayer();
                } else {
                    showPlayer();
                }
                break;
            case R.id.btn_favorite:
                break;
            case R.id.btn_more:
                PlayerMorePopup dlg = new PlayerMorePopup(this, new PlayerMorePopup.MorePopupListener() {
                    @Override
                    public void onClick_battery() {
                        Intent intent = new Intent(context, BatterySaveActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onClick_prev() {
                        playPrevSong();
                    }

                    @Override
                    public void onClick_next() {
                        playNextSong();
                    }

                    @Override
                    public void onClick_favorite() {
                        List<SongData> listPlayerList = Global.getInstance().getPlaySongListData();
                        for(SongData song : listPlayerList) {
                            if(song.getRealVideoId().equals(ConstantStrings.getCurrentSongVideoId())) {
                                StorageDBHelper.getInstance().addData(song);
                            }
                        }
                    }

                    @Override
                    public void onClick_share() {
                        Utils.showShare(context,
                                Utils.getText(R.string.title_share_application),
                                Utils.getText(R.string.message_share_application));
                    }
                });
                dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                dlg.show();
                break;
            default:
                break;
        }
    }

    private void showPlayer() {
        viewToHide.setVisibility(View.VISIBLE);
        xOnAppear = 0;
        yOnAppear = params.y;
        params.x = xAtHiding;
        params.y = yAtHiding;
        param_player.x = xAtHiding;
        param_player.y = yAtHiding;
        windowManager.updateViewLayout(playerView, param_player);
        visible = true;
    }

    private void hidePlayer() {
        xAtHiding = params.x;
        yAtHiding = params.y;
        //To hide the Player View
        final WindowManager.LayoutParams tmpPlayerParams = new WindowManager.LayoutParams(
                100,
                100,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        tmpPlayerParams.x = scrnWidth;
        tmpPlayerParams.y = scrnHeight;
        windowManager.updateViewLayout(playerView, tmpPlayerParams);
        viewToHide.setVisibility(View.GONE);
        if (updateHead) {
            params.x = xOnAppear;
            params.y = yOnAppear;
        }
        visible = false;
    }

    //Layout Params Initialized
    private void InitParams() {
        //Service Head Params
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        //Web Player Params
        parWebView = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );

        //Player View Params
        param_player = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        param_min_player = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

    }

    private int playerType = -1;

    private void setPlayerType(int playerType) {
        if (WebPlayer.getPlayer() == null || topControl == null) {
            return;
        }

        this.playerType = playerType;
        switch (playerType) {
            case PlayerConstants.PLAYER_TYPE_FLOATING: {
                LogUtil.d(TAG, "floating player.");
                showFloatingPlayer();
                removeViewByWindowManager(playerView);

                seekBar.setVisibility(View.VISIBLE);
                topControl.setVisibility(View.VISIBLE);
                bottomControl.setVisibility(View.VISIBLE);

                removeView((ViewGroup) webPlayer.getPlayer().getParent(), webPlayer.getPlayer());

                viewToHide.addView(webPlayer.getPlayer(), parWebView);
                ViewGroup.LayoutParams fillWidthParamFrame = webPlayerFrame.getLayoutParams();
                fillWidthParamFrame.width = Utils.dp(300);
                fillWidthParamFrame.height = Utils.dp(285);
                webPlayerFrame.setLayoutParams(fillWidthParamFrame);
                param_player.gravity = Gravity.TOP | Gravity.LEFT;
                param_player.width = Utils.dp(300);
                param_player.height = Utils.dp(285);
                windowManager.addView(playerView, param_player);

                layer_buttons.setVisibility(View.VISIBLE);
                layer_white.setVisibility(View.VISIBLE);
            }
            break;

            case PlayerConstants.PLAYER_TYPE_FLOATING_MINIMUM: {
                showFloatingPlayer();
                removeViewByWindowManager(playerView);

                seekBar.setVisibility(View.GONE);
                topControl.setVisibility(View.GONE);
                bottomControl.setVisibility(View.GONE);

                ViewGroup.LayoutParams fillWidthParamFrame = webPlayerFrame.getLayoutParams();
                fillWidthParamFrame.width = Utils.dp(120);
                fillWidthParamFrame.height = Utils.dp(80);
                webPlayerFrame.setLayoutParams(fillWidthParamFrame);

                ViewGroup.LayoutParams playerEntireWidPar = WebPlayer.getPlayer().getLayoutParams();
                playerEntireWidPar.width = WindowManager.LayoutParams.MATCH_PARENT;
                playerEntireWidPar.height = Utils.dp(80);
                viewToHide.updateViewLayout(WebPlayer.getPlayer(), playerEntireWidPar);

                param_player.gravity = Gravity.TOP | Gravity.LEFT;
                param_player.width = Utils.dp(120);
                param_player.height = Utils.dp(80);
                windowManager.addView(playerView, param_player);

                layer_buttons.setVisibility(View.GONE);
                layer_white.setVisibility(View.GONE);
            }
            break;

            case PlayerConstants.PLAYER_TYPE_IN_FRAGMENT:
                hideFloatingPlayer();
                removeViewByWindowManager(playerView);

                seekBar.setVisibility(View.GONE);
                topControl.setVisibility(View.GONE);
                bottomControl.setVisibility(View.GONE);

                topControl.setVisibility(View.GONE);
                bottomControl.setVisibility(View.GONE);

                layer_buttons.setVisibility(View.GONE);
                layer_white.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    private void removeView(ViewGroup parentView, View view) {
        try {
            parentView.removeView(view);
        } catch (Exception e) {
        }
    }

    private void removeViewByWindowManager(View view) {
        try {
            windowManager.removeView(view);
        } catch (Exception e) {
        }
    }

    public void showFloatingPlayer() {
        playerView.setVisibility(View.VISIBLE);
    }

    public void hideFloatingPlayer() {
        playerView.setVisibility(View.GONE);
    }


}