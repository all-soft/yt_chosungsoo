package com.chsapps.yt_hongjinyoung.utils.permission;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;

import com.chsapps.yt_hongjinyoung.app.yt7080;

import static android.Manifest.permission.SYSTEM_ALERT_WINDOW;

public final class PermissionsUtils {
    public static final String REQ_PERMS[] = {
            SYSTEM_ALERT_WINDOW,
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return Settings.canDrawOverlays(yt7080.getContext());
            }
            return true;
        }
    }

    public static boolean is_SYSTEM_ALERT_WINDOW() {
        return IMPL.is_SYSTEM_ALERT_WINDOW();
    }

    public static boolean reqAllPermissions(Activity act, int reqCode) {
        if (!is_SYSTEM_ALERT_WINDOW()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                act.requestPermissions(
                        REQ_PERMS,
                        reqCode);
            }
            return false;
        }
        return true;
    }

    public static void reqPermissions(Activity act, String[] permissions, int reqCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            act.requestPermissions(permissions, reqCode);
        }
    }
}
