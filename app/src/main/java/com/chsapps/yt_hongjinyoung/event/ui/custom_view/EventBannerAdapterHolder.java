package com.chsapps.yt_hongjinyoung.event.ui.custom_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chsapps.yt_hongjinyoung.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventBannerAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = EventBannerAdapterHolder.class.getSimpleName();

    private Context context;

    private int type;
    @BindView(R.id.event_banner_viewer)
    EventBannerViewer event_banner_viewer;

    public EventBannerAdapterHolder(Context context, View itemView, int type) {
        super(itemView);
        this.context = context;
        this.type = type;
        ButterKnife.bind(this, itemView);

        event_banner_viewer.setLayoutType(type);
    }

}
