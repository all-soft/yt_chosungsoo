package com.allsoft.app_store.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.api.model.response.RecommandAppAPIData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FreeAppStoreAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = FreeAppStoreAdapterHolder.class.getSimpleName();

    private Context context;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_thumbnail)
    ImageView img_thumbnail;
    @BindView(R.id.tv_desc)
    TextView tv_desc;

    private String market_url;

    public interface AppStoreAdapterListener {
        void onClick(String url);
    }

    private AppStoreAdapterListener listener;
    public FreeAppStoreAdapterHolder(Context context, View itemView, AppStoreAdapterListener listener) {
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

    public void update(RecommandAppAPIData.RecommandApp info) {
        this.market_url = info.app_package_name;

        tv_title.setText(info.app_title);
        tv_desc.setText(info.app_description);
        Glide.with(context).load(info.image_url).into(img_thumbnail);
    }

    @OnClick({R.id.layer_main})
    public void onClick_btn_play(View view) {
        if(listener != null) {
            listener.onClick(market_url);
        }
    }
}
