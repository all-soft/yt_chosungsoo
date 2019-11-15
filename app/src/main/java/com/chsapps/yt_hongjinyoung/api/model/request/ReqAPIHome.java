package com.chsapps.yt_hongjinyoung.api.model.request;

import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.utils.Utils;

public class ReqAPIHome {
    private static final String TAG = ReqAPIHome.class.getSimpleName();

    public String lang;
    public int app_id;

    public int type;

    public ReqAPIHome(int type) {
        lang = Utils.getLanguage();
        app_id = Constants.APPID;
        this.type = type;
    }


}
