package com.chsapps.yt_hongjinyoung.ui.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chsapps.yt_hongjinyoung.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KeywordAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = KeywordAdapterHolder.class.getSimpleName();

    private Context context;
    private String keyword;
    private KeywordListHolderListener listener;

    @BindView(R.id.tv_title)
    TextView tv_title;

    public KeywordAdapterHolder(Context context, View itemView, KeywordListHolderListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        ButterKnife.bind(this, itemView);
    }

    public void update(String keyword) {
        this.keyword = keyword;
        tv_title.setText(keyword);
    }

    @OnClick(R.id.btn_delete)
    public void onClick_btn_delete() {
        if(listener != null) {
            listener.deleteKeyword(keyword);
        }
    }
    @OnClick(R.id.main_view)
    public void onClick_main_view() {
        if(listener != null) {
            listener.search(keyword);
        }
    }

}
