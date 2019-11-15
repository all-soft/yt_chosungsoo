package com.chsapps.yt_hongjinyoung.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.model.response.HomeData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.ADConstants;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.data.PlayerStatus;
import com.chsapps.yt_hongjinyoung.data.YoutubePlayerStatus;
import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;
import com.chsapps.yt_hongjinyoung.ui.base.BaseActivity;
import com.chsapps.yt_hongjinyoung.ui.fragment.SearchSongFragment;
import com.chsapps.yt_hongjinyoung.utils.AdUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchSongActivity extends BaseActivity {
    public final static String TAG = SearchSongActivity.class.getSimpleName();

    @BindView(R.id.layer_player)
    ViewGroup layer_player;

    @BindView(R.id.layer_youtube_player)
    ViewGroup layer_youtube_player;

    @BindView(R.id.tv_title)
    TextView tv_title;

    com.google.android.gms.ads.AdView adMobAdView;
    @BindView(R.id.layer_banner_ad)
    RelativeLayout layer_banner_ad;

    @BindView(R.id.btn_play)
    ImageView btn_play;

    boolean isBannerAdSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void initialize() {
        setDoubleBackToExit(false);
        replace(R.id.layout_main, SearchSongFragment.newInstance(getIntent().getExtras()), SearchSongFragment.TAG, false, false);
    }

    @Override
    protected void clearMemory() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Global.getInstance().isShowBannerAdInBottom()) {
            if(!isBannerAdSet) {
                isBannerAdSet = true;
                HomeData.AD_CONFIG adConfig = null;//Global.getInstance().getAdConfig();
                adMobAdView = new com.google.android.gms.ads.AdView(this);
                layer_banner_ad.addView(adMobAdView);
//                AdUtils.getInstance().showAdMobBannerAd(adMobAdView, adConfig.getBanner_id());
                AdUtils.getInstance().showAdMobBannerAd(adMobAdView, ADConstants.ADMOBB_AD_BANNER_ID);
            }
        } else {
        }

//        WebYoutubePlayer = WebPlayer.getPlayer();
//        if(WebYoutubePlayer != null) {
//            layer_banner_ad.setVisibility(View.GONE);
//            setPlayerLayout();
//        } else{
//            layer_banner_ad.setVisibility(View.VISIBLE);
//            layer_player.setVisibility(View.GONE);
//        }
//
//        if(Global.getInstance().isPlayerClosed) {
//            layer_banner_ad.setVisibility(View.VISIBLE);
//            layer_player.setVisibility(View.GONE);
//        }
    }

    @OnClick(R.id.layer_player)
    public void onClick_layer_player() {
        Intent intent = new Intent(this, PlaySongActivity.class);
        this.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePlayerStatus(PlayerStatus status) {
        if(status != null) {
            if(status.playStatus == 1) {
                btn_play.setBackgroundResource(R.drawable.pause);
            } else {
                btn_play.setBackgroundResource(R.drawable.play);
            }
        }
    }

    @OnClick(R.id.btn_play)
    public void onClick_btn_play() {
        Intent i = new Intent(this, YoutubePlayerService.class);
        i.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE);
        startService(i);
    }

    @OnClick(R.id.btn_close)
    public void onClick_btn_close() {
        if(YoutubePlayerService.isVideoPlaying) {
            Intent i = new Intent(context, YoutubePlayerService.class);
            i.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE);
            context.startService(i);
        }
        layer_player.setVisibility(View.GONE);
        layer_banner_ad.setVisibility(View.VISIBLE);

        Global.getInstance().isPlayerClosed = true;

        EventBus.getDefault().post(new YoutubePlayerStatus(true));
    }
}
