package com.chsapps.yt_nahoonha.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.data.SingersData;
import com.chsapps.yt_nahoonha.ui.adapter.listener.SingerAdapterHolderListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingerAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = SingerAdapterHolder.class.getSimpleName();

    private Context context;
    private SingerAdapterHolderListener listener;
    private SingersData selectedSinger;
    private SingersData singerStartData;
    private SingersData singerEndData;

    @BindView(R.id.btn_singer_thumb_start)
    ViewGroup btn_singer_thumb_start;
    @BindView(R.id.btn_singer_thumb_end)
    ViewGroup btn_singer_thumb_end;

    @BindView(R.id.iv_singer_thumb_start)
    ImageView iv_singer_thumb_start;
    @BindView(R.id.iv_singer_thumb_end)
    ImageView iv_singer_thumb_end;

    @BindView(R.id.tv_singer_start)
    TextView tv_singer_start;
    @BindView(R.id.tv_singer_end)
    TextView tv_singer_end;

    @BindView(R.id.iv_selected_singer_thumb_start)
    ImageView iv_selected_singer_thumb_start;
    @BindView(R.id.iv_selected_singer_thumb_end)
    ImageView iv_selected_singer_thumb_end;

    public SingerAdapterHolder(Context context, View itemView, SingerAdapterHolderListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        ButterKnife.bind(this, itemView);
    }

    public void update(SingersData startData, SingersData endData, SingersData selectedSinger) {
        singerStartData = startData;
        singerEndData = endData;
        this.selectedSinger = selectedSinger;

        if(singerStartData != null) {
            btn_singer_thumb_start.setVisibility(View.VISIBLE);
            tv_singer_start.setText(singerStartData.getCategory_name());
            Glide.with(context).load(singerStartData.getCategory_image_url()).into(iv_singer_thumb_start);
            if(selectedSinger != null && selectedSinger.getCategory_idx() == singerStartData.getCategory_idx()) {
                iv_selected_singer_thumb_start.setVisibility(View.VISIBLE);
            } else {
                iv_selected_singer_thumb_start.setVisibility(View.GONE);
            }
        } else {
            btn_singer_thumb_start.setVisibility(View.INVISIBLE);
            tv_singer_start.setText("");
        }
        if(singerEndData != null) {
            btn_singer_thumb_end.setVisibility(View.VISIBLE);
            tv_singer_end.setText(singerEndData.getCategory_name());
            Glide.with(context).load(singerEndData.getCategory_image_url()).into(iv_singer_thumb_end);
            if(selectedSinger != null && selectedSinger.getCategory_idx() == singerEndData.getCategory_idx()) {
                iv_selected_singer_thumb_end.setVisibility(View.VISIBLE);
            } else {
                iv_selected_singer_thumb_end.setVisibility(View.GONE);
            }
        } else {
            btn_singer_thumb_end.setVisibility(View.INVISIBLE);
            tv_singer_end.setText("");
        }
    }

    @OnClick(R.id.btn_singer_thumb_start)
    public void onClick_btn_singer_thumb_start() {
        if(listener != null && singerStartData != null) {
            listener.selected(singerStartData);
        }
    }
    @OnClick(R.id.btn_singer_thumb_end)
    public void onClick_btn_singer_thumb_end() {
        if(listener != null && singerEndData != null) {
            listener.selected(singerEndData);
        }
    }

}
