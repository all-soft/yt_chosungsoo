package com.chsapps.yt_hongjinyoung.service;

import com.allsoft.notification.NotificationUtils;
import com.allsoft.notification.PushData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    // 메시지 수신
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        PushData pushData = NotificationUtils.getPushData(remoteMessage.getData());
        if(pushData != null) {
            NotificationUtils.showNotification(getApplicationContext(), pushData);
        }
    }
}
