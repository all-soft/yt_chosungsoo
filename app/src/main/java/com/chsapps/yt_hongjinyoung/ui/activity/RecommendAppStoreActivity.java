package com.chsapps.yt_hongjinyoung.ui.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.common.BaseActivity;
import com.chsapps.yt_hongjinyoung.ui.fragment.RecommendAppStoreFragment;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;

public class RecommendAppStoreActivity extends BaseActivity {
    public final static String TAG = RecommendAppStoreActivity.class.getSimpleName();

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
        setDrawerLayout(false);
        setSupportActionBar();

        setDoubleBackToExit(false);
        replace(R.id.layout_main, RecommendAppStoreFragment.newInstance(getIntent().getExtras()), RecommendAppStoreFragment.TAG, false, false);
    }

    @Override
    protected void clearMemory() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
