package com.allsoft.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.ui.activity.SplashActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;

import javax.annotation.Nullable;

public class NotificationUtils {

    public static int NOTIFICATION_ID = 133009;

    public static PushData getPushData(Map<String, String> mapPushData) {
        try {
            PushData pushData = new PushData();
            pushData.title = mapPushData.get("title");
            pushData.message = mapPushData.get("message");
            pushData.img_url = mapPushData.get("img_url");

            pushData.is_custom = false;
            try {
                pushData.is_custom = Boolean.parseBoolean(mapPushData.get("is_custom"));
            } catch (Exception e) {
            }
            pushData.custom_type = 0;
            try {
                pushData.custom_type = Integer.parseInt(mapPushData.get("custom_type"));
            } catch (Exception e) {
            }

            pushData.click_type = 1;
            try {
                pushData.click_type = Integer.parseInt(mapPushData.get("click_type"));
            } catch (Exception e) {
            }

            if (pushData.click_type == 1) {
                //일반.
            } else if (pushData.click_type == 2) {
                pushData.page_title = mapPushData.get("page_title");
                pushData.page_url = mapPushData.get("page_url");
            } else if (pushData.click_type == 3) {
                pushData.contents_idx = 1;
                try {
                    pushData.contents_idx = Integer.parseInt(mapPushData.get("contents_idx"));
                } catch (Exception e) {
                }
                pushData.contents_title = mapPushData.get("contents_title");
                pushData.contents_thumbnail = mapPushData.get("contents_thumbnail");
                pushData.contents_video_id = mapPushData.get("contents_video_id");
                pushData.contents_duration = mapPushData.get("contents_duration");
            }
            return pushData;
        } catch (Exception e) {
            return null;
        }
    }

    public static void showNotification(Context context, PushData pushData) {
        if(pushData == null) {
            return;
        }

        if(TextUtils.isEmpty(pushData.img_url)) {
            showNotificationNormal(context, pushData, null);
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(pushData.img_url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @android.support.annotation.Nullable Transition<? super Bitmap> transition) {
                            switch (pushData.custom_type) {
                                case 1:
                                    showNotificationCustomTypeSmall(context, pushData, resource);
                                    break;
                                case 2:
                                    showNotificationCustomTypeLarge(context, pushData, resource);
                                    break;
                                default:
                                    showNotificationNormal(context, pushData, resource);
                                    break;
                            }
                        }
                    });
        }

    }
    public static void showNotificationNormal(Context context, PushData pushData, @Nullable Bitmap bitmapImage) {
        Bundle bundle = new Bundle();
        bundle.putString("push_type", "receive");

        FirebaseAnalytics.getInstance(context).logEvent("push_event", bundle);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    context.getString(R.string.default_notification_channel_id),
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(null);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra("receive_push", true);
        intent.putExtra("push_data", pushData);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                1099
                /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(pushData.title)
                .setContentText(pushData.message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (bitmapImage != null) {
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.bigLargeIcon(bitmapImage);
            bigPictureStyle.bigPicture(bitmapImage);
            notificationBuilder.setStyle(bigPictureStyle);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID /* ID of notification */, notificationBuilder.build());
    }

    public static void showNotificationCustomTypeSmall(Context context, PushData pushData, @Nullable Bitmap bitmapImage) {
        NotificationCustomSmall noti = NotificationCustomSmall.getInstance(context);
        noti.imageBitmap = bitmapImage;
        noti.pushData = pushData;
        noti.update();
    }

    public static void showNotificationCustomTypeLarge(Context context, PushData pushData, @Nullable Bitmap bitmapImage) {
        NotificationCustomLarge noti = new NotificationCustomLarge(context);
        noti.imageBitmap = bitmapImage;
        noti.pushData = pushData;
        noti.update();
    }

    public static void test(Context context) {

        PushData pushData = new PushData();
        pushData.title = "‘송가인 노래듣기’";
        pushData.message = "‘오늘 하루도 행복하게 보내세요~^^’";
        pushData.click_type = 3;
        pushData.custom_type = 0;//2;
        pushData.is_custom = false;
        pushData.img_url = "http://lightstick.storage.googleapis.com/push/test/push_big.png";//https://lightstick.storage.googleapis.com/event/images/banner/banner_89356_191108165901.jpg";

        pushData.page_title = "미스트롯 미국 시애들공연 풀영상!";
        pushData.page_url = "https://www.youtube.com/watch?v=2F01k8f95FU";
        pushData.contents_idx = 427686;
        pushData.contents_thumbnail = "https://i.ytimg.com/vi/57gaEjw1Nds/hqdefault.jpg";
        pushData.contents_duration = "PT24M50S";
        pushData.contents_title = "영상보";
        pushData.contents_video_id = "2F01k8f95FU";
        NotificationUtils.showNotification(context, pushData);
    }

    public static void closeNotification(Context context) {
//        if (Global.getInstance().notificationPlayers == null) {
//            Global.getInstance().notificationPlayers = new NotificationPlayer(context);
//        }
//
//        Global.getInstance().notificationPlayers.removeNotificationPlayer();
    }
}
