package com.chsapps.yt_hongjinyoung.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allsoft.app_store.FreeAppStoreActivity;
import com.allsoft.request_song.RequestSongActivity;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BuildConfig;
import com.chsapps.yt_hongjinyoung.constants.ADConstants;
import com.chsapps.yt_hongjinyoung.constants.APIConstants;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.data.PlayerStatus;
import com.chsapps.yt_hongjinyoung.data.YoutubePlayerStatus;
import com.chsapps.yt_hongjinyoung.event.AppEventManager;
import com.chsapps.yt_hongjinyoung.event.ui.popup.MainEventPopup;
import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;
import com.chsapps.yt_hongjinyoung.ui.base.BaseActivity;
import com.chsapps.yt_hongjinyoung.ui.fragment.MainFragment;
import com.chsapps.yt_hongjinyoung.ui.view.popup.LoadingAdPopup;
import com.chsapps.yt_hongjinyoung.ui.view.popup.RecommendedPopup;
import com.chsapps.yt_hongjinyoung.ui.view.popup.ResizeTextSizePopup;
import com.chsapps.yt_hongjinyoung.utils.AdUtils;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;
import com.chsapps.yt_hongjinyoung.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    public final static String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.layer_player)
    ViewGroup layer_player;

    @BindView(R.id.layer_youtube_player)
    ViewGroup layer_youtube_player;

    @BindView(R.id.tv_title)
    TextView tv_title;

    AdView adMobAdView;
    @BindView(R.id.layer_banner_ad)
    RelativeLayout layer_banner_ad;

    @BindView(R.id.btn_play)
    ImageView btn_play;
    @BindView(R.id.btn_close)
    ImageView btn_close;

    DrawerLayout drawer;

    boolean isBannerAdSet = false;
    public boolean isShowAdInterstitial = false;

    public int tempCntMovePlaySongList = 0;

    private boolean isMainEventPopupShowed = false;
    private MainEventPopup mainEventPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent != null){
            boolean isReceive = intent.getBooleanExtra("receive_push",false);
            if(isReceive){
                Bundle bundle = new Bundle();
                bundle.putString("push_type","app_launching");
                FirebaseAnalytics.getInstance(this).logEvent("push_event",bundle);
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
    protected void onDestroy() {
        super.onDestroy();

        if (Global.getInstance().isGenreTypeUpdate0)
            Global.getInstance().setLastLaunchTime(0);

        if (Global.getInstance().isGenreTypeUpdate1)
            Global.getInstance().setLastLaunchTime(1);

        Global.clear();
    }

    @Override
    protected void initialize() {
        tempCntMovePlaySongList = Global.getInstance().cntMovePlaySongList;

//        if (Global.getInstance().isShowInterstitialAdInMainActivity() && !Global.getInstance().isMainActivityAdShow)
        {
            Global.getInstance().isMainActivityAdShow = true;
//            HomeData.AD_CONFIG adConfig = Global.getInstance().getAdConfig();
//            showAdMobInterstitialAd(adConfig);
            showAdMobInterstitialAd();
        }

        initLayout();
        replace(R.id.layout_main, MainFragment.newInstance(getIntent().getExtras()), MainFragment.TAG, false, true);
        setDoubleBackToExit(true);
    }

    @Override
    protected void clearMemory() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mainEventPopup != null && mainEventPopup.isShowing()) {
            return;
        }

        if (Global.getInstance().isShowBannerAdInBottom()) {
            if (!isBannerAdSet) {
                isBannerAdSet = true;
                adMobAdView = new com.google.android.gms.ads.AdView(this);
                layer_banner_ad.addView(adMobAdView);
                AdUtils.getInstance().showAdMobBannerAd(adMobAdView, ADConstants.ADMOBB_AD_BANNER_ID);
            }
        }

        if(!Global.getInstance().isMainActivityAdShow) {
            Global.getInstance().isMainActivityAdShow = true;
            showAdMobInterstitialAd();
            return;
        }

        if(!isMainEventPopupShowed && AppEventManager.getInstance().isShowMainPageEventPopup()) {
            isMainEventPopupShowed = true;
            if(mainEventPopup == null) {
                return;
            }
            mainEventPopup = new MainEventPopup(this, new MainEventPopup.onMainEventPopupListener() {
                @Override
                public void closePopup() {

                }

                @Override
                public void joinEvent() {

                }
            });
            mainEventPopup.show();
        } else {
            if (mainEventPopup != null && mainEventPopup.isShowing()) {
                return;
            }
            if (tempCntMovePlaySongList != Global.getInstance().cntMovePlaySongList) {
                tempCntMovePlaySongList = Global.getInstance().cntMovePlaySongList;
                if (Global.getInstance().isFirstRecommendedApp()) {
                    tempCntMovePlaySongList = Global.getInstance().cntMovePlaySongList = 0;
                    //TODO : 친구에게 추천 팝업.
                    Global.getInstance().setFirstRecommendedApp(false);
                    RecommendedPopup dlg = new RecommendedPopup(this);
                    dlg.show();
                } else {
                    switch (tempCntMovePlaySongList) {
                        case 1:
                            showAdMobInterstitialAd();
                            break;
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePlayerStatus(PlayerStatus status) {
        if (status != null) {
            if (status.playStatus == 1) {
                btn_play.setBackgroundResource(R.drawable.pause);
            } else {
                btn_play.setBackgroundResource(R.drawable.play);
            }
        }
    }

    @OnClick(R.id.layer_player)
    public void onClick_layer_player() {
        Intent intent = new Intent(this, PlaySongActivity.class);
        this.startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_9:
            case R.id.nav_kakaotalk:
                Utils.showKakaoShare(this, Utils.getString(R.string.title_share_application), Utils.getString(R.string.message_share_application));
                break;
            case R.id.menu_6:
            case R.id.nav_search:
                startActivity(new Intent(this, SearchSongActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.menu_7:
            case R.id.nav_grade_star:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MARKET_URL)));
                break;
            case R.id.nav_storage:
                MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
                fragment.updateTab(3);
                drawer.closeDrawers();
                break;
            case R.id.menu_8:
            case R.id.nav_resize_text:
                drawer.closeDrawers();
                ResizeTextSizePopup dlg = new ResizeTextSizePopup(this, new ResizeTextSizePopup.ResizeTextSizePopupListener() {
                    @Override
                    public void onChangedTextSize(int size) {
                        resizeTextSize(true, size);
                    }
                });
                dlg.show();
                break;
            case R.id.menu_10:
            case R.id.nav_another_apps:
                startActivity(new Intent(this, AppStoreActivity.class));
                break;

            case R.id.menu_1:
                Toast.makeText(this, "장르 음악 듣기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_2:
                Toast.makeText(this, "가수 음악 듣기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_3:
                Toast.makeText(this, "트로트 라디오 듣기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_4:
                Toast.makeText(this, "노래 부르기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_5:
                Toast.makeText(this, "운세 보기", Toast.LENGTH_SHORT).show();
                break;
        }


        return false;
    }

    private void initLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.btn_navi);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getHeaderView(0).findViewById(R.id.menu_1).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.menu_2).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.menu_3).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.menu_4).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.menu_5).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.menu_6).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.menu_7).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.menu_8).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.menu_9).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.menu_10).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.menu_request_song).setOnClickListener(this);

        TextView tv_url = navigationView.getHeaderView(0).findViewById(R.id.tv_url);
        if(BuildConfig.IS_DEBUG_MODE) {
            tv_url.setVisibility(View.VISIBLE);
            tv_url.setText(BuildConfig.IS_DEBUG_MODE ? APIConstants.API_TEST_SERVER_URL : APIConstants.API_SERVER_URL);
        } else {
            tv_url.setVisibility(View.GONE);
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
        if (YoutubePlayerService.isVideoPlaying) {
            Intent i = new Intent(context, YoutubePlayerService.class);
            i.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE);
            context.startService(i);
        }
        layer_player.setVisibility(View.GONE);
        layer_banner_ad.setVisibility(View.VISIBLE);

        Global.getInstance().isPlayerClosed = true;

        EventBus.getDefault().post(new YoutubePlayerStatus(true));
    }

