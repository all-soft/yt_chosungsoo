package com.chsapps.yt_hongjinyoung.utils;

import com.chsapps.yt_hongjinyoung.constants.APIConstants;

public class SecureUtils {
    static {
        try {
            System.loadLibrary("libssl");
        } catch (Exception error) {

        }
    }

    public static String getAesKey(){
        try {
            return aesKey();
        }catch (Exception e){

        }
        return "";
    }

    public static String getAppKey(){
        try {
            return APIConstants.API_KEY;
        }catch (Exception e){

        }
        return "";
    }

    public static String getAppValue(){
        try {
            return APIConstants.API_APPLICATION;
        }catch (Exception e){

        }
        return "";
    }
    private static native String aesKey();
    private static native String appKey();
    private static native String appValue();
}
