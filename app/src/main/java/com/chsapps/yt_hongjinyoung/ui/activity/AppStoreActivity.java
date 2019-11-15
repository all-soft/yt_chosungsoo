package com.chsapps.yt_hongjinyoung.ui.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.ui.base.BaseActivity;
import com.chsapps.yt_hongjinyoung.ui.fragment.AppStoreFragment;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;

public class AppStoreActivity extends BaseActivity {
    public final static String TAG = AppStoreActivity.class.getSimpleName();

    AdView adMobAdView;
    @BindView(R.id.layer_banner_ad)
    RelativeLayout layer_banner_ad;

    boolean isBannerAdSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Global.getInstance().isPlayerClosed = false;
    }

    @Override
    protected void initialize() {
        setDoubleBackToExit(false);
        replace(R.id.layout_main, AppStoreFragment.newInstance(getIntent().getExtras()), AppStoreFragment.TAG, false, false);
    }

    @Override
    protected void clearMemory() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
