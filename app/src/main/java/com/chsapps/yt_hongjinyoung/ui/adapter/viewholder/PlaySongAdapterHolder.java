package com.chsapps.yt_hongjinyoung.ui.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.bumptech.glide.Glide;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaySongAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = PlaySongAdapterHolder.class.getSimpleName();

    private Context context;
    private SongData song;
    private SongListHolderListener listener;

    @BindView(R.id.btn_play)
    ViewGroup btn_play;
    @BindView(R.id.chk_select)
    CheckBox chk_select;
    @BindView(R.id.layer_main)
    ViewGroup layer_main;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbnail;
    @BindView(R.id.btn_save)
    ViewGroup btn_save;

    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.tv_duration)
    TextView tv_duration;

    public PlaySongAdapterHolder(Context context, View itemView, SongListHolderListener listener) {
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
        layer_main.setBackgroundColor(isSelected ? context.getResources().getColor(R.color.color_list_selected) : context.getResources().getColor(R.color.White));
        chk_select.setVisibility(View.GONE);
        btn_save.setVisibility(View.VISIBLE);

        tv_count.setText("" + NumberFormat.getInstance().format(song.play_cnt) + " 재생됨");
        tv_duration.setText(song.getDurationTime());
    }

    public void update(SongData song) {
        this.song = song;
        tv_number.setText(String.valueOf(song.song_idx));
        tv_title.setText(song.getTitle());

        Glide.with(context).load(song.getThumbnail()).into(iv_thumbnail);

        layer_main.setBackgroundColor(context.getResources().getColor(R.color.White));
    }

    public void update(int index, SongData song, String videoId) {
        this.song = song;

        btn_save.setVisibility(View.GONE);
        chk_select.setVisibility(View.GONE);
        btn_play.setVisibility(View.VISIBLE);
        tv_number.setText(String.valueOf(index + 1));
        tv_title.setText(song.getTitle());

        if(!TextUtils.isEmpty(videoId) && song.getVideoid().equals(videoId)) {
            layer_main.setBackgroundColor(context.getResources().getColor(R.color.color_list_selected));
        } else {
            layer_main.setBackgroundColor(context.getResources().getColor(R.color.White));
        }

        Glide.with(context).load(song.getThumbnail()).into(iv_thumbnail);

        tv_count.setText("" + NumberFormat.getInstance().format(song.play_cnt) + " 재생됨");
        tv_duration.setText(song.getDurationTime());
    }

    @OnClick(R.id.layer_main)
    public void onClick_btn_play(View view) {
        if(listener != null) {
            listener.playSong(song);
        }
    }

    @OnClick(R.id.btn_save)
    public void onClick_btn_save() {
        if(listener != null) {
            listener.saveSong(song);
        }
    }

}
