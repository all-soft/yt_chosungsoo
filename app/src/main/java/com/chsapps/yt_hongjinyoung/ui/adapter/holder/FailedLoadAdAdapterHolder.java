package com.chsapps.yt_hongjinyoung.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public class FailedLoadAdAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = FailedLoadAdAdapterHolder.class.getSimpleName();

    private Context context;

    public FailedLoadAdAdapterHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
    }
}
