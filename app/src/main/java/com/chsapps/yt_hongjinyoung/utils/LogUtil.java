package com.chsapps.yt_hongjinyoung.utils;

import android.util.Log;

import com.chsapps.yt_hongjinyoung.common.BuildConfig;

public class LogUtil {

    public static void v(String tag, String msg) {
        if (BuildConfig.IS_DEBUG_MODE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.IS_DEBUG_MODE) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.IS_DEBUG_MODE) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.IS_DEBUG_MODE) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.IS_DEBUG_MODE) {
            Log.w(tag, msg);
        }
    }
}
