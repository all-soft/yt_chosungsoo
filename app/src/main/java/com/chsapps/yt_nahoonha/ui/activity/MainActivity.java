package com.chsapps.yt_nahoonha.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.app.Global;
import com.chsapps.yt_nahoonha.common.BaseActivity;
import com.chsapps.yt_nahoonha.ui.fragment.MainFragment;
import com.chsapps.yt_nahoonha.utils.AdUtils;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void initialize() {
        setDrawerLayout(false);
        setSupportActionBar(false);
        replace(R.id.layout_main, MainFragment.newInstance(getIntent().getExtras()), MainFragment.TAG, false, true);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
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
