package com.chsapps.yt_hongjinyoung.utils;

import android.os.Bundle;

import com.chsapps.yt_hongjinyoung.api.model.HomeData;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.common.BaseActivity;
import com.chsapps.yt_hongjinyoung.constants.AdConstants;
import com.chsapps.yt_hongjinyoung.ui.view.popup.LoadingAdPopup;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AdMobAdUtils {
    public static final String TAG = AdMobAdUtils.class.getSimpleName();

    private static boolean isShowLoadingDlg = true;
    public static void showAdMobInterstitialAd(BaseActivity activity, HomeData.AD_CONFIG adConfig) {
        final LoadingAdPopup dlg = new LoadingAdPopup(activity);
        if(isShowLoadingDlg) {
            dlg.show();
            isShowLoadingDlg = false;
        }

        final InterstitialAd am_interstitialAd = new InterstitialAd(activity);
        am_interstitialAd.setAdUnitId(AdConstants.AD_ID_INTERSTITIAL);
        Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
                .setNativeAdChoicesIconExpandable(false)
                .build();
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                .addTestDevice("086A436107A5322A6AD435A899DADB5A")
                .build();
        am_interstitialAd.loadAd(adRequest);
        am_interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if(dlg.isShowing())
                    dlg.dismiss();
                am_interstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if(dlg.isShowing())
                    dlg.dismiss();
                Global.getInstance().isMainActivityAdShow = false;
            }
        });

    }

    public static void showAdMobBannerAd(AdView adMobAdView, String adId) {
//        adMobAdView.setAdSize(AdSize.BANNER);
//        adMobAdView.setAdUnitId(AdConstants.AD_ID_BANNER);
//        Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
//                .setNativeAdChoicesIconExpandable(false)
//                .build();
//        AdRequest adRequest = new AdRequest.Builder()
//                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
//                .addTestDevice("086A436107A5322A6AD435A899DADB5A")
//                .build();
//        adMobAdView.loadAd(adRequest);
    }
}
