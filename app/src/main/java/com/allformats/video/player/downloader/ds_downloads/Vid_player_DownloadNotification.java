package com.allformats.video.player.downloader.ds_downloads;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.ds_tube_android_util.Vid_player_NotificationUtil;

public class Vid_player_DownloadNotification {
    public static final String NOTIFICATION_CHANNEL_ID = "Downloading";
    public static final String NOTIFICATION_GROUP_ID = "DownloadingGroup";
    private NotificationCompat.Builder builder;
    private Context context;
    private int notificationId;
    private NotificationManagerCompat notificationManagerCompat;

    public static int getForegroundNotificationId() {
        return -1;
    }

    public Vid_player_DownloadNotification(Context context, int i) {
        this.builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).setPriority(0);
        this.notificationManagerCompat = NotificationManagerCompat.from(context);
        this.context = context;
        this.notificationId = i;
    }

    public void notifyProgress(String str, String str2, int i, boolean z, Intent intent, int i2) {
        this.builder.setContentTitle(str).setSmallIcon(i2).setContentIntent(getPendingIntent(intent, this.notificationId)).setOnlyAlertOnce(true).setOngoing(true).setProgress(100, i, z);
        if (Build.VERSION.SDK_INT >= 24) {
            this.builder.setGroup(NOTIFICATION_GROUP_ID);
            this.builder.setSubText(str2);
            this.builder.setContentText(null);
        } else {
            this.builder.setContentText(str2);
            this.builder.setSubText(null);
        }
        this.notificationManagerCompat.notify(this.notificationId, this.builder.build());
    }

    public void notify(String str, String str2, String str3, Intent intent, int i) {
        this.builder.setContentTitle(str).setSmallIcon(i).setContentText(str2).setSubText(str3).setOngoing(false).setContentIntent(getPendingIntent(intent, this.notificationId)).setProgress(0, 0, false).setOnlyAlertOnce(true);
        if (Build.VERSION.SDK_INT >= 24) {
            this.builder.setGroup(NOTIFICATION_GROUP_ID);
        }
        this.notificationManagerCompat.notify(this.notificationId, this.builder.build());
    }

    private PendingIntent getPendingIntent(Intent intent, int i) {
        if (intent != null) {
            return PendingIntent.getActivity(this.context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return null;
    }

    public void cancel() {
        this.notificationManagerCompat.cancel(this.notificationId);
    }

    public static void cancelAll(Context context) {
        NotificationManagerCompat.from(context).cancelAll();
    }

    public static Notification getForegroundNotification(Context context, String str) {
        String string = context.getString(R.string.app_name);
        NotificationCompat.Builder priority = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).setSmallIcon(R.drawable.ic_stat_musicdownloader).setPriority(1);
        if (Build.VERSION.SDK_INT >= 24) {
            priority.setSubText(str);
            priority.setContentText(null);
        } else {
            priority.setContentTitle(string);
            priority.setContentText(str);
        }
        return priority.build();
    }

    public static void init(Context context) {
        Vid_player_NotificationUtil.createNotificationChannel(context, NOTIFICATION_CHANNEL_ID, 2);
    }
}
