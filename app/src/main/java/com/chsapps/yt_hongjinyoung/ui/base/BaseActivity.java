package com.chsapps.yt_hongjinyoung.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.ActivityStack;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.data.YoutubePlayerStatus;
import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;
import com.chsapps.yt_hongjinyoung.ui.view.CicleDialog;
import com.chsapps.yt_hongjinyoung.ui.view.DrawerLayoutInstaller;
import com.chsapps.yt_hongjinyoung.ui.view.player.WebPlayer;
import com.chsapps.yt_hongjinyoung.ui.view.popup.ExitPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity {
    private final static String TAG = BaseActivity.class.getSimpleName();

    private Unbinder unbinder;

    protected Context context;
    protected CompositeDisposable subscription = new CompositeDisposable();

    //About exit app from double click.
    private boolean doubleBackToExit = false;
    private boolean doubleBackToExitPressedOnce = false;
    private Handler exitHandler = new Handler();
    private Runnable exitRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    //About BaseFragment.
    private int mainViewId = -1;
    private BaseFragment baseFragment;

    protected com.google.android.gms.ads.InterstitialAd am_interstitialAd;

    private boolean isHaveDrawerlayout = true;

    protected WebView WebYoutubePlayer;
    protected ViewGroup PlayerParent;

    public CicleDialog cicleDialog;

    protected abstract void initialize();

    protected abstract void clearMemory();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resizeTextSize(Global.getInstance().getResizeTextSize());
        if (ActivityStack.getInstance().regOnCreateState(this)) {
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        unbinder = ButterKnife.bind(this);
        context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ActivityStack.getInstance().regOnResumeState(this);
        if (WebPlayer.getPlayer() != null) {
            if(Global.getInstance().isPlayerClosed) {
                EventBus.getDefault().post(new YoutubePlayerStatus(true));
            }
            else {
                EventBus.getDefault().post(new YoutubePlayerStatus(false));
            }
        } else {
            EventBus.getDefault().post(new YoutubePlayerStatus(true));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityStack.getInstance().regOnPauseState(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMemory();

        EventBus.getDefault().unregister(this);
        ActivityStack.getInstance().regOnDestroyState(this);
        if (subscription != null) {
            subscription.dispose();
        }
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onBackPressed() {
        if (baseFragment == null || !baseFragment.onBackPressed()) {
            if (doubleBackToExit) {
                ExitPopup popup = new ExitPopup(this);
                popup.setActionListener(new ExitPopup.CommonPopupActionListener() {
                    @Override
                    public void onActionPositiveBtn() {
                        ActivityStack.getInstance().finishActivityStack();
                    }

                    @Override
                    public void onActionNegativeBtn() {

                    }
                });
                popup.show();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void applicationDestroy() {
        if (exitHandler != null) {
            exitHandler.removeCallbacksAndMessages(null);
        }
        exitHandler = null;
        ActivityStack.getInstance().finishActivityStack();
    }

    /**
     * About Loading Dialog.
     */
    public void showLoading() {
        if (cicleDialog != null && cicleDialog.isShowing()) {
            return;
        }
        cicleDialog = new CicleDialog(BaseActivity.this);
        cicleDialog.show();
    }

    public void showLoading(CharSequence message, boolean cancelable) {
        if (cicleDialog != null && cicleDialog.isShowing()) {
            cicleDialog.dismiss();
            cicleDialog = null;
        }
        cicleDialog = new CicleDialog(BaseActivity.this);
        cicleDialog.setCancelable(cancelable);
        cicleDialog.setMessage(message);
        cicleDialog.show();
    }

    public void dismissLoading() {
        try {
            if (cicleDialog != null && cicleDialog.isShowing()) {
                cicleDialog.dismiss();
                cicleDialog = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * About Double Exit
     */
    public void setDoubleBackToExit(boolean val) {
        doubleBackToExit = val;
    }

    public void doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {

            if (WebPlayer.getPlayer() != null) {
                Intent i = new Intent(this, YoutubePlayerService.class);
                i.putExtra("ACTION_SET_YOUTUBE_PLAYER_TYPE", PlayerConstants.PLAYER_TYPE_FLOATING);
                i.setAction(PlayerConstants.ACTION.ACTION_SET_YOUTUBE_PLAYER_TYPE);
                startService(i);
            }
            applicationDestroy();
            finishAffinity();
        } else {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(getApplicationContext(), R.string.double_click_exit_comments, Toast.LENGTH_LONG).show();
            exitHandler.postDelayed(exitRunnable, 1000);
        }
    }

    public void setActionBarTitle(int strID) {
        setTitle(strID);
    }

    public void setActionBarTitle(String str) {
        setTitle(str);
    }

    /**
     * About Fragment.
     */
    public int getFragmentMainViewID() {
        return this.mainViewId;
    }

    public void replace(int mainViewId, Fragment fragment, String tag, boolean addToBackStack) {
        replace(mainViewId, fragment, tag, addToBackStack, false);
    }

    public void replace(int mainViewId, Fragment fragment, String tag, boolean addToBackStack, boolean doubleBackToExit) {
        if (mainViewId == 0 || fragment == null)
            return;

        this.mainViewId = mainViewId;
        this.baseFragment = (BaseFragment) fragment;
        this.doubleBackToExit = doubleBackToExit;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(mainViewId, fragment, tag);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }

    public void replace(int mainViewId, Fragment fragment, String tag, boolean addToBackStack, boolean doubleBackToExit, Object o) {
        if (mainViewId == 0 || fragment == null)
            return;

        this.mainViewId = mainViewId;
        this.baseFragment = (BaseFragment) fragment;
        this.doubleBackToExit = doubleBackToExit;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(mainViewId, fragment, tag);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }

    private DrawerLayout drawerLayout;
    private LinearLayout layerMenuView;

    public void setInstallDrawer(boolean isInstall) {
        if (isInstall) {
            try {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } catch (NullPointerException e) {
                throw new NullPointerException("setContentView 아래에 정의하세요.");
            }
        }
    }

    public void setDisableDrawer(boolean disable) {
        if (disable) {
            try {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } catch (NullPointerException e) {
                throw new NullPointerException("setContentView 아래에 정의하세요.");
            }
        }
    }

    private void setupDrawer() {
        if (isHaveDrawerlayout) {
            layerMenuView = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_left_menu, null);
            drawerLayout = DrawerLayoutInstaller.from(this)
                    .drawerRoot(R.layout.drawer_root)
                    .drawerLeftView(layerMenuView)
                    .drawerListener(new DrawerLayout.DrawerListener() {
                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {

                        }

                        @Override
                        public void onDrawerOpened(View drawerView) {

                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {

                        }

                        @Override
                        public void onDrawerStateChanged(int newState) {

                        }
                    })
                    .build();
        } else {
        }
    }

    public void openDrawerLayout() {
        if (drawerLayout != null)
            drawerLayout.openDrawer(GravityCompat.START);
    }

    public void resizeTextSize(boolean isReload, int size) {
        resizeTextSize(size);
        if (isReload) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public void resizeTextSize(int size) {
        float scale = Constants.MINIMUM_RESIZE_FONT_SCALE + (((Constants.MAXIMUM_RESIZE_FONT_SCALE - Constants.MINIMUM_RESIZE_FONT_SCALE) / 10) * size);
//        float scale = (float) (0.5 + (0.1 * size));
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = scale;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResumePlayer(YoutubePlayerStatus status) {

    }

}
