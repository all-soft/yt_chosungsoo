package com.chsapps.yt_hongjinyoung.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.constants.PlayerConstants;
import com.chsapps.yt_hongjinyoung.service.YoutubePlayerService;
import com.chsapps.yt_hongjinyoung.utils.DelayListenerListener;
import com.chsapps.yt_hongjinyoung.utils.Utils;
import com.crashlytics.android.Crashlytics;
//import com.facebook.appevents.AppEventsLogger;
import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;

import com.chsapps.yt_hongjinyoung.service.ScreenOnReceiver;
import com.google.firebase.messaging.FirebaseMessaging;

import io.fabric.sdk.android.Fabric;
import io.reactivex.disposables.CompositeDisposable;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEventFailure;
import com.adjust.sdk.AdjustEventSuccess;
import com.adjust.sdk.AdjustSessionFailure;
import com.adjust.sdk.AdjustSessionSuccess;
import com.adjust.sdk.OnAttributionChangedListener;
import com.adjust.sdk.OnDeeplinkResponseListener;
import com.adjust.sdk.OnEventTrackingFailedListener;
import com.adjust.sdk.OnEventTrackingSucceededListener;
import com.adjust.sdk.OnSessionTrackingFailedListener;
import com.adjust.sdk.OnSessionTrackingSucceededListener;


public class yt7080 extends Application {
    public static volatile Handler applicationHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        AppContext.init(base);
    }

    private void initialize() {
        applicationHandler = new Handler(AppContext.getInstance().getMainLooper());

        initAdjust();

        //Stetho 초기화
        Stetho.initializeWithDefaults(this);
        Utils.setADID();

        Fabric.with(this, new Crashlytics());

//        initFacebook();
        initAdmob();
        initFcmSubscription();

        ScreenOnReceiver screenOnReceiver = new ScreenOnReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(screenOnReceiver, filter);
    }

    public static Context getContext() {
        return AppContext.getInstance();
    }

    private void initFcmSubscription(){
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FCM_TOPIC);
    }
//    private void initFacebook() {
//        AppEventsLogger.activateApp(this);
//    }
    private void initAdmob(){
        MobileAds.initialize(this,getString(R.string.admob_app_id));
    }
    private void initAdjust() {
        String appToken = "45wumy431xc0";
        String environment = AdjustConfig.ENVIRONMENT_PRODUCTION;
//        String environment = AdjustConfig.ENVIRONMENT_SANDBOX;
        AdjustConfig config = new AdjustConfig(this, appToken, environment);
//        config.setLogLevel(LogLevel.VERBOSE); // enable all logs

        // Set attribution delegate.
        config.setOnAttributionChangedListener(new OnAttributionChangedListener() {
            @Override
            public void onAttributionChanged(AdjustAttribution attribution) {
                Log.d("example", "Attribution callback called!");
                Log.d("example", "Attribution: " + attribution.toString());
            }
        });

        // Set event success tracking delegate.
        config.setOnEventTrackingSucceededListener(new OnEventTrackingSucceededListener() {
            @Override
            public void onFinishedEventTrackingSucceeded(AdjustEventSuccess eventSuccessResponseData) {
                Log.d("example", "Event success callback called!");
                Log.d("example", "Event success data: " + eventSuccessResponseData.toString());
            }
        });

        // Set event failure tracking delegate.
        config.setOnEventTrackingFailedListener(new OnEventTrackingFailedListener() {
            @Override
            public void onFinishedEventTrackingFailed(AdjustEventFailure eventFailureResponseData) {
                Log.d("example", "Event failure callback called!");
                Log.d("example", "Event failure data: " + eventFailureResponseData.toString());
            }
        });

        // Set session success tracking delegate.
        config.setOnSessionTrackingSucceededListener(new OnSessionTrackingSucceededListener() {
            @Override
            public void onFinishedSessionTrackingSucceeded(AdjustSessionSuccess sessionSuccessResponseData) {
                Log.d("example", "Session success callback called!");
                Log.d("example", "Session success data: " + sessionSuccessResponseData.toString());
            }
        });

        // Set session failure tracking delegate.
        config.setOnSessionTrackingFailedListener(new OnSessionTrackingFailedListener() {
            @Override
            public void onFinishedSessionTrackingFailed(AdjustSessionFailure sessionFailureResponseData) {
                Log.d("example", "Session failure callback called!");
                Log.d("example", "Session failure data: " + sessionFailureResponseData.toString());
            }
        });

        // Evaluate deferred deep link to be launched.
        config.setOnDeeplinkResponseListener(new OnDeeplinkResponseListener() {
            @Override
            public boolean launchReceivedDeeplink(Uri deeplink) {
                Log.d("example", "Deferred deep link callback called!");
                Log.d("example", "Deep link URL: " + deeplink);

                return true;
            }
        });

        // Allow to send in the background.
        config.setSendInBackground(true);

        // Remove all session callback parameters.
        Adjust.resetSessionCallbackParameters();

        // Remove all session partner parameters.
        Adjust.resetSessionPartnerParameters();

        Adjust.onCreate(config);

        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());
    }

    private static CompositeDisposable subscription = new CompositeDisposable();
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

    // You can use this class if your app is for Android 4.0 or higher
    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }
    }
}
