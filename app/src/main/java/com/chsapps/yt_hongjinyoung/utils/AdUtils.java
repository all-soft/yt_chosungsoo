package com.chsapps.yt_hongjinyoung.utils;

import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.api.model.HomeData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseActivity;
import com.chsapps.yt_hongjinyoung.constants.AdConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdUtils {
    public static final String TAG = AdUtils.class.getSimpleName();

    private static AdUtils instance = null;

    public static AdUtils getInstance() {
        if(instance == null) {
            instance = new AdUtils();
        }
        return instance;
    }

    public static void showInterstitialAd(BaseActivity activity) {
        //Admob mediation.
        HomeData.AD_CONFIG adConfig = Global.getInstance().getAdConfig();
        AdMobAdUtils.showAdMobInterstitialAd(activity, adConfig);
    }

    public static boolean addBannerView(BaseActivity activity, boolean isBannerAdSet, ViewGroup layerAd) {
        if (Global.getInstance().isShowBannerAdInBottom()) {
            if (!isBannerAdSet) {
                HomeData.AD_CONFIG adConfig = null;//Global.getInstance().getAdConfig();
                //Admob mediation.
                AdView adMobAdView = new AdView(activity);
                layerAd.addView(adMobAdView);
//                AdMobAdUtils.showAdMobBannerAd(adMobAdView, adConfig.getBanner_id());
                AdMobAdUtils.showAdMobBannerAd(adMobAdView, AdConstants.AD_ID_BANNER);
                return true;
            }
        }
        return false;
    }

    public AdRequest getAdMobAdRequest() {
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("086A436107A5322A6AD435A899DADB5A")
                .build();
        return adRequest;
    }
}
