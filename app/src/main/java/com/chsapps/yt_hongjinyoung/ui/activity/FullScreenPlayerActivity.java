package com.chsapps.yt_hongjinyoung.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseActivity;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.data.PlayTimeStatus;
import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;
import com.chsapps.yt_hongjinyoung.ui.youtube_player.JavaScript;
import com.chsapps.yt_hongjinyoung.ui.youtube_player.WebPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class FullScreenPlayerActivity extends BaseActivity {

    static public boolean active = false;
    static public Activity fullScreenAct;

    ViewGroup parent;
    WebView player;

    @BindView(R.id.play_button)
    ImageView play_button;
    @BindView(R.id.icon_random)
    ImageView icon_random;
    @BindView(R.id.icon_repeat)
    ImageView icon_repeat;
    @BindView(R.id.control_container)
    ViewGroup control_container;
    @BindView(R.id.seekBar)
    SeekBar seekBar;

    private int currentVideoTotalTime = 0;

    @Override
    protected void initialize() {

    }

    @Override
    protected void clearMemory() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.active = true;
        fullScreenAct = this;
        setContentView(R.layout.activity_fullscreen_player);

        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_fullscreen);
        player = WebPlayer.getPlayer();

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
        );

        parent = (ViewGroup) player.getParent();
        if(parent != null) {
            parent.removeView(player);
        }

        try {
            ll.addView(player, params);
        } catch (Exception e) {
            finish();
        }

        WebPlayer.loadScript(JavaScript.playVideoScript());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    YoutubePlayerService.seek(currentVideoTotalTime * progress / 100, true);
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
    @Override
    public void onBackPressed() {
        if(active){
            ((ViewGroup) player.getParent()).removeView(player);
            if(parent != null) {
                parent.addView(player);
            }
//            YoutubePlayerService.startAgain();
        }
        active = false;
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        if(active) {
            fullScreenAct.onBackPressed();
        }
        active = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.view_touch)
    public void onClick_view_touch() {
        if(control_container.getVisibility() == View.VISIBLE) {
            control_container.setVisibility(View.GONE);
        } else {
            control_container.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.previous_button)
    public void onClick_previous_button() {
        Intent i = new Intent(this, YoutubePlayerService.class);
        i.setAction(PlayerConstants.ACTION.ACTION_PLAY_PREV_SONG);
        this.startService(i);
    }
    @OnClick(R.id.play_button)
    public void onClick_play_button() {
        Intent i = new Intent(this, YoutubePlayerService.class);
        i.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE);
        this.startService(i);
    }
    @OnClick(R.id.next_button)
    public void onClick_next_button() {
        Intent i = new Intent(this, YoutubePlayerService.class);
        i.setAction(PlayerConstants.ACTION.ACTION_PLAY_NEXT_SONG);
        this.startService(i);
    }

    @OnClick(R.id.btn_random)
    public void onClick_btn_random() {
        Global.getInstance().setRandomType();
        updatePlayMode();
    }

    @OnClick(R.id.btn_repeat)
    public void onClick_btn_repeat() {
        Global.getInstance().setRepeatType();
        updatePlayMode();
    }

    private void updatePlayMode() {
//        int randomType = Global.getInstance().getRandomType();
//        int repeatType = Global.getInstance().getRepeatType();
//
//        /**
//         * 0 : none repeat
//         * 1 : repeat all
//         * 2 : repeat one song
//         * */
//        icon_random.setBackgroundResource(randomType == 0 ? R.drawable.none_shuffle : R.drawable.shuffle);
//        switch (repeatType) {
//            case 0:
//                icon_repeat.setBackgroundResource(R.drawable.none_repeat);
//                break;
//            case 1:
//                icon_repeat.setBackgroundResource(R.drawable.repeat);
//                break;
//            case 2:
//                icon_repeat.setBackgroundResource(R.drawable.repeat_one);
//                break;
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void playTime(PlayTimeStatus status) {
        try {
            currentVideoTotalTime = status.totalTime;
            int current = status.currentTime == 0 ? 0 : status.currentTime * 100 / status.totalTime;
            seekBar.setProgress(current);
        } catch (Exception e) {
        }
    }
}
