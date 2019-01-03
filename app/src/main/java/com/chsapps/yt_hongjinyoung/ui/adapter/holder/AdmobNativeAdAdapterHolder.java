package com.chsapps.yt_hongjinyoung.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdmobNativeAdAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = AdmobNativeAdAdapterHolder.class.getSimpleName();

    private Context context;

    @BindView(R.id.main_layer)
    UnifiedNativeAdView main_layer;

    @BindView(R.id.media_view)
    MediaView media_view;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_comments)
    TextView tv_comments;

    @BindView(R.id.progress)
    ProgressBar progress;

    public int position;

    public AdmobNativeAdAdapterHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;

        ButterKnife.bind(this, itemView);
    }

    public void update(UnifiedNativeAd unifiedNativeAd, String title, String comments) {

        progress.setVisibility(View.GONE);
        media_view.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.VISIBLE);
        tv_comments.setVisibility(View.VISIBLE);

        main_layer.setBodyView(main_layer);
        main_layer.setMediaView(media_view);
        main_layer.setHeadlineView(tv_title);

        tv_title.setText(title);
        tv_comments.setText(comments);

        main_layer.setNativeAd(unifiedNativeAd);
    }

    public void loadingAd() {
        progress.setVisibility(View.VISIBLE);
        media_view.setVisibility(View.GONE);
        tv_title.setVisibility(View.GONE);
        tv_comments.setVisibility(View.GONE);
    }
}
