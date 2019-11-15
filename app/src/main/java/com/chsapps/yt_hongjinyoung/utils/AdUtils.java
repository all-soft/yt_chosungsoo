package com.chsapps.yt_hongjinyoung.utils;

//import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
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

    public void showAdMobBannerAd(AdView adMobAdView) {
        //adMobAdView.setAdUnitId(ADConstants.ADMOB_BANNER_AD_ID);
        AdRequest adRequest = AdUtils.getInstance().getAdMobAdRequest();
        adMobAdView.loadAd(adRequest);
    }
    public void showAdMobBannerAd(AdView adMobAdView, String adId) {
        adMobAdView.setAdSize(AdSize.SMART_BANNER);
        adMobAdView.setAdUnitId(adId);
        adMobAdView.loadAd(getAdMobAdRequest());
    }

    public AdRequest getAdMobAdRequest() {
//        Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
//                .setNativeAdChoicesIconExpandable(false)
//                .build();
        AdRequest adRequest = new AdRequest.Builder()
//                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
//                .addTestDevice("086A436107A5322A6AD435A899DADB5A")
                .build();
        return adRequest;
    }
}
