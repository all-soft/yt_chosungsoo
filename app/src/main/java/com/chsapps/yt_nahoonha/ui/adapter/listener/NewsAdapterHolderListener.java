package com.chsapps.yt_nahoonha.ui.adapter.listener;

import com.chsapps.yt_nahoonha.data.NewsData;

public interface NewsAdapterHolderListener {
    void selected(NewsData news);
    void selectedHearts(NewsData news);
    void selectedComments(NewsData news);
    void selectedShare(NewsData news);
}
