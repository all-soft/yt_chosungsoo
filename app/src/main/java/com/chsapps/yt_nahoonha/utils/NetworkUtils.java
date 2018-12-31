package com.chsapps.yt_nahoonha.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import com.chsapps.yt_nahoonha.app.AllSoft;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static boolean isAirplaneModeOn() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(AllSoft.getContext().getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(AllSoft.getContext().getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public static boolean isNetworkConnected() {
        try {
            final ConnectivityManager cm = (ConnectivityManager) AllSoft.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED && !isAirplaneModeOn())
                return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNetworkConnectedOrConnecting() {
        try {
            final ConnectivityManager cm = (ConnectivityManager) AllSoft.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting() && !isAirplaneModeOn())
                return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
