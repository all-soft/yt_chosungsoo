package com.chsapps.yt_nahoonha.ui.adapter.listener;

import com.chsapps.yt_nahoonha.data.ReviewData;

public interface ReviewAdapterHolderListener {
    void giveLike(ReviewData reviewData);
    void writeComment(ReviewData reviewData);
}
