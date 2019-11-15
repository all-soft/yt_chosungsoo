package com.chsapps.yt_hongjinyoung.ui.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.ui.base.BaseActivity;
import com.chsapps.yt_hongjinyoung.ui.view.player.JavaScript;
import com.chsapps.yt_hongjinyoung.ui.view.player.WebPlayer;
import com.chsapps.yt_hongjinyoung.utils.DelayListenerListener;
import com.chsapps.yt_hongjinyoung.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class BatterySaveActivity extends BaseActivity {
    public final static String TAG = BatterySaveActivity.class.getSimpleName();

    private WebView player;
    private ViewGroup parent;

    private LayoutParams params;
    private float originallyBrightness;

    @BindView(R.id.layer_youtube_player)
    RelativeLayout layer_youtube_player;
    @BindView(R.id.layer_touch)
    ViewGroup layer_touch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_battery);
    }

    @Override
    protected void initialize() {
        getWindow().setFlags(1024, 1024);

        this.params = getWindow().getAttributes();
        this.originallyBrightness = this.params.screenBrightness;

        Utils.delay(subscription, 1000, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                setBright();
            }
        });

        layer_touch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    params.screenBrightness = originallyBrightness;
                    getWindow().setAttributes(params);
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    Utils.delay(subscription, 1000, new DelayListenerListener() {
                        @Override
                        public void delayedTime() {
                            setBright();
                        }
                    });
                }
                return true;
            }
        });
    }

    @Override
    protected void clearMemory() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        player = WebPlayer.getPlayer();
        if(player != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
            );

            parent = (ViewGroup) player.getParent();
            if(parent != null)
                parent.removeView(player);

            layer_youtube_player.addView(player, params);
            WebPlayer.loadScript(JavaScript.playVideoScript());

            layer_youtube_player.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
    }

    private void setBright() {
        this.params.screenBrightness = 0.1f;
        getWindow().setAttributes(this.params);
    }

    @OnClick(R.id.btn_exit)
    public void onClick_btn_exit() {
        finish();
    }


}
