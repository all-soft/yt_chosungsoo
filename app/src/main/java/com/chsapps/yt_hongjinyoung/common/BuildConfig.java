package com.chsapps.yt_hongjinyoung.common;

public class BuildConfig {
    private final static String TAG = BuildConfig.class.getSimpleName();

    public final static boolean IS_DEBUG_MODE = false;

    /*
    * 고정 가수를 할 시
    *
    * IS_FIXED_SINGER -> "true" 로 변경
    * index / name / share url 세가지를 입력.
    * */
    public static boolean IS_FIXED_SINGER = true;
    public final static int FLAG_SINGER_INFO_INDEX = 43;    // 홍진영
    public final static String FLAG_SINGER_INFO_NAME = "홍진영";
    public final static String FLAG_SINGER_INFO_SHARE_URL = "https://storage.googleapis.com/lightstick/alltrot/43_end.jpg";
    public final static String FLAG_SINGER_INFO_VIDEO_KEYWORD = "홍진영 노래";
    public final static String FLAG_SINGER_INFO_NEWS_KEYWORD = "홍진영 콘서트";
}
