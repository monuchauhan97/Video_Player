package com.allformats.video.player.downloader.ds_downloads;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.ds_tube_android_util.classes.Vid_player_DelayTask;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_AudioItem;

public class Vid_player_DownloadService extends Service {
    public static final String ACTION_CANCEL_DOWNLOAD = "action_cancel_download";
    public static final String ACTION_DOWNLOAD = "action_download";
    public static final String DS_ACTION = "ds_action";
    public static final String DS_AUDIO_ID = "ds_url";
    public static final String DS_DEST_DIR = "ds_dest_dir";
    public static final String DS_DOWNLOAD_ID = "ds_download_id";
    public static final String DS_DOWNLOAD_SOURCE = "ds_download_source";
    public static final String DS_TITLE = "ds_filename";
    private static final String WAKELOCK_TAG = "MusicDownloader:Vid_player_DownloadService";
    public Vid_player_DownloadManager vidplayerDownloadManager;
    public Vid_player_DelayTask stopTimer;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Vid_player_DownloadNotification.init(this);
        startForeground(Vid_player_DownloadNotification.getForegroundNotificationId(), Vid_player_DownloadNotification.getForegroundNotification(this, getString(R.string.downloading)));
        this.vidplayerDownloadManager = new Vid_player_DownloadManager(this);
        this.stopTimer = stopTimer();
        this.vidplayerDownloadManager.setOnDownloadsFinishListener(new Vid_player_DownloadManager.OnDownloadsFinishListener() {
            @Override
            public void onDownloadsFinish() {
                Vid_player_DownloadService vidplayerDownloadService = Vid_player_DownloadService.this;
                int foregroundNotificationId = Vid_player_DownloadNotification.getForegroundNotificationId();
                Vid_player_DownloadService vidplayerDownloadService2 = Vid_player_DownloadService.this;
                vidplayerDownloadService.startForeground(foregroundNotificationId, Vid_player_DownloadNotification.getForegroundNotification(vidplayerDownloadService2, vidplayerDownloadService2.getString(R.string.music_end_of_download)));
                Vid_player_DownloadService.this.stopTimer.start();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        this.stopTimer.cancel();
        acquireWakeLock();
        startForeground(Vid_player_DownloadNotification.getForegroundNotificationId(), Vid_player_DownloadNotification.getForegroundNotification(this, getString(R.string.downloading)));
        if (intent == null) {
            return Service.START_NOT_STICKY;
        }
        String stringExtra = intent.getStringExtra(DS_ACTION);
        Vid_player_AudioItem vidplayerAudioItem = (Vid_player_AudioItem) intent.getParcelableExtra(DS_DOWNLOAD_SOURCE);
        String stringExtra2 = intent.getStringExtra(DS_AUDIO_ID);
        String stringExtra3 = intent.getStringExtra(DS_TITLE);
        String stringExtra4 = intent.getStringExtra(DS_DEST_DIR);
        int intExtra = intent.getIntExtra(DS_DOWNLOAD_ID, 0);
        if (stringExtra == null) {
            return Service.START_NOT_STICKY;
        }
        if (stringExtra.equals(ACTION_DOWNLOAD)) {
            this.vidplayerDownloadManager.download(stringExtra2, stringExtra4, stringExtra3);
            return Service.START_NOT_STICKY;
        } else if (!stringExtra.equals(ACTION_CANCEL_DOWNLOAD)) {
            return Service.START_NOT_STICKY;
        } else {
            this.vidplayerDownloadManager.cancelDownload(intExtra);
            return Service.START_NOT_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        if (this.vidplayerDownloadManager.isDownloading()) {
            this.vidplayerDownloadManager.close();
            releaseWakelock();
            Vid_player_DownloadNotification.cancelAll(this);
            stopForeground(true);
        }
        super.onDestroy();
    }

    private void acquireWakeLock() {
        if (this.wakeLock == null) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            this.powerManager = powerManager;
            this.wakeLock = powerManager.newWakeLock(1, WAKELOCK_TAG);
        }
        if (!this.wakeLock.isHeld()) {
            this.wakeLock.acquire();
        }
    }

    public void releaseWakelock() {
        if (this.wakeLock == null) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            this.powerManager = powerManager;
            this.wakeLock = powerManager.newWakeLock(1, WAKELOCK_TAG);
        }
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
    }

    private Vid_player_DelayTask stopTimer() {
        return new Vid_player_DelayTask(new Runnable() {
            @Override
            public void run() {
                Vid_player_DownloadService.this.vidplayerDownloadManager.close();
                Vid_player_DownloadService.this.releaseWakelock();
                Vid_player_DownloadService.this.stopForeground(true);
                Vid_player_DownloadService.this.stopSelf();
            }
        }, 5000L);
    }
}
