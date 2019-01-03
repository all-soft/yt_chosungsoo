package com.chsapps.yt_hongjinyoung.ui.adapter.listener;

import com.chsapps.yt_hongjinyoung.data.NewsData;

public interface NewsAdapterHolderListener {
    void selected(NewsData news);
    void selectedHearts(NewsData news);
    void selectedComments(NewsData news);
    void selectedShare(NewsData news);
}
