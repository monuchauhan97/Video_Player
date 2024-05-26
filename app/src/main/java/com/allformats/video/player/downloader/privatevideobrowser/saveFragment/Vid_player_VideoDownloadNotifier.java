package com.allformats.video.player.downloader.privatevideobrowser.saveFragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.format.Formatter;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.R;

import java.io.File;


public class Vid_player_VideoDownloadNotifier {
    private Intent downloadServiceIntent;
    private DownloadingRunnable downloadingRunnable;
    private Handler handler;
    private boolean sound;
    private boolean vibrate;
    private NotificationManager notificationManager = (NotificationManager) Vid_player_DS_Helper.getInstance().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


    public Vid_player_VideoDownloadNotifier(Intent intent) {
        SharedPreferences sharedPreferences = Vid_player_DS_Helper.getInstance().getSharedPreferences("activity_ds_browse_settings", 0);
        this.sound = sharedPreferences.getBoolean(Vid_player_DS_Helper.getInstance().getApplicationContext().getString(R.string.soundON), true);
        this.vibrate = sharedPreferences.getBoolean(Vid_player_DS_Helper.getInstance().getApplicationContext().getString(R.string.vibrateON), true);
        this.downloadServiceIntent = intent;
        if (Build.VERSION.SDK_INT >= 26) {
            this.notificationManager.createNotificationChannel(new NotificationChannel("download_01", "Download Notification", NotificationManager.IMPORTANCE_LOW));
            boolean z = this.sound;
            if (z && this.vibrate) {
                NotificationChannel notificationChannel = new NotificationChannel("download_02", "Download Notification", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableVibration(true);
                this.notificationManager.createNotificationChannel(notificationChannel);
            } else if (z && !this.vibrate) {
                NotificationChannel notificationChannel2 = new NotificationChannel("download_03", "Download Notification", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel2.enableVibration(false);
                this.notificationManager.createNotificationChannel(notificationChannel2);
            } else if (!z && this.vibrate) {
                NotificationChannel notificationChannel3 = new NotificationChannel("download_04", "Download Notification", NotificationManager.IMPORTANCE_LOW);
                notificationChannel3.enableVibration(true);
                this.notificationManager.createNotificationChannel(notificationChannel3);
            } else if (!z && !this.vibrate) {
                NotificationChannel notificationChannel4 = new NotificationChannel("download_05", "Download Notification", NotificationManager.IMPORTANCE_LOW);
                notificationChannel4.enableVibration(false);
                this.notificationManager.createNotificationChannel(notificationChannel4);
            }
        }
        HandlerThread handlerThread = new HandlerThread("downloadNotificationThread");
        handlerThread.start();
        this.handler = new Handler(handlerThread.getLooper());
    }

    public void notifyDownloading() {
        DownloadingRunnable downloadingRunnable = new DownloadingRunnable();
        this.downloadingRunnable = downloadingRunnable;
        downloadingRunnable.run();
    }

    public void notifyDownloadFinished() {
        this.handler.removeCallbacks(this.downloadingRunnable);
        this.notificationManager.cancel(77777);
        String str = this.downloadServiceIntent.getStringExtra("name") + "." + this.downloadServiceIntent.getStringExtra("type");
        if (Build.VERSION.SDK_INT >= 26) {
            boolean z = this.sound;
            String str2 = "download_02";
            if (!z || !this.vibrate) {
                if (z && !this.vibrate) {
                    str2 = "download_03";
                } else if (!z && this.vibrate) {
                    str2 = "download_04";
                } else if (!z && !this.vibrate) {
                    str2 = "download_05";
                }
            }
            this.notificationManager.notify(8888, new Notification.Builder(Vid_player_DS_Helper.getInstance().getApplicationContext(), str2).setTimeoutAfter(1500L).setContentTitle("Download Finished").setContentText(str).setAutoCancel(true).setSmallIcon(R.drawable.splash_icon).setLargeIcon(BitmapFactory.decodeResource(Vid_player_DS_Helper.getInstance().getApplicationContext().getResources(), R.drawable.splash_icon)).build());
            return;
        }
        Notification.Builder largeIcon = new Notification.Builder(Vid_player_DS_Helper.getInstance().getApplicationContext()).setTicker("Download Finished").setPriority(Notification.PRIORITY_HIGH).setSound(RingtoneManager.getDefaultUri(2)).setSmallIcon(R.drawable.splash_icon).setAutoCancel(true).setLargeIcon(BitmapFactory.decodeResource(Vid_player_DS_Helper.getInstance().getApplicationContext().getResources(), R.drawable.splash_icon));
        if (this.sound) {
            largeIcon.setSound(RingtoneManager.getDefaultUri(2));
        } else {
            largeIcon.setSound(null);
        }
        if (this.vibrate) {
            largeIcon.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        } else {
            largeIcon.setVibrate(new long[]{0, 0, 0, 0, 0});
        }
        this.notificationManager.notify(8888, largeIcon.build());
        this.notificationManager.cancel(8888);
    }

    public void cancel() {
        DownloadingRunnable downloadingRunnable = this.downloadingRunnable;
        if (downloadingRunnable != null) {
            this.handler.removeCallbacks(downloadingRunnable);
        }
        this.notificationManager.cancel(77777);
    }

    public class DownloadingRunnable implements Runnable {
        private DownloadingRunnable() {
        }

        @Override
        public void run() {
            Notification.Builder builder;
            String str = Vid_player_VideoDownloadNotifier.this.downloadServiceIntent.getStringExtra("name") + "." + Vid_player_VideoDownloadNotifier.this.downloadServiceIntent.getStringExtra("type");
            if (Build.VERSION.SDK_INT >= 26) {
                builder = new Notification.Builder(Vid_player_DS_Helper.getInstance().getApplicationContext(), "download_01").setStyle(new Notification.BigTextStyle());
            } else {
                builder = new Notification.Builder(Vid_player_DS_Helper.getInstance().getApplicationContext());
            }
            builder.setContentTitle("Downloading " + str).setSmallIcon(R.drawable.splash_icon).setAutoCancel(true).setLargeIcon(BitmapFactory.decodeResource(Vid_player_DS_Helper.getInstance().getApplicationContext().getResources(), R.drawable.splash_icon)).setOngoing(true);
            if (Vid_player_VideoDownloadNotifier.this.downloadServiceIntent.getBooleanExtra("chunked", false)) {
                File file = new File(Vid_player_DS_Helper.PATH_VIDEO_DOWNLOADER + "/" + str);
                builder.setProgress(100, 0, true).setContentText(file.exists() ? Formatter.formatFileSize(Vid_player_DS_Helper.getInstance().getApplicationContext(), file.length()) : "0KB");
                Vid_player_VideoDownloadNotifier.this.notificationManager.notify(77777, builder.build());
                Vid_player_VideoDownloadNotifier.this.handler.postDelayed(this, 1000L);
                return;
            }
            File file2 = new File(Vid_player_DS_Helper.PATH_VIDEO_DOWNLOADER + "/" + str);
            String stringExtra = Vid_player_VideoDownloadNotifier.this.downloadServiceIntent.getStringExtra("size");
            double length = (double) file2.length();
            double parseLong = (double) Long.parseLong(stringExtra);
            Double.isNaN(length);
            Double.isNaN(parseLong);
            int ceil = (int) Math.ceil((length / parseLong) * 100.0d);
            if (ceil >= 100) {
                ceil = 100;
            }
            String formatFileSize = Formatter.formatFileSize(Vid_player_DS_Helper.getInstance().getApplicationContext(), file2.length());
            String formatFileSize2 = Formatter.formatFileSize(Vid_player_DS_Helper.getInstance().getApplicationContext(), Long.parseLong(stringExtra));
            builder.setProgress(100, ceil, false).setAutoCancel(true).setContentText(formatFileSize + "/" + formatFileSize2 + "   " + ceil + "%");
            Vid_player_VideoDownloadNotifier.this.notificationManager.notify(77777, builder.build());
            Vid_player_VideoDownloadNotifier.this.handler.postDelayed(this, 1000L);
        }
    }
}
