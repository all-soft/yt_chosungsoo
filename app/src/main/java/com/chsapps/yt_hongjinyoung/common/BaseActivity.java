package com.chsapps.yt_hongjinyoung.common;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.data.PlayerStatus;
import com.chsapps.yt_hongjinyoung.data.YoutubePlayerStatus;
import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;
import com.chsapps.yt_hongjinyoung.ui.activity.NavigationListener;
import com.chsapps.yt_hongjinyoung.ui.activity.PlayerActivity;
import com.chsapps.yt_hongjinyoung.ui.view.CicleDialog;
import com.chsapps.yt_hongjinyoung.ui.view.popup.ExitPopup;
import com.chsapps.yt_hongjinyoung.ui.youtube_player.WebPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
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

    private boolean isHaveDrawerlayout = true;

    public CicleDialog cicleDialog;


    public boolean isBannerAdSet = false;
    public boolean isHavePlayer = true;

    @Nullable
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Nullable
    @BindView(R.id.nav_view)
    public NavigationView nav_view;

    @Nullable
    @BindView(R.id.layer_banner_ad)
    public RelativeLayout layer_banner_ad;

    @Nullable
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer_layout;

    @Nullable
    @BindView(R.id.layer_player)
    public View layer_player;

    @Nullable
    @BindView(R.id.layer_youtube_player)
    public ViewGroup layer_youtube_player;

    @Nullable
    @BindView(R.id.tv_title)
    public TextView tv_title;

    @Nullable
    @BindView(R.id.btn_play)
    public ImageView btn_play;

    @Nullable
    @BindView(R.id.btn_close)
    public ImageView btn_close;

    public TextView tv_alarm;
    public Switch switch_alarm;
    public ViewGroup nav_singers;
    public ViewGroup nav_share;
    public ViewGroup nav_search;
    public ViewGroup nav_resize_font;
    public ViewGroup nav_auto_stop;
    public ViewGroup nav_give_grade;
    public ViewGroup nav_another_app;

    protected abstract void initialize();

    protected abstract void clearMemory();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resizeTextSize(Global.getInstance().getResizeTextSize());
        ActivityStack.getInstance().regOnCreateState(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        context = getApplicationContext();

        EventBus.getDefault().register(this);

        initialize();

        if (btn_play != null) {
            btn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick_btn_play();
                }
            });
        }
        if (btn_close != null) {
            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick_btn_close();
                }
            });
        }
        if (layer_player != null) {
            layer_player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick_layer_player();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityStack.getInstance().regOnResumeState(this);

        resizeTextSize(Global.getInstance().getResizeTextSize());
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
                        finish();
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
        if (drawer_layout != null) {
            openDrawer();
        } else {
            onBackPressed();
        }
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
            applicationDestroy();
            finishAffinity();
        } else {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(getApplicationContext(), R.string.message_double_click_exit, Toast.LENGTH_LONG).show();
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

    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    public void hideBannerAd() {
        if (layer_banner_ad != null) {
            layer_banner_ad.setVisibility(View.GONE);
        }
    }

    public void showBannerAd() {
        if (layer_banner_ad != null) {
            layer_banner_ad.setVisibility(View.VISIBLE);
        }
    }

    public void setSupportActionBar() {
        if (toolbar != null) {
            setSupportActionBar(true);
            setSupportActionBar(toolbar);
        }
    }

    public void setSupportActionBar(boolean value) {
        if (toolbar != null) {
            if (value) {
                toolbar.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        }
    }

    public void setDrawerLayout(boolean isSet) {
        if (nav_view != null)
            nav_view.setVisibility(isSet ? View.VISIBLE : View.GONE);
    }

    public void setDrawerLayout(NavigationView.OnNavigationItemSelectedListener listener) {
        if (drawer_layout == null || toolbar == null || nav_view == null) {
            return;
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer();
            }
        });
        nav_view.setNavigationItemSelectedListener(listener);
    }

    public void openDrawer() {
        if (drawer_layout != null) {
            drawer_layout.openDrawer(GravityCompat.START);
        }
    }

    public void closeDrawer() {
        if (drawer_layout != null) {
            drawer_layout.closeDrawer(GravityCompat.START);
        }
    }

    public void setAlaramTitle(String title) {
        if (tv_alarm == null)
            return;
        tv_alarm.setText(title);
    }

    protected void setHavePlayer(boolean value) {
        isHavePlayer = value;
    }

    private void onResumePlayer(boolean isEventBus) {
        if (layer_player != null) {
            if (WebPlayer.getPlayer() != null) {
                layer_banner_ad.setVisibility(View.GONE);
            } else {
                layer_banner_ad.setVisibility(View.VISIBLE);
                layer_player.setVisibility(View.GONE);
            }

            if (!isHavePlayer) {
                layer_player.setVisibility(View.GONE);
            }
            if(isEventBus) {
                EventBus.getDefault().post(new YoutubePlayerStatus(WebPlayer.getPlayer() != null));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePlayerStatus(PlayerStatus status) {
    }

    public void onClick_btn_play() {
        Intent i = new Intent(this, YoutubePlayerService.class);
        i.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE);
        startService(i);
    }

    public void onClick_btn_close() {
        if (YoutubePlayerService.isVideoPlaying) {
            Intent i = new Intent(context, YoutubePlayerService.class);
            i.setAction(PlayerConstants.ACTION.ACTION_PLAY_OR_PAUSE);
            context.startService(i);

            WebPlayer.destroyWebView();
        }
        layer_player.setVisibility(View.GONE);

        onResumePlayer(false);
        EventBus.getDefault().post(new YoutubePlayerStatus(false));
    }

    public void onClick_layer_player() {
        Intent intent = new Intent(this, PlayerActivity.class);
        this.startActivity(intent);
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
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = scale;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }

    NavigationListener navigationListener;
    public void setNavigationListener(NavigationListener listener) {
        this.navigationListener = listener;
    }
    public void findViewNavigationLayer() {
        if(nav_view != null) {
            View parentView = nav_view.getHeaderView(0);
            tv_alarm = parentView.findViewById(R.id.tv_alarm);
            tv_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch_alarm.setChecked(!switch_alarm.isChecked());
                }
            });

            switch_alarm = parentView.findViewById(R.id.switch_alarm);
            switch_alarm.setChecked(Global.getInstance().isAllowNewNotice());
            switch_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(navigationListener == null) return;
                    Global.getInstance().setAllowNewNotice(b);
                    navigationListener.onChanged_switch(b);
                }
            });
            nav_singers = parentView.findViewById(R.id.nav_singers);
            nav_singers.setVisibility(BuildConfig.IS_FIXED_SINGER ? View.GONE : View.VISIBLE);
            nav_singers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(navigationListener == null) return;
                    navigationListener.onClick_navigation(0);
                }
            });
            nav_share = parentView.findViewById(R.id.nav_share);
            nav_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(navigationListener == null) return;
                    navigationListener.onClick_navigation(1);
                }
            });
            nav_search = parentView.findViewById(R.id.nav_search);
            nav_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(navigationListener == null) return;
                    navigationListener.onClick_navigation(2);
                }
            });
            nav_resize_font = parentView.findViewById(R.id.nav_resize_font);
            nav_resize_font.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(navigationListener == null) return;
                    navigationListener.onClick_navigation(3);
                }
            });
            nav_auto_stop = parentView.findViewById(R.id.nav_auto_stop);
            nav_auto_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(navigationListener == null) return;
                    navigationListener.onClick_navigation(4);
                }
            });
            nav_give_grade = parentView.findViewById(R.id.nav_give_grade);
            nav_give_grade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(navigationListener == null) return;
                    navigationListener.onClick_navigation(5);
                }
            });
            nav_another_app = parentView.findViewById(R.id.nav_another_app);
            nav_another_app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(navigationListener == null) return;
                    navigationListener.onClick_navigation(6);
                }
            });
        }
    }
}
