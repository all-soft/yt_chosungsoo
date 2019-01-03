package com.chsapps.yt_hongjinyoung.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.data.NewsData;
import com.chsapps.yt_hongjinyoung.ui.adapter.listener.NewsAdapterHolderListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = NewsAdapterHolder.class.getSimpleName();

    private Context context;
    private NewsData newsData;
    private NewsAdapterHolderListener listener;

    @BindView(R.id.layer_thumbnail)
    ViewGroup layer_thumbnail;

    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbnail;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_contents)
    TextView tv_contents;
    @BindView(R.id.tv_from)
    TextView tv_from;

    @BindView(R.id.tv_heart_cnt)
    TextView tv_heart_cnt;

    public NewsAdapterHolder(Context context, View itemView, NewsAdapterHolderListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        ButterKnife.bind(this, itemView);
    }

    public void update(NewsData newsData) {
        this.newsData = newsData;

        if(!TextUtils.isEmpty(this.newsData.getImage_url())) {
            layer_thumbnail.setVisibility(View.VISIBLE);
            Glide.with(context).load(newsData.getImage_url()).into(iv_thumbnail);
        } else {
            layer_thumbnail.setVisibility(View.GONE);
        }
        tv_title.setText(Html.fromHtml(this.newsData.getTitle()));
        tv_contents.setText(Html.fromHtml(this.newsData.getDesc()));
        tv_from.setText(Html.fromHtml(this.newsData.getDate()));
    }

    @OnClick(R.id.layer_news)
    public void onClick_layer_news() {
        if(listener != null) {
            listener.selected(newsData);
        }
    }

    @OnClick(R.id.btn_share)
    public void onClick_btn_share() {
        if(listener != null)
            listener.selectedShare(newsData);
    }

}
