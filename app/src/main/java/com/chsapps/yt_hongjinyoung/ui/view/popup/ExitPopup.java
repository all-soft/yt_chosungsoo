package com.chsapps.yt_hongjinyoung.ui.view.popup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.constants.ADConstants;
import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.utils.AdUtils;
import com.chsapps.yt_hongjinyoung.utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import butterknife.BindView;
import butterknife.OnClick;

public class ExitPopup extends BaseDialog {

    private final static String TAG = ExitPopup.class.getSimpleName();

    private Context context;

    private CommonPopupActionListener actionListener;

    @BindView(R.id.layer_exit)
    ViewGroup layer_exit;
    @BindView(R.id.image_share)
    ViewGroup image_share;
    @BindView(R.id.adView)
    FrameLayout adViewLayer;

    @BindView(R.id.layer_review)
    ViewGroup layer_review;

    public interface CommonPopupActionListener {
        void onActionPositiveBtn();
        void onActionNegativeBtn();
    }

    public ExitPopup(Context context) {
        super(context, true, null);
        this.context = context;
        initialize();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public ExitPopup setActionListener(CommonPopupActionListener listener) {
        actionListener = listener;
        return this;
    }

    private void initialize() {
        setContentView(R.layout.view_exit_popup);

        int exitType = Global.getInstance().isFirstExitApp();
        if(exitType == 0) {
            //TODO : 앱 평가 팝업.
            layer_review.setVisibility(View.VISIBLE);
            layer_exit.setVisibility(View.GONE);
            Global.getInstance().setFirstExitApp(1);
        } else if(exitType == 1) {
            //TODO : 친구 곡 소개 추천 팝업
            layer_exit.setVisibility(View.VISIBLE);
            layer_review.setVisibility(View.GONE);
            Global.getInstance().setFirstExitApp(2);

            image_share.setVisibility(View.VISIBLE);
            adViewLayer.setVisibility(View.GONE);
        } else {
            //TODO : 광고 팝업
            loadAd();
            layer_exit.setVisibility(View.VISIBLE);
            layer_review.setVisibility(View.GONE);

            image_share.setVisibility(View.GONE);
            adViewLayer.setVisibility(View.VISIBLE);
        }


//        if(Global.getInstance().isExitPopupShare()) {
//            Global.getInstance().setExitPopupShare(false);
//            layer_exit.setVisibility(View.VISIBLE);
//            layer_review.setVisibility(View.GONE);
//            if(Global.getInstance().isShowExitAd()) {
//                image_share.setVisibility(View.GONE);
//                adViewLayer.setVisibility(View.VISIBLE);
//                loadAd();
//            } else {
//                image_share.setVisibility(View.VISIBLE);
//                adViewLayer.setVisibility(View.GONE);
//            }
//        } else
//        {
//            Global.getInstance().setExitPopupShare(true);
//            layer_exit.setVisibility(View.GONE);
//            layer_review.setVisibility(View.VISIBLE);
//        }
    }


    @OnClick(R.id.btn_negative)
    public void onClick_btn_negative() {
        if(actionListener != null) {
            actionListener.onActionNegativeBtn();
        }
        dismiss();
    }

    @OnClick(R.id.btn_positive)
    public void onClick_btn_positive() {
        if(actionListener != null) {
            actionListener.onActionPositiveBtn();
        }

        dismiss();
    }

    @OnClick(R.id.btn_share)
    public void onClick_btn_share() {
        Utils.showShare(context, Utils.getString(R.string.title_share_application), Utils.getString(R.string.message_share_application));
    }

    private void loadAd() {
        AdLoader adLoader = new AdLoader.Builder(context, ADConstants.ADMOB_NATIVE_AD_ID)
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        try {
                            UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.ad_content, null);

                            TextView headlineView = adView.findViewById(R.id.ad_headline);
                            headlineView.setText(unifiedNativeAd.getHeadline());
                            adView.setHeadlineView(headlineView);

                            MediaView mv = adView.findViewById(R.id.media_view);
                            adView.setMediaView(mv);

                            adView.setNativeAd(unifiedNativeAd);
                            adViewLayer.removeAllViews();
                            adViewLayer.addView(adView);
                        } catch (Exception e) {

                        }
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        adLoader.loadAd(AdUtils.getInstance().getAdMobAdRequest());
    }



    @OnClick(R.id.btn_layer_1_exit)
    public void onClick_btn_layer_1_exit() {
        if(actionListener != null) {
            actionListener.onActionPositiveBtn();
        }

        dismiss();
    }

    @OnClick(R.id.btn_layer_1_review)
    public void onClick_btn_layer_1_review() {
        dismiss();
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MARKET_URL)));
    }

}
