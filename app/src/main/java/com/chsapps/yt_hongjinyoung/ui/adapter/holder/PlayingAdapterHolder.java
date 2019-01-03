package com.chsapps.yt_hongjinyoung.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.ui.adapter.listener.SongAdapterHolderListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayingAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = PlayingAdapterHolder.class.getSimpleName();

    private Context context;
    private SongData songData;
    private SongAdapterHolderListener listener;

    @BindView(R.id.layer_bg)
    ViewGroup layer_bg;
    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbnail;
    @BindView(R.id.tv_title)
    TextView tv_title;

    public PlayingAdapterHolder(Context context, View itemView, SongAdapterHolderListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        ButterKnife.bind(this, itemView);
    }

    public void update(SongData songData, String playingVideoId) {
        this.songData = songData;
        Glide.with(context).load(songData.getThumbnail()).into(iv_thumbnail);
        tv_title.setText(songData.getTitle());

        if(!TextUtils.isEmpty(playingVideoId) && songData.getRealVideoId().equals(playingVideoId)) {
            layer_bg.setBackgroundColor(context.getResources().getColor(R.color.color_e9e9e9));
        } else {
            layer_bg.setBackgroundColor(context.getResources().getColor(R.color.White));
        }
    }

    @OnClick(R.id.view_click)
    public void onClick_view_click() {
        if(listener != null) {
            listener.selected(songData);
        }
    }
}
