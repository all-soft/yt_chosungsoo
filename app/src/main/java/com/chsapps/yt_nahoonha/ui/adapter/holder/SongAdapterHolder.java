package com.chsapps.yt_nahoonha.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.data.SongData;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SongAdapterHolderListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SongAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = SongAdapterHolder.class.getSimpleName();

    private Context context;
    private SongData songData;
    private SongAdapterHolderListener listener;

    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbnail;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.btn_save)
    ViewGroup btn_save;
    @BindView(R.id.btn_ico_save)
    View btn_ico_save;
    @BindView(R.id.btn_ico_delete)
    View btn_ico_delete;

    public SongAdapterHolder(Context context, View itemView, SongAdapterHolderListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        ButterKnife.bind(this, itemView);
    }

    public void update(SongData songData, boolean isVisibleActionBtn, boolean isDeleted) {
        this.songData = songData;

        btn_save.setVisibility(isVisibleActionBtn ? View.VISIBLE : View.GONE);
        Glide.with(context).load(songData.getThumbnail()).into(iv_thumbnail);
        tv_title.setText(songData.getTitle());
        if(isDeleted) {
            btn_ico_save.setVisibility(View.GONE);
            btn_ico_delete.setVisibility(View.VISIBLE);
        } else {
            btn_ico_save.setVisibility(View.VISIBLE);
            btn_ico_delete.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_save)
    public void onClick_btn_save() {
        if(listener != null) {
            listener.save(songData);
        }
    }

    @OnClick(R.id.view_click)
    public void onClick_view_click() {
        if(listener != null) {
            listener.selected(songData);
        }
    }
}
