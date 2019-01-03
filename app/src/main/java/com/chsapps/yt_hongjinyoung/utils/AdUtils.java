package com.chsapps.yt_hongjinyoung.utils;

import android.view.ViewGroup;

import com.chsapps.yt_hongjinyoung.api.model.HomeData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseActivity;

public class AdUtils {
    public static final String TAG = AdUtils.class.getSimpleName();

    public static void showInterstitialAd(BaseActivity activity) {

        //Admob mediation.
        HomeData.AD_CONFIG adConfig = Global.getInstance().getAdConfig();
        AdMobAdUtils.showAdMobInterstitialAd(activity, adConfig);

//        if (adConfig.getAd_fullad_type().equals("2")) {
//            //Show Facebook Ad.
//            FacebookAdUtils.showFacebookInterstitialAd(activity, adConfig);
//        } else {
//            //Show Admob Ad.
//            AdMobAdUtils.showAdMobInterstitialAd(activity, adConfig);
//        }
    }

    public static boolean addBannerView(BaseActivity activity, boolean isBannerAdSet, ViewGroup layerAd) {
        if (Global.getInstance().isShowBannerAdInBottom()) {
            if (!isBannerAdSet) {
                HomeData.AD_CONFIG adConfig = Global.getInstance().getAdConfig();
                //Admob mediation.
                com.google.android.gms.ads.AdView adMobAdView = new com.google.android.gms.ads.AdView(activity);
                layerAd.addView(adMobAdView);
                AdMobAdUtils.showAdMobBannerAd(adMobAdView, adConfig.getBanner_id());

//                if (adConfig.getAd_banner_type().equals("2")) {
//                    //Show Facebook Ad.
//                    com.facebook.ads.AdView fbAdView = new com.facebook.ads.AdView(activity, adConfig.getBanner_id(), AdSize.BANNER_HEIGHT_50);
//                    layerAd.addView(fbAdView);
//                    fbAdView.loadAd();
//                } else {
//                    //Show Admob Ad.
//                    com.google.android.gms.ads.AdView adMobAdView = new com.google.android.gms.ads.AdView(activity);
//                    layerAd.addView(adMobAdView);
//                    AdMobAdUtils.showAdMobBannerAd(adMobAdView, adConfig.getBanner_id());
//                }
                return true;
            }
        }
        return false;
    }
}
