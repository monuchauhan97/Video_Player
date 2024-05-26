package com.allformats.video.player.downloader.ds_downloads;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_DownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.database.NoDatabaseImpl;
import com.allformats.video.player.downloader.ds_tube_android_util.classes.Vid_player_ToastCompat;

import java.io.File;

public class Vid_player_DownloadManager {
    public Context context;
    public boolean downloading;
    public Vid_player_Downloads vidplayerDownloads = new Vid_player_Downloads();
    private FileDownloader fileDownloader;
    public OnDownloadsFinishListener onDownloadsFinishListener;

    
    interface OnDownloadsFinishListener {
        void onDownloadsFinish();
    }

    
    public Vid_player_DownloadManager(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        try {
            FileDownloader.setupOnApplicationOnCreate((Application) this.context.getApplicationContext()).database(new NoDatabaseImpl.Maker());
            FileDownloader impl = FileDownloader.getImpl();
            this.fileDownloader = impl;
            impl.setMaxNetworkThreadCount(9);
        } catch (Exception e) {
            Log.e(getClass().getName(), Log.getStackTraceString(e));
        }
    }

    public void download(String str, String str2, String str3) {
        if (this.fileDownloader == null) {
            init();
        }
        FileDownloader fileDownloader = this.fileDownloader;
        if (fileDownloader != null) {
            final Vid_player_DownloadTask vidplayerDownloadTask = new Vid_player_DownloadTask(this.context, fileDownloader, str, str2, str3);
            vidplayerDownloadTask.setCallbacks(new Vid_player_DownloadTask.DownloadCallbacks() {
                @Override 
                public void onProgress(int i, long j, long j2) {
                }

                @Override 
                public void onStart() {
                    Vid_player_DownloadManager.this.downloading = true;
                }

                @Override 
                public void onCompleted(File file) {
                    Vid_player_DownloadManager.this.vidplayerDownloads.remove(vidplayerDownloadTask.getId());
                    if (Vid_player_DownloadManager.this.vidplayerDownloads.isEmpty()) {
                        Vid_player_DownloadManager.this.downloading = false;
                        if (Vid_player_DownloadManager.this.onDownloadsFinishListener != null) {
                            Vid_player_DownloadManager.this.onDownloadsFinishListener.onDownloadsFinish();
                        }
                    }
                }

                @Override 
                public void onError(Exception exc) {
                    if (exc.getCause() != null && exc.getCause().getMessage() != null) {
                        Vid_player_ToastCompat.showText(Vid_player_DownloadManager.this.context.getApplicationContext(), exc.getCause().getLocalizedMessage(), 1);
                    } else if (exc.getMessage() != null) {
                        Vid_player_ToastCompat.showText(Vid_player_DownloadManager.this.context.getApplicationContext(), exc.getLocalizedMessage(), 1);
                    }
                    Log.e(Vid_player_DownloadManager.class.getName(), Log.getStackTraceString(exc));
                    Vid_player_DownloadManager.this.vidplayerDownloads.remove(vidplayerDownloadTask.getId());
                    if (Vid_player_DownloadManager.this.vidplayerDownloads.isEmpty()) {
                        Vid_player_DownloadManager.this.downloading = false;
                        if (Vid_player_DownloadManager.this.onDownloadsFinishListener != null) {
                            Vid_player_DownloadManager.this.onDownloadsFinishListener.onDownloadsFinish();
                        }
                    }
                }

                @Override 
                public void onCancel() {
                    Vid_player_DownloadManager.this.vidplayerDownloads.remove(vidplayerDownloadTask.getId());
                    if (Vid_player_DownloadManager.this.vidplayerDownloads.isEmpty()) {
                        Vid_player_DownloadManager.this.downloading = false;
                        if (Vid_player_DownloadManager.this.onDownloadsFinishListener != null) {
                            Vid_player_DownloadManager.this.onDownloadsFinishListener.onDownloadsFinish();
                        }
                    }
                }
            });
            if (this.vidplayerDownloads.size() < 3 && !this.vidplayerDownloads.containsFile(vidplayerDownloadTask.getFile())) {
                this.vidplayerDownloads.add(vidplayerDownloadTask.getId(), vidplayerDownloadTask);
                vidplayerDownloadTask.start();
            }
            if (this.vidplayerDownloads.isEmpty()) {
                this.downloading = false;
                OnDownloadsFinishListener onDownloadsFinishListener = this.onDownloadsFinishListener;
                if (onDownloadsFinishListener != null) {
                    onDownloadsFinishListener.onDownloadsFinish();
                    return;
                }
                return;
            }
            return;
        }
        this.downloading = false;
        OnDownloadsFinishListener onDownloadsFinishListener2 = this.onDownloadsFinishListener;
        if (onDownloadsFinishListener2 != null) {
            onDownloadsFinishListener2.onDownloadsFinish();
        }
    }

    public void cancelDownload(int i) {
        Vid_player_DownloadTask vidplayerDownloadTask = this.vidplayerDownloads.get(i);
        if (vidplayerDownloadTask != null) {
            vidplayerDownloadTask.cancel();
        }
    }

    public void setOnDownloadsFinishListener(OnDownloadsFinishListener onDownloadsFinishListener) {
        this.onDownloadsFinishListener = onDownloadsFinishListener;
    }

    public void close() {
        FileDownloader fileDownloader = this.fileDownloader;
        if (fileDownloader != null) {
            fileDownloader.pauseAll();
            this.fileDownloader.clearAllTaskData();
        }
        this.fileDownloader = null;
    }

    public boolean isDownloading() {
        return this.downloading;
    }
}
