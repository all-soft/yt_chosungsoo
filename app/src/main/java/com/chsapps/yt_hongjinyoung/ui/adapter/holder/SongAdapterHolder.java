package com.chsapps.yt_hongjinyoung.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.ui.adapter.listener.SongAdapterHolderListener;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.text.NumberFormat;


public class SongAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = SongAdapterHolder.class.getSimpleName();

    private Context context;
    private SongData songData;
    private SongAdapterHolderListener listener;

    @BindView(R.id.layer_selected)
    View layer_selected;
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
    @BindView(R.id.tv_index)
    TextView tv_index;
    @BindView(R.id.tv_playcount)
    TextView tv_playcount;

    public SongAdapterHolder(Context context, View itemView, SongAdapterHolderListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        ButterKnife.bind(this, itemView);
    }

    public void update(int index, SongData songData, boolean isVisibleActionBtn, boolean isDeleted, boolean isSelected) {
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
        layer_selected.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        tv_index.setText(String.valueOf(index+1));

        if(songData.play_cnt > -1) {
            tv_playcount.setVisibility(View.VISIBLE);
            tv_playcount.setText(NumberFormat.getInstance().format(songData.play_cnt) + " 재생됨.");
        } else {
            tv_playcount.setVisibility(View.GONE);
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
