package com.chsapps.yt_nahoonha.ui.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.common.BaseActivity;
import com.chsapps.yt_nahoonha.service.YoutubePlayerService;
import com.chsapps.yt_nahoonha.ui.youtube_player.JavaScript;
import com.chsapps.yt_nahoonha.ui.youtube_player.WebPlayer;
import com.chsapps.yt_nahoonha.utils.DelayListenerListener;
import com.chsapps.yt_nahoonha.utils.Utils;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BatterySaveActivity extends BaseActivity implements View.OnTouchListener {
    public final static String TAG = BatterySaveActivity.class.getSimpleName();

    private WebView player;
    private ViewGroup parent;

    private LayoutParams params;
    private float originallyBrightness;

    @BindView(R.id.layer_youtube_player)
    RelativeLayout layer_youtube_player;
    @BindView(R.id.layer_touch)
    ViewGroup layer_touch;
    @BindView(R.id.btn_slicer)
    View btn_slicer;
    @BindView(R.id.layer_close_btn)
    View layer_close_btn;

    private boolean isDownBtnSlicer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_battery);
    }

    @Override
    protected void initialize() {
        setHavePlayer(false);
        getWindow().setFlags(1024, 1024);

        this.params = getWindow().getAttributes();
        this.originallyBrightness = this.params.screenBrightness;
        btn_slicer.setVisibility(View.GONE);

        Utils.delay(subscription, 100, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                initSlicer();
            }
        });
        subscription.add(Observable.empty()
                .delay(1000, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                               @Override
                               public void accept(Object o) throws Exception {

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {

                               }
                           }, new Action() {
                               @Override
                               public void run() throws Exception {
                                   setBright();
                               }
                           }
                ));
        layer_touch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    params.screenBrightness = originallyBrightness;
                    getWindow().setAttributes(params);
                } else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    subscription.add(Observable.empty()
                            .delay(1000, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {

                                }
                            }, new Action() {
                                @Override
                                public void run() throws Exception {
                                    setBright();
                                }
                            }));
                }
                return true;
            }
        });
        layer_close_btn.setOnTouchListener(this);
    }

    @Override
    protected void clearMemory() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        player = WebPlayer.getPlayer();
        if(player != null) {
            YoutubePlayerService.getInstance().hideFloatingPlayer();

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
            );

            parent = (ViewGroup) player.getParent();
            if(parent != null)
                parent.removeView(player);

            layer_youtube_player.addView(player, params);
            WebPlayer.loadScript(JavaScript.playVideoScript());
        }
    }

    private int screenWidth = 0, screenHeight = 0;
    private void initSlicer() {
        DisplayMetrics bundle = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(bundle);
        screenHeight =  bundle.heightPixels;
        screenWidth = bundle.widthPixels;

        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) btn_slicer.getLayoutParams();
        if(param != null) {
            param.setMarginStart(screenWidth / 2 - Utils.dp(78) / 2);
            btn_slicer.setLayoutParams(param);
        }
        btn_slicer.setVisibility(View.VISIBLE);
    }
    private void setBright() {
        this.params.screenBrightness = 0.1f;
        getWindow().setAttributes(this.params);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        int action = motionEvent.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN: {
                isDownBtnSlicer = true;
                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                if(isDownBtnSlicer) {
                    RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) btn_slicer.getLayoutParams();
                    if(param != null) {
                        float ratio = x / (float)screenWidth;
                        int marginStart = (int) (screenWidth * ratio) - Utils.dp(78) / 2;
                        if(x + Utils.dp(78) / 2 > screenWidth) {
                            marginStart = screenWidth - Utils.dp(78);
                        } else if(x - Utils.dp(78) / 2 < 0) {
                            marginStart = 0;
                        }
                        param.setMarginStart(marginStart);
                        btn_slicer.setLayoutParams(param);
                    }
                }
                return true;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE: {
                isDownBtnSlicer = false;
                initSlicer();

                float ratio = x / (float)screenWidth;
                if(ratio > 0.9f) {
                    Utils.delay(subscription, 200, new DelayListenerListener() {
                        @Override
                        public void delayedTime() {
                            BatterySaveActivity.this.finish();
                        }
                    });
                }
                return true;
            }
        }
        return true;
    }
}
