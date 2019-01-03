package com.chsapps.yt_hongjinyoung.utils;

import com.chsapps.yt_hongjinyoung.api.model.HomeData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseActivity;
import com.chsapps.yt_hongjinyoung.ui.view.popup.LoadingAdPopup;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class FacebookAdUtils {
    public static final String TAG = FacebookAdUtils.class.getSimpleName();

    public static InterstitialAd initFacebookInterstitialAd(BaseActivity activity) {
        return new InterstitialAd(activity, "");
    }

    public static InterstitialAd initFacebookInterstitialAd(BaseActivity activity, String adId) {
        return new InterstitialAd(activity, adId);
    }

    public static void showFacebookInterstitialAd(BaseActivity activity, InterstitialAd interstitialAd, InterstitialAdListener listener) {
        if(interstitialAd == null) {
            interstitialAd = initFacebookInterstitialAd(activity);
        }
        interstitialAd.setAdListener(listener);

        interstitialAd.loadAd();
    }

    public static void showFacebookInterstitialAd(BaseActivity activity, HomeData.AD_CONFIG adConfig) {
        final InterstitialAd fb_interstitialAd = initFacebookInterstitialAd(activity, adConfig.getFullad_id());
        final LoadingAdPopup dlg = new LoadingAdPopup(activity);
        dlg.show();

        showFacebookInterstitialAd(activity, fb_interstitialAd, new InterstitialAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                dlg.dismiss();
                Global.getInstance().isMainActivityAdShow = false;
                LogUtil.e(TAG, "facebook ad fail : " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                CompositeDisposable subscription = new CompositeDisposable();
                subscription.add(Observable.empty()
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) {

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                dlg.dismiss();
                                if (fb_interstitialAd != null) {
                                    fb_interstitialAd.show();
                                }
                            }
                        }));
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
            }
        });
    }
}
