package com.allsoft.notification;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.chsapps.yt_hongjinyoung.ui.activity.SplashActivity;

public class CustomNotificationService extends Service {
    private static final String TAG = CustomNotificationService.class.getSimpleName();

    public static final String ACTION_TYPE_SMALL_NOTI = "com.allsoft.notification.service.CustomNotificationService.ACTION_TYPE_SMALL_NOTI";
    public static final String ACTION_TYPE_LARGE_NOTI = "com.allsoft.notification.service.CustomNotificationService.ACTION_TYPE_LARGE_NOTI";

    //function.
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            String actionType = intent.getAction();

            if(actionType.equals(ACTION_TYPE_SMALL_NOTI)) {
                process((PushData) intent.getSerializableExtra("push_data"));
            } else if(actionType.equals(ACTION_TYPE_LARGE_NOTI)) {
                process((PushData) intent.getSerializableExtra("push_data"));
            } else {
                stopSelf();
            }
        } catch (Exception e) {
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void processCloseNotification() {
        NotificationUtils.closeNotification(getApplicationContext());
        stopSelf();
    }

    private void process(PushData pushData) {
        NotificationUtils.closeNotification(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.putExtra("push_data", pushData);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);

        NotificationCustomSmall.getInstance(getApplicationContext()).cancel();
        stopSelf();
    }
}