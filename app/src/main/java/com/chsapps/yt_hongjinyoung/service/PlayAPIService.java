package com.chsapps.yt_hongjinyoung.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.ui.youtube_player.ConstantStrings;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;
import com.chsapps.yt_hongjinyoung.utils.NetworkUtils;

import rx.subscriptions.CompositeSubscription;

public class PlayAPIService extends Service {
    private static final String TAG = PlayAPIService.class.getSimpleName();

    private static Context context;
    public static String ACTION_TYPE_PLAY = "com.chsapps.yt7080.play";

    //function.
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate() {
        LogUtil.e(TAG, "onCreate.!");
        context = this.getApplicationContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e(TAG, "onStartCommand.!");
        String actionType = intent.getAction();
        LogUtil.e(TAG, "onStartCommand.! : " + actionType);
        if (TextUtils.isEmpty(actionType)) {
            return START_NOT_STICKY;
        }

        if(actionType.equals(ACTION_TYPE_PLAY)) {
            reqPlay(intent.getIntExtra(ParamConstants.PARAM_PLAY_SONG_IDX, ConstantStrings.getCurrentSongAPIIndex()));
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(subscription != null) {
            subscription.unsubscribe();
        }
    }

    CompositeSubscription subscription = new CompositeSubscription();
    private void reqPlay(int song_idx) {
        if (!NetworkUtils.isNetworkConnected()) {
            stopSelf();
            return;
        }
    }

}