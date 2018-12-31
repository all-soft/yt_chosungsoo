package com.chsapps.yt_nahoonha.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseActivity;
import com.chsapps.yt_nahoonha.ui.fragment.SearchFragment;
import com.chsapps.yt_nahoonha.utils.AdUtils;

public class SearchActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void initialize() {
        setDrawerLayout(false);
        setSupportActionBar();
        replace(R.id.layout_main, SearchFragment.newInstance(getIntent().getExtras()), SearchFragment.TAG, false, false);
    }

    @Override
    protected void clearMemory() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_no_drawer);
    }

    @Override
    public void onBackPressed() {
        if(drawer_layout != null) {
            if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
                closeDrawer();
                return;
            }
        }
        super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        closeDrawer();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.getInstance().isShowInterstitialAdInMainActivity() && !Global.getInstance().isMainActivityAdShow) {
            Global.getInstance().isMainActivityAdShow = true;
            AdUtils.showInterstitialAd(this);
        }
        isBannerAdSet = AdUtils.addBannerView(this, isBannerAdSet, layer_banner_ad);
    }
}
