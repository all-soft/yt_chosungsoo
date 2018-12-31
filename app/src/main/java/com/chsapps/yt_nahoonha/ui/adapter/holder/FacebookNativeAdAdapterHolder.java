package com.chsapps.yt_nahoonha.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chsapps.yt_nahoonha.R;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FacebookNativeAdAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = FacebookNativeAdAdapterHolder.class.getSimpleName();

    private Context context;

    @BindView(R.id.main_layer)
    ViewGroup main_layer;

    @BindView(R.id.media_view)
    AdIconView media_view;
    @BindView(R.id.fb_media)
    MediaView fb_media;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_comments)
    TextView tv_comments;

    @BindView(R.id.progress)
    ProgressBar progress;

    public int position;

    public FacebookNativeAdAdapterHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;

        ButterKnife.bind(this, itemView);
    }

    public void update(NativeAd nativeAd, String title, String comments) {

        progress.setVisibility(View.GONE);
        media_view.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.VISIBLE);
        tv_comments.setVisibility(View.VISIBLE);

        nativeAd.registerViewForInteraction(
                main_layer,
                fb_media,
                media_view);

        tv_title.setText(title);
        tv_comments.setText(comments);
    }

    public void loadingAd() {
        progress.setVisibility(View.VISIBLE);
        media_view.setVisibility(View.GONE);
        tv_title.setVisibility(View.GONE);
        tv_comments.setVisibility(View.GONE);
    }
}
