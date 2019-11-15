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
import com.chsapps.yt_hongjinyoung.api.model.request.ReqAPIToken;
import com.chsapps.yt_hongjinyoung.api.model.response.BaseAPIData;
import com.chsapps.yt_hongjinyoung.constants.APIConstants;
import com.chsapps.yt_hongjinyoung.utils.LogUtil;
import com.chsapps.yt_hongjinyoung.utils.NetworkUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TokenAPIService extends Service {
    private static final String TAG = TokenAPIService.class.getSimpleName();

    private static Context context;
    public static String ACTION_TYPE_TOKEN = "com.chsapps.yt_hongjinyoung.token";

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
        try {
            LogUtil.e(TAG, "onStartCommand.!");
            String actionType = intent.getAction();
            LogUtil.e(TAG, "onStartCommand.! : " + actionType);
            if (TextUtils.isEmpty(actionType)) {
                return START_NOT_STICKY;
            }

            if(actionType.equals(ACTION_TYPE_TOKEN)) {
                requestToken();
            }
        } catch (Exception e) {

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
    private void requestToken() {
        if (!NetworkUtils.isNetworkConnected()) {
            stopSelf();
            return;
        }

        ReqAPIToken param = new ReqAPIToken();
        subscription.add(APIUtils.getInstanse().getApiService()
                .request_token(APIConstants.API_KEY, String.valueOf(param.app_type), param.app_ver, param.cc, param.lang, param.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        stopSelf();
                    }
                })
                .retry(3)
                .subscribe(new Consumer<BaseAPIData>() {
                    @Override
                    public void accept(BaseAPIData response) throws Exception {
                        if(response != null) {
                            //TODO : token 값 비었다고 계속 에러남. 일단 그냥 넘어가게 처리.
                            if(response.isSuccess()) {

                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

}