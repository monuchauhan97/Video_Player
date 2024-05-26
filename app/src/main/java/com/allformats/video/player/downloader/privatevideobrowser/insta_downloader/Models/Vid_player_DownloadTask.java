package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.preference.PreferenceManager;

import com.allformats.video.player.downloader.ds_downloads.Vid_player_AudioVidplayerMedia;
import com.allformats.video.player.downloader.ds_downloads.Vid_player_DownloadNotification;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.ds_tube_android_util.Vid_player_FileUtil;
import com.allformats.video.player.downloader.ds_tube_android_util.Vid_player_IntentUtil;
import com.allformats.video.player.downloader.ds_tube_android_util.classes.Vid_player_DelayTask;
import com.allformats.video.player.downloader.ds_tube_connect.Vid_player_AudioConnector;
import com.allformats.video.player.downloader.ds_tube_connect.Vid_player_DownloadFormats;
import com.allformats.video.player.downloader.ds_tube_connect.Vid_player_M4AVidplayerAudioConnector;
import com.allformats.video.player.downloader.ds_tube_connect.Vid_player_MP3VidplayerAudioConnector;
import com.allformats.video.player.downloader.ds_tube_connect.Vid_player_OPUSVidplayerAudioConnector;
import com.allformats.video.player.downloader.view.Vid_player_PlayerActivity;

import java.io.File;


public class Vid_player_DownloadTask {
    public BaseDownloadTask baseDownloadTask;
    public boolean cancel = false;
    public Context context;
    public String downloadFormat;
    public Vid_player_DownloadNotification vidplayerDownloadNotification;
    public FileDownloader fileDownloader;
    public File finalFile;
    public File tempFile;
    private String audioId;
    private DownloadCallbacks callbacks;
    private String ext;
    private int id;
    private SharedPreferences preferences;
    private File tempFile2;


    public Vid_player_DownloadTask(Context context, FileDownloader fileDownloader, String str, String str2, String str3) {
        this.fileDownloader = fileDownloader;
        this.audioId = str;
        this.context = context;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.preferences = defaultSharedPreferences;
        String string = defaultSharedPreferences.getString(context.getString(R.string.audio_pref_key_download_format), Vid_player_DownloadFormats.M4A);
        this.downloadFormat = string;
        this.ext = string.toLowerCase();
        File notExistFile = Vid_player_FileUtil.getNotExistFile(new File(str2, Vid_player_FileUtil.parseToFilename(str3) + "." + this.ext));
        this.finalFile = notExistFile;
        String parent = notExistFile.getParent();
        this.tempFile = new File(parent, this.finalFile.getName() + ".temp");
        String parent2 = this.finalFile.getParent();
        this.tempFile2 = new File(parent2, this.finalFile.getName() + ".temp.temp");
        this.tempFile.delete();
        this.tempFile2.delete();
        int generateDownloadId = generateDownloadId(context);
        this.id = generateDownloadId;
        this.vidplayerDownloadNotification = new Vid_player_DownloadNotification(context, generateDownloadId);
    }