//    private void showAdMobInterstitialAd(HomeData.AD_CONFIG adConfig) {
      private void showAdMobInterstitialAd() {
        final LoadingAdPopup dlg = new LoadingAdPopup(this);
        dlg.show();

        am_interstitialAd = new InterstitialAd(this);
        am_interstitialAd.setAdUnitId(ADConstants.ADMOBB_AD_INTERSTITIALS_ID);
        am_interstitialAd.loadAd(AdUtils.getInstance().getAdMobAdRequest());
        am_interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                dlg.dismiss();
                am_interstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();

                AppEventManager.getInstance().showEventNoticePopup(MainActivity.this);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                LogUtil.e(TAG, "admob ad fail : " + errorCode);
                dlg.dismiss();

                AppEventManager.getInstance().showEventNoticePopup(MainActivity.this);
            }
        });

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_9:
            case R.id.nav_kakaotalk:
                Utils.showKakaoShare(this, Utils.getString(R.string.title_share_application), Utils.getString(R.string.message_share_application));
                break;
            case R.id.menu_6:
            case R.id.nav_search:
                startActivity(new Intent(this, SearchSongActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.menu_7:
            case R.id.nav_grade_star:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MARKET_URL)));
                break;
            case R.id.nav_storage:
                MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
                fragment.updateTab(3);
                drawer.closeDrawers();
                break;
            case R.id.menu_8:
            case R.id.nav_resize_text:
                drawer.closeDrawers();
                ResizeTextSizePopup dlg = new ResizeTextSizePopup(this, new ResizeTextSizePopup.ResizeTextSizePopupListener() {
                    @Override
                    public void onChangedTextSize(int size) {
                        resizeTextSize(true, size);
                    }
                });
                dlg.show();
                break;
            case R.id.menu_10:
            case R.id.nav_another_apps:
                startActivity(new Intent(this, FreeAppStoreActivity.class));
                break;

            case R.id.menu_1: {
                MainFragment ft = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
                ft.updateTab(2);
                ft.updateGenreType(0);

                drawer.closeDrawers();
            }
            break;
            case R.id.menu_2: {
                MainFragment ft = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
                ft.updateTab(2);
                ft.updateGenreType(1);

                drawer.closeDrawers();
            }
            break;
            case R.id.menu_3:
                Toast.makeText(this, "트로트 라디오 듣기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_4:
                Toast.makeText(this, "노래 부르기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_5:
                Toast.makeText(this, "운세 보기", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_request_song:
                startActivity(new Intent(this, RequestSongActivity.class));
                break;
        }
    }
}
