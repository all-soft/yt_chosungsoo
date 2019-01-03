package com.chsapps.yt_hongjinyoung.app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;
import com.chsapps.yt_hongjinyoung.utils.DelayListenerListener;
import com.chsapps.yt_hongjinyoung.utils.Utils;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;

import io.reactivex.disposables.CompositeDisposable;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;

import io.fabric.sdk.android.Fabric;

public class AllSoft extends MultiDexApplication{
    public static volatile Handler applicationHandler = null;


    private static CompositeDisposable subscription = new CompositeDisposable();
    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
        Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppContext.init(base);
    }

    private void initialize() {
        applicationHandler = new Handler(AppContext.getInstance().getMainLooper());
        Stetho.initializeWithDefaults(this);

        initAdmob();
        initFacebook();
        initFcmSubscription();
    }

    private void initAdmob(){
        MobileAds.initialize(this,getString(R.string.ad_app_id));
    }

    private void initFcmSubscription(){
        FirebaseMessaging.getInstance().subscribeToTopic("yt_hongjinyoung");
    }

    private void initFacebook() {
        AppEventsLogger.activateApp(this);
    }

    public static Context getContext() {
        return AppContext.getInstance();
    }


    public static void SetClosePlayerTimer(int msTime) {
        Utils.delay(subscription, msTime, new DelayListenerListener() {
            @Override
            public void delayedTime() {
                if (YoutubePlayerService.isVideoPlaying) {
                    Intent i = new Intent(getContext(), YoutubePlayerService.class);
                    i.setAction(PlayerConstants.ACTION.ACTION_CLOSE_PLAYER);
                    getContext().startService(i);
                }
            }
        });
    }
}
