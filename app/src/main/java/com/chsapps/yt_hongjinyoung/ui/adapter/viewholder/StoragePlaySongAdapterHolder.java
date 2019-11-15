package com.chsapps.yt_hongjinyoung.ui.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoragePlaySongAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = StoragePlaySongAdapterHolder.class.getSimpleName();

    private Context context;
    private SongData song;
    private SongListHolderListener listener;

    @BindView(R.id.layer_bg)
    ViewGroup layer_bg;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbnail;
    @BindView(R.id.chk_select)
    CheckBox chk_select;

    public StoragePlaySongAdapterHolder(Context context, View itemView, SongListHolderListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        ButterKnife.bind(this, itemView);
    }

    public void update(int index, boolean isSelected, SongData song) {
        this.song = song;
        tv_number.setText(String.valueOf(index + 1));
        tv_title.setText(song.getTitle());

        Glide.with(context).load(song.getThumbnail()).into(iv_thumbnail);
        chk_select.setVisibility(View.GONE);

        layer_bg.setBackgroundColor(isSelected ? context.getResources().getColor(R.color.color_list_selected) : context.getResources().getColor(R.color.White));
    }

    public void update(SongData song) {
        this.song = song;
        tv_number.setText(String.valueOf(song.song_idx));
        tv_title.setText(song.getTitle());

        Glide.with(context).load(song.getThumbnail()).into(iv_thumbnail);
    }

    @OnClick({R.id.layer_main})
    public void onClick_btn_play(View view) {
        if(listener != null) {
            listener.playSong(song);
        }
    }

    @OnClick(R.id.btn_delete)
    public void onClick_btn_delete() {
        if(listener != null) {
            listener.deleteSong(song);
        }
    }

    @OnClick(R.id.btn_share)
    public void onClick_btn_share() {
        if(listener != null) {
            listener.shareSong(song);
        }
    }

}
