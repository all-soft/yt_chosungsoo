package com.chsapps.yt_hongjinyoung.api.model.request;

import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.utils.Utils;

public class ReqSongList {
    private static final String TAG = ReqSongList.class.getSimpleName();

    public String lang;
    public int appid;
    public int max;
    public int page;

    public ReqSongList() {
        lang = Utils.getLanguage();
        appid = Constants.APPID;
        max = Constants.CATEGORY_MAX;
        page = 0;
    }


}
