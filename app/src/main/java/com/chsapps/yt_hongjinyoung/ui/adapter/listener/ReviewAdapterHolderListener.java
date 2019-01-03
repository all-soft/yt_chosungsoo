package com.chsapps.yt_hongjinyoung.ui.adapter.listener;

import com.chsapps.yt_hongjinyoung.data.ReviewData;

public interface ReviewAdapterHolderListener {
    void giveLike(ReviewData reviewData);
    void writeComment(ReviewData reviewData);
}
