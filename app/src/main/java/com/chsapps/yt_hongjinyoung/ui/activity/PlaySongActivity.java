package com.chsapps.yt_hongjinyoung.ui.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.ui.base.BaseActivity;
import com.chsapps.yt_hongjinyoung.ui.fragment.PlaySongListFragment;

import butterknife.BindView;

public class PlaySongActivity extends BaseActivity {
    public final static String TAG = PlaySongActivity.class.getSimpleName();

    com.google.android.gms.ads.AdView adMobAdView;
    @BindView(R.id.layer_banner_ad)
    RelativeLayout layer_banner_ad;

    boolean isBannerAdSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Global.getInstance().cntMovePlaySongList++;
        Global.getInstance().isPlayerClosed = false;
        Global.getInstance().isShowAdInterstitial = true;
    }

    @Override
    protected void initialize() {
        setDoubleBackToExit(false);
        replace(R.id.layout_main, PlaySongListFragment.newInstance(getIntent().getExtras()), PlaySongListFragment.TAG, false, false);
    }

    @Override
    protected void clearMemory() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
