package com.chsapps.yt_hongjinyoung.utils.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.chsapps.yt_hongjinyoung.app.AllSoft;

import static android.Manifest.permission.SYSTEM_ALERT_WINDOW;

public final class PermissionsUtils {
    public static final String REQ_PERMS[] = {
            SYSTEM_ALERT_WINDOW
    };

    static final PermissionsImpl IMPL;

    interface PermissionsImpl {
        boolean is_SYSTEM_ALERT_WINDOW();
    }

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            IMPL = new MarshmallowPermissionImpl();
        } else {
            IMPL = new DefaultPermissionImpl();
        }
    }


    private static class DefaultPermissionImpl implements PermissionsImpl {
        @Override
        public boolean is_SYSTEM_ALERT_WINDOW() {
            return true;
        }
    }

    private static class MarshmallowPermissionImpl implements PermissionsImpl {
        @Override
        public boolean is_SYSTEM_ALERT_WINDOW() {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return Settings.canDrawOverlays(AllSoft.getContext());
            }
            return true;
        }
    }

    public static boolean is_SYSTEM_ALERT_WINDOW() {
        return IMPL.is_SYSTEM_ALERT_WINDOW();
    }

    public static boolean reqAllPermissions() {
        return is_SYSTEM_ALERT_WINDOW();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void showPermissionPopup(Activity activity) {
        activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 1004);
    }
}
