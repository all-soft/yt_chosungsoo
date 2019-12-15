package com.allsoft.request_song;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.ui.base.BaseActivity;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;

public class RequestSongActivity extends BaseActivity {
    public final static String TAG = RequestSongActivity.class.getSimpleName();

    AdView adMobAdView;
    @BindView(R.id.layer_banner_ad)
    RelativeLayout layer_banner_ad;

    boolean isBannerAdSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void initialize() {
        setDoubleBackToExit(false);
        replace(R.id.layout_main, RequestSongFragment.newInstance(getIntent().getExtras()), RequestSongFragment.TAG, false, false);
    }

    @Override
    protected void clearMemory() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
