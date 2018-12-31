package com.chsapps.yt_nahoonha.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chsapps.yt_nahoonha.R;
import com.chsapps.yt_nahoonha.data.ReviewData;
import com.chsapps.yt_nahoonha.ui.adapter.listener.ReviewAdapterHolderListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewAdapterHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ReviewAdapterHolder.class.getSimpleName();

    private Context context;
    private ReviewData reviewData;

    private ReviewAdapterHolderListener listener;

    @BindView(R.id.tv_comments)
    TextView tv_comments;
    @BindView(R.id.tv_create_at)
    TextView tv_create_at;
    @BindView(R.id.tv_like_cnt)
    TextView tv_like_cnt;

    public ReviewAdapterHolder(Context context, View itemView, ReviewAdapterHolderListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;

        ButterKnife.bind(this, itemView);
    }

    public void update(ReviewData reviewData) {
        this.reviewData = reviewData;
    }

    @OnClick(R.id.btn_write_comment)
    public void onClick_btn_write_comment() {
        if(listener != null) {
            listener.writeComment(reviewData);
        }
    }
}
