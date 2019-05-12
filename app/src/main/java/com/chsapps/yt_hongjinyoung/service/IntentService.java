package com.chsapps.yt_hongjinyoung.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.ui.activity.SplashActivity;
import com.fingerpush.android.FingerNotification;
import com.fingerpush.android.FingerPushFcmListener;
import com.fingerpush.android.FingerPushManager;

public class IntentService extends FingerPushFcmListener {

    @Override
    public void onMessage(Context context, Bundle data) {
        createNotificationChannel(data);
    }

    private void createNotificationChannel(Bundle data) {
        Intent intent = new Intent(IntentService.this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent.putExtra("msgTag", FingerPushManager.getMessageId(data));
        intent.putExtra("mode", FingerPushManager.getPushMode(data));
        intent.putExtra("lCode", FingerPushManager.getMessageLabel(data));
        intent.putExtra("receive_push",true);

        PendingIntent pi = PendingIntent.getActivity(IntentService.this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        FingerNotification fingerNotification = new FingerNotification(this);
        fingerNotification.setNofiticaionIdentifier((int) System.currentTimeMillis());
        fingerNotification.setIcon(R.mipmap.ic_launcher); // Notification small icon
        fingerNotification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fingerNotification.setColor(Color.rgb(0, 114, 162));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fingerNotification.createChannel("channel_id", "channel_name");
        }
        fingerNotification.setVibrate(new long[]{0, 500, 600, 1000});
        fingerNotification.setLights(Color.parseColor("#ffff00ff"), 500, 500);
        fingerNotification.showNotification(data, pi);
    }

}
