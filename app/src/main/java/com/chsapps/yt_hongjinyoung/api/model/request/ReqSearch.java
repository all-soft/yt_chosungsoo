package com.chsapps.yt_hongjinyoung.api.model.request;

import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.utils.Utils;

public class ReqSearch {
    private static final String TAG = ReqSearch.class.getSimpleName();

    public String lang;
    public int appid;
    public String keyword;
    public int max;
    public int page;

    public ReqSearch(String keyword) {
        lang = Utils.getLanguage();
        appid = Constants.APPID;
        max = Constants.CATEGORY_MAX;
        page = 0;
        this.keyword = keyword;
    }


}
