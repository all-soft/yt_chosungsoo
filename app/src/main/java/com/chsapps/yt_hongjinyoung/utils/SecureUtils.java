package com.chsapps.yt_hongjinyoung.utils;

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
            return appKey();
        }catch (Exception e){

        }
        return "";
    }

    public static String getAppValue(){
        try {
            return appValue();
        }catch (Exception e){

        }
        return "";
    }
    private static native String aesKey();
    private static native String appKey();
    private static native String appValue();
}