    public void start() {
        Vid_player_AudioConnector.OnConnectionCompleteListener onConnectionCompleteListener = new Vid_player_AudioConnector.OnConnectionCompleteListener() {
            @Override
            public void onConnectionSuccess(String str) {
                if (!Vid_player_DownloadTask.this.cancel) {
                    Vid_player_DownloadTask vidplayerDownloadTask = Vid_player_DownloadTask.this;
                    vidplayerDownloadTask.baseDownloadTask = vidplayerDownloadTask.fileDownloader.create(str).setAutoRetryTimes(2).setCallbackProgressTimes(Integer.MAX_VALUE).setCallbackProgressMinInterval(500).setPath(Vid_player_DownloadTask.this.tempFile.getAbsolutePath()).setListener(new FileDownloadListener() {
                        @Override
                        public void paused(BaseDownloadTask baseDownloadTask, int i, int i2) {
                        }

                        @Override
                        public void pending(BaseDownloadTask baseDownloadTask, int i, int i2) {
                            Vid_player_DownloadTask.this.onProgress(0, 0L, 0L);
                        }

                        @Override
                        public void progress(BaseDownloadTask baseDownloadTask, int i, int i2) {
                            double d = i;
                            double d2 = i2;
                            Double.isNaN(d);
                            Double.isNaN(d2);
                            Vid_player_DownloadTask vidplayerDownloadTask2 = Vid_player_DownloadTask.this;
                            Double.isNaN(d);
                            Double.isNaN(d2);
                            vidplayerDownloadTask2.onProgress((int) ((d / d2) * 100.0d), i, i2);
                        }

                        @Override
                        public void completed(BaseDownloadTask baseDownloadTask) {
                            Vid_player_DownloadTask.this.tempFile.renameTo(Vid_player_DownloadTask.this.finalFile);
                            Vid_player_DownloadTask.this.onComplete();
                        }

                        @Override
                        public void error(BaseDownloadTask baseDownloadTask, Throwable th) {
                            Vid_player_DownloadTask.this.onError(new Exception(th));
                        }

                        @Override
                        public void warn(BaseDownloadTask baseDownloadTask) {
                            Vid_player_DownloadTask.this.baseDownloadTask.pause();
                            Vid_player_DownloadTask.this.onError(new Exception("There has already had some same Tasks(Same-URL & Same-SavePath) in Pending-Queue or is running."));
                        }
                    });
                    Vid_player_DownloadTask.this.baseDownloadTask.start();
                }
            }

            @Override

            public void onConnectionError(Exception exc) {
                if (!Vid_player_DownloadTask.this.cancel) {
                    Vid_player_DownloadTask.this.onError(exc);
                }
            }
        };
        if (!this.cancel) {
            onStart();
            String str = this.downloadFormat;
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case 75674:
                    if (str.equals(Vid_player_DownloadFormats.M4A)) {
                        c = 0;
                        break;
                    }
                    break;
                case 76528:
                    if (str.equals(Vid_player_DownloadFormats.MP3)) {
                        c = 1;
                        break;
                    }
                    break;
                case 2433087:
                    if (str.equals(Vid_player_DownloadFormats.OPUS)) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    Vid_player_M4AVidplayerAudioConnector.tryConnectAudio(this.audioId, onConnectionCompleteListener);
                    return;
                case 1:
                    Vid_player_MP3VidplayerAudioConnector.tryConnectAudio(this.audioId, onConnectionCompleteListener);
                    return;
                case 2:
                    Vid_player_OPUSVidplayerAudioConnector.tryConnectAudio(this.audioId, onConnectionCompleteListener);
                    return;
                default:
                    return;
            }
        }
    }

    private void onStart() {
        DownloadCallbacks downloadCallbacks = this.callbacks;
        if (downloadCallbacks != null) {
            downloadCallbacks.onStart();
        }
    }

    public void onProgress(int i, long j, long j2) {
        DownloadCallbacks downloadCallbacks = this.callbacks;
        if (downloadCallbacks != null) {
            downloadCallbacks.onProgress(i, j, j2);
        }
    }

    public void onComplete() {
        this.tempFile.delete();
        this.tempFile2.delete();
        DownloadCallbacks downloadCallbacks = this.callbacks;
        if (downloadCallbacks != null) {
            downloadCallbacks.onCompleted(this.tempFile);
        }
        this.vidplayerDownloadNotification.cancel();
        new Vid_player_DelayTask(new Runnable() {
            @Override
            public void run() {
                Vid_player_DownloadNotification vidplayerDownloadNotification = Vid_player_DownloadTask.this.vidplayerDownloadNotification;
                String name = Vid_player_DownloadTask.this.finalFile.getName();
                String string = Vid_player_DownloadTask.this.context.getString(R.string.ds_download_success);
                Vid_player_DownloadTask vidplayerDownloadTask = Vid_player_DownloadTask.this;
                vidplayerDownloadNotification.notify(name, string, null, vidplayerDownloadTask.completeIntent(vidplayerDownloadTask.finalFile), R.drawable.ic_stat_success);
            }
        }, 500L).start();
    }

    private void onCancel() {
        this.tempFile.delete();
        this.tempFile2.delete();
        DownloadCallbacks downloadCallbacks = this.callbacks;
        if (downloadCallbacks != null) {
            downloadCallbacks.onCancel();
        }
        this.vidplayerDownloadNotification.cancel();
        new Vid_player_DelayTask(new Runnable() {
            @Override
            public void run() {
                Vid_player_DownloadTask.this.vidplayerDownloadNotification.notify(Vid_player_DownloadTask.this.finalFile.getName(), Vid_player_DownloadTask.this.context.getString(R.string.ds_download_canceled), null, null, R.drawable.ic_stat_cancel);
            }
        }, 500L).start();
    }

    public void onError(Exception exc) {
        this.tempFile.delete();
        this.tempFile2.delete();
        DownloadCallbacks downloadCallbacks = this.callbacks;
        if (downloadCallbacks != null) {
            downloadCallbacks.onError(exc);
        }
        this.vidplayerDownloadNotification.cancel();
        new Vid_player_DelayTask(new Runnable() {
            @Override
            public void run() {
                Vid_player_DownloadTask.this.vidplayerDownloadNotification.notify(Vid_player_DownloadTask.this.finalFile.getName(), Vid_player_DownloadTask.this.context.getString(R.string.ds_download_failed), null, null, R.drawable.ic_stat_error);
            }
        }, 500L).start();
    }

    public void setCallbacks(DownloadCallbacks downloadCallbacks) {
        this.callbacks = downloadCallbacks;
    }

    public Intent completeIntent(File file) {
        Vid_player_IntentUtil.disableDeathOnFileExposure();
        if (this.preferences.getBoolean(this.context.getString(R.string.audio_pref_key_open_with_internal_player), false)) {
            Intent intent = new Intent(this.context, Vid_player_PlayerActivity.class);
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.fromFile(file));
            return intent;
        }
        Intent intent2 = new Intent("android.intent.action.VIEW");
        intent2.setDataAndType(Uri.fromFile(file), Vid_player_AudioVidplayerMedia.getMimeTypeFromExtension(Vid_player_FileUtil.getFileExtension(file)));
        return Intent.createChooser(intent2, this.context.getString(R.string.open_file));
    }

    public void cancel() {
        this.cancel = true;
        this.baseDownloadTask.pause();
        onCancel();
    }

    public int getId() {
        return this.id;
    }

    private int generateDownloadId(Context context) {
        int i = this.preferences.getInt(context.getString(R.string.audio_pref_key_download_id), 1);
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putInt(context.getString(R.string.audio_pref_key_download_id), i + 1);
        edit.commit();
        return i;
    }

    public File getFile() {
        return this.finalFile;
    }

    public interface DownloadCallbacks {
        void onCancel();

        void onCompleted(File file);

        void onError(Exception exc);

        void onProgress(int i, long j, long j2);

        void onStart();
    }
}
