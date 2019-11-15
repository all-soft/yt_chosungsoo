package com.chsapps.yt_hongjinyoung.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chsapps.yt_hongjinyoung.api.APIUtils;
import com.chsapps.yt_hongjinyoung.api.model.response.BaseAPIData;
import com.chsapps.yt_hongjinyoung.constants.APIConstants;
import com.chsapps.yt_hongjinyoung.constants.ParamConstants;
import com.chsapps.yt_hongjinyoung.ui.view.player.ConstantStrings;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;
import com.chsapps.yt_hongjinyoung.utils.NetworkUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PlayAPIService extends Service {
    private static final String TAG = PlayAPIService.class.getSimpleName();

    private static Context context;
    public static String ACTION_TYPE_PLAY = "com.chsapps.yt_hongjinyoung.play";

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
            subscription.dispose();
        }
    }

    CompositeDisposable subscription = new CompositeDisposable();
    private void reqPlay(int song_idx) {
        if (!NetworkUtils.isNetworkConnected()) {
            stopSelf();
            return;
        }

        subscription.add(APIUtils.getInstanse().getApiService()
                .request_play_song(APIConstants.API_KEY, ConstantStrings.getCurrentSongAPIIndex())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(3)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        stopSelf();
                    }
                })
                .subscribe(new Consumer<BaseAPIData>() {
                    @Override
                    public void accept(BaseAPIData baseAPIData) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

}