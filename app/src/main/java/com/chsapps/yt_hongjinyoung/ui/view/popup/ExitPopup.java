package com.chsapps.yt_hongjinyoung.ui.view.popup;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.app.Global;
import com.chsapps.yt_hongjinyoung.utils.Utils;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
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

    @BindView(R.id.image_share)
    ImageView image_share;
    @BindView(R.id.adView)
    FrameLayout adViewLayer;

    @BindView(R.id.layer_none_ad_content)
    ViewGroup layer_none_ad_content;
    @BindView(R.id.layer_ad_content)
    ViewGroup layer_ad_content;

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
        if(false) {//Global.getInstance().isShowExitAd()) {
            image_share.setVisibility(View.GONE);
            adViewLayer.setVisibility(View.VISIBLE);
            loadAd();
        } else {
            layer_ad_content.setVisibility(View.GONE);
            layer_none_ad_content.setVisibility(View.VISIBLE);

            if(Global.staticSingersData != null) {
                Global.staticSingersData.Log();
                Glide.with(context).load(Global.staticSingersData.getCategory_share_url()).into(image_share);
            } else {
                image_share.setBackgroundResource(R.drawable.img_add);
            }
        }
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

    @OnClick(R.id.btn_exit)
    public void onClick_btn_exit() {
        if(actionListener != null) {
            actionListener.onActionPositiveBtn();
        }

        dismiss();
    }

    @OnClick(R.id.btn_share)
    public void onClick_btn_share() {
        Utils.showShare(context, Utils.getString(R.string.Share), Utils.getString(R.string.message_share_application));
    }

    private void loadAd() {
        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-5539000902582500/5397608635")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        try {
                            UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.view_ad_content, null);

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
        Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
                .setNativeAdChoicesIconExpandable(false)
                .build();
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                .addTestDevice("086A436107A5322A6AD435A899DADB5A")
                .build();
        adLoader.loadAd(adRequest);
    }
}
