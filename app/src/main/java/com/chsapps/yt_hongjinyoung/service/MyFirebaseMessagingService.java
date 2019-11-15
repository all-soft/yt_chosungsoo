package com.chsapps.yt_hongjinyoung.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.ui.activity.MainActivity;
import com.chsapps.yt_hongjinyoung.ui.glide.GlideApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        Log.v("DEBUG900","토큰 :"+s);
    }

    // 메시지 수신
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = "";
        String message = "";
        String imgUrl = "";
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if(notification !=null){
            title = notification.getTitle();
            message = notification.getBody();
            if(notification.getImageUrl() != null){
                imgUrl = notification.getImageUrl().toString();
            }
        }else{
            Map<String, String> data = remoteMessage.getData();
            title = data.get("title");
            message = data.get("content");
            imgUrl = data.get("img_url");
        }
        sendNotification(title, message,imgUrl);
    }

    private void sendNotification(String title, String message,String imgUrl) {
        if(TextUtils.isEmpty(imgUrl)){
            sendNotifiCation(title,message,null);
        }else{
            GlideApp.with(this)
                    .asBitmap()
                    .load(imgUrl)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            sendNotifiCation(title,message,resource);
                        }
                    });
        }
    }

    private void sendNotifiCation(String title,String message,@Nullable Bitmap imgBitMap){
        Bundle bundle = new Bundle();
        bundle.putString("push_type","receive");
        FirebaseAnalytics.getInstance(this).logEvent("push_event",bundle);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(null);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("receive_push",true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 231344 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,getString(R.string.default_notification_channel_id))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_info))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if(imgBitMap != null){
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.bigLargeIcon(imgBitMap);
            bigPictureStyle.bigPicture(imgBitMap);
            notificationBuilder.setStyle(bigPictureStyle);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(10006 /* ID of notification */, notificationBuilder.build());
    }
}
