package com.chsapps.yt_hongjinyoung.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.data.SongData;
import com.chsapps.yt_hongjinyoung.ui.adapter.listener.SongAdapterHolderListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = VideoAdapterHolder.class.getSimpleName();

    private Context context;
    private SongData songData;
    private SongAdapterHolderListener listener;

    @BindView(R.id.iv_thumbnail)
    ImageView iv_thumbnail;
    @BindView(R.id.iv_user_thumbnail)
    ImageView iv_user_thumbnail;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_info)
    TextView tv_info;
    @BindView(R.id.btn_save)
    ViewGroup btn_save;

    @BindView(R.id.view_selected)
    View view_selected;

    @BindView(R.id.chb_selected)
    CheckBox chb_selected;

    public VideoAdapterHolder(Context context, View itemView, SongAdapterHolderListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        ButterKnife.bind(this, itemView);
    }

    public void update(SongData songData, boolean isSelected) {
        this.songData = songData;
        Glide.with(context).load(songData.getThumbnail()).into(iv_thumbnail);
        tv_title.setText(songData.getTitle());
        view_selected.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        chb_selected.setChecked(isSelected);
    }

    @OnClick(R.id.layer_main)
    public void onClick_layer_main() {
        if(listener != null) {
            listener.selected(songData);
        }
    }
    @OnClick(R.id.btn_save)
    public void onClick_btn_save() {
        if(listener != null) {
            listener.save(songData);
        }
    }
}
