package com.chsapps.yt_nahoonha.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.data.NewsReviewData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsReviewAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = NewsReviewAdapterHolder.class.getSimpleName();

    private Context context;

    @BindView(R.id.tv_comments)
    TextView tv_comments;
    @BindView(R.id.tv_create_at)
    TextView tv_create_at;
    @BindView(R.id.tv_like_cnt)
    TextView tv_like_cnt;
    @BindView(R.id.tv_comments_cnt)
    TextView tv_comments_cnt;

    public NewsReviewAdapterHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;

        ButterKnife.bind(this, itemView);
    }

    public void update(NewsReviewData newsReviewData) {
        tv_comments.setText(newsReviewData.getComment());
        tv_create_at.setText(newsReviewData.getCreate_at());
        tv_like_cnt.setText(String.valueOf(newsReviewData.like_cnt));
        tv_comments_cnt.setText(String.valueOf(newsReviewData.comment_cnt));
    }
}
