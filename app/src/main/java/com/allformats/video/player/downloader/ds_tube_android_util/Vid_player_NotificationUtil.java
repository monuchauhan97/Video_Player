package com.allformats.video.player.downloader.ds_tube_android_util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;


public class Vid_player_NotificationUtil {
    public static void createNotificationChannel(Context context, String str, int i) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager) context.getSystemService(NotificationManager.class)).createNotificationChannel(new NotificationChannel(str, str, i));
        }
    }
}
