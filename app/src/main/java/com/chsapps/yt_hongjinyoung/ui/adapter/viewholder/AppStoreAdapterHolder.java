package com.chsapps.yt_hongjinyoung.ui.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chsapps.yt_hongjinyoung.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppStoreAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = AppStoreAdapterHolder.class.getSimpleName();

    private Context context;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_thumbnail)
    ImageView img_thumbnail;

    private String market_url;

    public interface AppStoreAdapterListener {
        void onClick(String url);
    }

    private AppStoreAdapterListener listener;
    public AppStoreAdapterHolder(Context context, View itemView, AppStoreAdapterListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        ButterKnife.bind(this, itemView);
    }

    public void update(String title, String img_url, String market_url) {
        this.market_url = market_url;
        tv_title.setText(title);
        Glide.with(context).load(img_url).into(img_thumbnail);
    }

    @OnClick({R.id.layer_main})
    public void onClick_btn_play(View view) {
        if(listener != null) {
            listener.onClick(market_url);
        }
    }
}
