package com.chsapps.yt_hongjinyoung.constants;

import com.chsapps.yt_hongjinyoung.BuildConfig;

public class APIConstants {

    public static final int RESPONSE_SUCCESS = 200;
    public static final int RESPONSE_ANOTHER_SUCCESS = 201;
    public static final int RESPONSE_UNKNOWN_FAIL = 400;
    public static final int RESPONSE_NO_TOKEN_FAIL = 401;
    public static final int RESPONSE_NO_UUID_FAIL = 402;
    public static final int RESPONSE_NO_APP_TYPE_FAIL = 403;
    public static final int RESPONSE_NO_APP_VER_FAIL = 404;
    public static final int RESPONSE_NO_LANG_FAIL = 405;
    public static final int RESPONSE_NO_CC_FAIL = 406;


    public static final int SERVER_CONNTECT_TIME = 10000;
    public static final String API_SERVER_URL = BuildConfig.API_BASE_URL;

    public static final int LIST_MINIMUM_REQ_ITEM_CNT = 10;
}