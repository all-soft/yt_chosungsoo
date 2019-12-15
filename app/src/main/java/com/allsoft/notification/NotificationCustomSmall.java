package com.allsoft.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.chsapps.yt_hongjinyoung.R;
import com.chsapps.yt_hongjinyoung.ui.activity.SplashActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationCustomSmall {

    private final static String TAG = NotificationCustomSmall.class.getSimpleName();

    private final static String NOTIFICATION_CHANEL_ID = "notification_com.chsapps.classic";

    private Context context;

    private NotificationManager mNotificationManager;
    private CustomNotificationSmallBuilder mNotificationManagerBuilder;
    private boolean isForeground;

    private String channelId;

    public PushData pushData;
    public Bitmap imageBitmap;

    public static NotificationCustomSmall instance;
    public static NotificationCustomSmall getInstance(Context context) {
        if(instance == null) {
            instance = new NotificationCustomSmall(context);
        } else {
            instance.setContext(context);
        }
        return instance;
    }

    public NotificationCustomSmall(Context context) {
        this.context = context;
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANEL_ID,
                    context.getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW);

            mChannel.setDescription(null);
            mNotificationManager.createNotificationChannel(mChannel);
            channelId = mChannel.getId();
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void update() {
        cancel();
        mNotificationManagerBuilder = new CustomNotificationSmallBuilder();
        mNotificationManagerBuilder.execute();
    }

    public void removeNotificationPlayer() {
        NotificationManager nm = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(NotificationUtils.NOTIFICATION_ID);

        isForeground = false;
    }

    public void cancel() {
        if(mNotificationManager != null) {
            mNotificationManager.cancel(NotificationUtils.NOTIFICATION_ID);
        }
        if (mNotificationManagerBuilder != null) {
            mNotificationManagerBuilder.cancel(true);
            mNotificationManagerBuilder = null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class CustomNotificationSmallBuilder extends AsyncTask<Void, Void, Notification> {
        private RemoteViews mRemoteViews;
        private NotificationCompat.Builder mNotificationBuilder;
        private PendingIntent mMainPendingIntent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Intent mainActivity = new Intent(context, SplashActivity.class);
            if(pushData.click_type == 2) {
                mainActivity.putExtra(NotificationConstants.PARAM_EVENT_BANNER_DATA, pushData);
            }
            mMainPendingIntent = PendingIntent.getActivity(context, 0, mainActivity, 0);
            mNotificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANEL_ID);
            mRemoteViews = createRemoteView();
            mNotificationBuilder
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_dialog_info))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContent(mRemoteViews)
                    .setCustomHeadsUpContentView(mRemoteViews)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setChannelId(channelId);

            if (!isForeground) {
                isForeground = true;
            }
        }

        @Override
        protected Notification doInBackground(Void... params) {
            return mNotificationBuilder.build();
        }

        @Override
        protected void onPostExecute(Notification notification) {
            super.onPostExecute(notification);
            try {
                mNotificationManager.notify(NotificationUtils.NOTIFICATION_ID, notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private RemoteViews createRemoteView() {
            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.func_noti_view_custom_noti_small);
            if (imageBitmap != null) {
                contentView.setImageViewBitmap(R.id.iv_image, imageBitmap);
            }

            Intent notificationClick = new Intent(context, CustomNotificationService.class);
            notificationClick.putExtra("push_data", pushData);
            notificationClick.setAction(CustomNotificationService.ACTION_TYPE_SMALL_NOTI);
            PendingIntent notificationServicePendingIntent = PendingIntent.getService(
                    context,
                    1099,
                    notificationClick,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            contentView.setOnClickPendingIntent(R.id.iv_image, notificationServicePendingIntent);
            return contentView;
        }
    }
}
