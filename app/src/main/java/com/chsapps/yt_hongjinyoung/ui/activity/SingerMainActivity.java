package com.chsapps.yt_hongjinyoung.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseActivity;
import com.chsapps.yt_hongjinyoung.ui.fragment.MainFragment;
import com.chsapps.yt_hongjinyoung.ui.fragment.singer.SingerMainFragment;
import com.chsapps.yt_hongjinyoung.ui.fragment.singer.SingerNewsFragment;
import com.chsapps.yt_hongjinyoung.ui.fragment.singer.SingerPopularFragment;
import com.chsapps.yt_hongjinyoung.ui.fragment.singer.SingerVideoFragment;
import com.chsapps.yt_hongjinyoung.ui.view.popup.ResizeTextSizePopup;
import com.chsapps.yt_hongjinyoung.utils.AdUtils;
import com.chsapps.yt_hongjinyoung.utils.Utils;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

public class SingerMainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void initialize() {
        setDrawerLayout(true);
        setDrawerLayout(this);
        setSupportActionBar();

        SingerPopularFragment.clearData();
        SingerVideoFragment.clearData();
        SingerNewsFragment.clearData();

        findViewNavigationLayer();
        replace(R.id.layout_main, SingerMainFragment.newInstance(getIntent().getExtras()), MainFragment.TAG, false, true);
    }

    @Override
    protected void clearMemory() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Intent intent = getIntent();
        if (intent != null) {
            boolean isReceive = intent.getBooleanExtra("receive_push", false);
            if (isReceive) {
                Bundle bundle = new Bundle();
                bundle.putString("push_type", "app_launching");
                FirebaseAnalytics.getInstance(this).logEvent("push_event", bundle);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null){
            boolean isReceive = intent.getBooleanExtra("receive_push",false);
            if(isReceive){
                Bundle bundle = new Bundle();
                bundle.putString("push_type","app_launching");
                FirebaseAnalytics.getInstance(this).logEvent("push_event",bundle);
            }
        }
        setIntent(intent);
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
        switch (id) {
            case R.id.nav_singers:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.nav_share:
                Utils.showShare(this, Utils.getText(R.string.title_share_application), Utils.getText(R.string.message_share_application));
                break;
            case R.id.nav_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.nav_resize_font: {
                ResizeTextSizePopup dlg = new ResizeTextSizePopup(this, new ResizeTextSizePopup.ResizeTextSizePopupListener() {
                    @Override
                    public void onChangedTextSize(int size) {
                        resizeTextSize(true, size);
                    }
                });
                dlg.show();
                }
                break;
            case R.id.nav_auto_stop: {
//                AutoClosePopup dlg = new AutoClosePopup(this);
//                dlg.show();
                }
                break;
            case R.id.nav_give_grade:
                break;
            case R.id.nav_another_app:
                break;
        }

        closeDrawer();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Global.getInstance().isShowInterstitialAdInMainActivity() && !Global.getInstance().isMainActivityAdShow)
        {
            Global.getInstance().isMainActivityAdShow = true;
            AdUtils.showInterstitialAd(this);
        }
        isBannerAdSet = AdUtils.addBannerView(this, isBannerAdSet, layer_banner_ad);
    }
}
