package com.chsapps.yt_hongjinyoung.api.model.request;

import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.utils.Utils;

public class ReqAPISongList {
    private static final String TAG = ReqAPISongList.class.getSimpleName();

    public String lang;
    public int appid = Constants.APPID;

    public int category_idx;
    public int max;
    public int page;

    public ReqAPISongList(int category_idx) {
        this.category_idx = category_idx;

        lang = Utils.getLanguage();
        appid = Constants.APPID;
        max = Constants.CATEGORY_MAX;
        page = 0;
    }


}
