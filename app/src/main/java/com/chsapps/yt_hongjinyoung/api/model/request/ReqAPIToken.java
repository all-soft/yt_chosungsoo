package com.chsapps.yt_hongjinyoung.api.model.request;

import com.chsapps.yt_hongjinyoung.constants.Constants;
import com.chsapps.yt_hongjinyoung.utils.Utils;

public class ReqAPIToken {
    private static final String TAG = ReqAPIToken.class.getSimpleName();

    public String token;
    public String uuid;
    public int app_type;
    public String app_ver;
    public String lang;
    public String cc;

    public ReqAPIToken() {
        token = Utils.getFirebaseToken();
        app_type = Constants.APPID;
        app_ver = Utils.getAppVersion();
        lang = Utils.getLanguage();
        cc = Utils.getCC();
        uuid = Utils.getADID();
    }


}
