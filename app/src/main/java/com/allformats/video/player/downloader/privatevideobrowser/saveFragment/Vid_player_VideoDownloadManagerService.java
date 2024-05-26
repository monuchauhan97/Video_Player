package com.allformats.video.player.downloader.privatevideobrowser.saveFragment;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.util.Log;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.privatevideobrowser.saveFragment.lists.Vid_player_CompletedVideosData;
import com.allformats.video.player.downloader.privatevideobrowser.saveFragment.lists.Vid_player_DownloadQueuesData;
import com.allformats.video.player.downloader.privatevideobrowser.saveFragment.lists.Vid_player_InactiveDownloadsData;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Vid_player_VideoDownloadManagerService extends IntentService {
    public static Vid_player_VideoDownloadNotifier videoDownloadNotifier;
    private static ByteArrayOutputStream bytesOfChunk = null;
    private static boolean chunked = false;
    private static File downloadFile = null;
    private static long downloadSpeed = 0;
    private static Thread downloadThread = null;
    private static OnDownloadFinishedListener onDownloadFinishedListener = null;
    private static OnLinkNotFoundListener onLinkNotFoundListener = null;
    private static long prevDownloaded = 0;
    private static boolean stop = false;
    private static long totalSize;

    public Vid_player_VideoDownloadManagerService() {
        super("VideoDownloadManagerService");
    }

    public static void stop() {
        Log.d("debug", "stop: called");
        Vid_player_VideoDownloadNotifier videoDownloadNotifier = null;
        if (videoDownloadNotifier != null) {
            videoDownloadNotifier.cancel();
        }
        Vid_player_DS_Helper.getInstance().stopService(Vid_player_DS_Helper.getInstance().getDownloadService());
        forceStopIfNecessary();
    }

    public static void forceStopIfNecessary() {
        if (downloadThread != null) {
            Log.d("debug", "force: called");
            Thread currentThread = Thread.currentThread();
            downloadThread = currentThread;
            if (currentThread.isAlive()) {
                stop = true;
            }
        }
    }
    public void onHandleIntent(Intent intent) {
        Intent intent2 = intent;
        String str = "link";
        String str2 = "VDInfo";
        stop = false;
        downloadThread = Thread.currentThread();
        videoDownloadNotifier = new Vid_player_VideoDownloadNotifier(intent2);
        if (intent2 != null) {
            boolean booleanExtra = intent2.getBooleanExtra("chunked", false);
            chunked = booleanExtra;
            if (booleanExtra) {
                downloadFile = null;
                prevDownloaded = 0;
                downloadSpeed = 0;
                totalSize = 0;
                handleChunkedDownload(intent);
            } else {
                prevDownloaded = 0;
                try {
                    Object obj;
                    totalSize = Long.parseLong(intent2.getStringExtra("size"));
                    URLConnection openConnection = new URL(intent2.getStringExtra(str)).openConnection();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(intent2.getStringExtra("name"));
                    stringBuilder.append(".");
                    stringBuilder.append(intent2.getStringExtra("type"));
                    String stringBuilder2 = stringBuilder.toString();
                    File file = new File(Vid_player_DS_Helper.PATH_VIDEO_DOWNLOADER);
                    if (!(file.exists() || file.mkdir())) {
                        if (!file.createNewFile()) {
                            obj = null;
                            if (obj == null) {
                                videoDownloadNotifier.notifyDownloading();
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(Vid_player_DS_Helper.PATH_VIDEO_DOWNLOADER);
                                stringBuilder3.append("/");
                                stringBuilder3.append(stringBuilder2);
                                file = new File(stringBuilder3.toString());
                                downloadFile = file;
                                if (openConnection != null) {
                                    FileOutputStream fileOutputStream;
                                    if (file.exists()) {
                                        prevDownloaded = downloadFile.length();
                                        stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append("bytes=");
                                        stringBuilder3.append(downloadFile.length());
                                        stringBuilder3.append("-");
                                        openConnection.setRequestProperty("Range", stringBuilder3.toString());
                                        openConnection.connect();
                                        fileOutputStream = new FileOutputStream(downloadFile.getAbsolutePath(), true);
                                    } else {
                                        openConnection.connect();
                                        fileOutputStream = downloadFile.createNewFile() ? new FileOutputStream(downloadFile.getAbsolutePath(), true) : null;
                                    }
                                    if (fileOutputStream != null && downloadFile.exists()) {
                                        InputStream inputStream = openConnection.getInputStream();
                                        ReadableByteChannel newChannel = Channels.newChannel(inputStream);
                                        FileChannel channel = fileOutputStream.getChannel();
                                        while (downloadFile.length() < totalSize) {
                                            if (!stop) {
                                                channel.transferFrom(newChannel, 0, 1024);
                                                if (downloadFile == null) {
                                                    return;
                                                }
                                            }
                                            return;
                                        }
                                        newChannel.close();
                                        inputStream.close();
                                        fileOutputStream.flush();
                                        fileOutputStream.close();
                                        channel.close();
                                        downloadFinished(stringBuilder2);
                                    }
                                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{downloadFile.getAbsolutePath()}, null, new OnScanCompletedListener() {
                                        public void onScanCompleted(String str, Uri uri) {
                                        }
                                    });
                                }
                            } else {
                                Log.e(str2, "directory doesn't exist");
                            }
                        }
                    }
                    obj = 1;
                    if (obj == null) {
                    }
                } catch (FileNotFoundException unused) {
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("link:");
                    stringBuilder4.append(intent2.getStringExtra(str));
                    stringBuilder4.append(" not found");
                    Log.i(str2, stringBuilder4.toString());
                    linkNotFound(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void downloadFinished(String str) {
        videoDownloadNotifier.notifyDownloadFinished();
        OnDownloadFinishedListener onDownloadFinishedListener = this.onDownloadFinishedListener;
        if (onDownloadFinishedListener != null) {
            onDownloadFinishedListener.onDownloadFinished();
            return;
        }
        Vid_player_DownloadQueuesData load = Vid_player_DownloadQueuesData.load(getApplicationContext());
        load.deleteTopVideo(getApplicationContext());
        Vid_player_CompletedVideosData.load(getApplicationContext()).addVideo(getApplicationContext(), str);
        Vid_player_DownloadVideo topVideo = load.getTopVideo();
        if (topVideo != null) {
            Intent downloadService = Vid_player_DS_Helper.getInstance().getDownloadService();
            downloadService.putExtra("link", topVideo.link);
            downloadService.putExtra("name", topVideo.name);
            downloadService.putExtra("type", topVideo.type);
            downloadService.putExtra("size", topVideo.size);
            downloadService.putExtra("page", topVideo.page);
            downloadService.putExtra("chunked", topVideo.chunked);
            downloadService.putExtra("website", topVideo.website);
            onHandleIntent(downloadService);
        }
    }

    private void linkNotFound(Intent intent) {
        videoDownloadNotifier.cancel();
        OnLinkNotFoundListener onLinkNotFoundListener = this.onLinkNotFoundListener;
        if (onLinkNotFoundListener != null) {
            onLinkNotFoundListener.onLinkNotFound();
            return;
        }
        Vid_player_DownloadQueuesData load = Vid_player_DownloadQueuesData.load(getApplicationContext());
        load.deleteTopVideo(getApplicationContext());
        Vid_player_DownloadVideo vidplayerDownloadVideo = new Vid_player_DownloadVideo();
        String str = "name";
        vidplayerDownloadVideo.name = intent.getStringExtra(str);
        String str2 = "link";
        vidplayerDownloadVideo.link = intent.getStringExtra(str2);
        String str3 = "type";
        vidplayerDownloadVideo.type = intent.getStringExtra(str3);
        String str4 = "size";
        vidplayerDownloadVideo.size = intent.getStringExtra(str4);
        String str5 = "page";
        vidplayerDownloadVideo.page = intent.getStringExtra(str5);
        String str6 = "website";
        vidplayerDownloadVideo.website = intent.getStringExtra(str6);
        String str7 = "chunked";
        vidplayerDownloadVideo.chunked = intent.getBooleanExtra(str7, false);
        Vid_player_InactiveDownloadsData.load(getApplicationContext()).add(getApplicationContext(), vidplayerDownloadVideo);
        Vid_player_DownloadVideo topVideo = load.getTopVideo();
        if (topVideo != null) {
            Intent downloadService = Vid_player_DS_Helper.getInstance().getDownloadService();
            downloadService.putExtra(str2, topVideo.link);
            downloadService.putExtra(str, topVideo.name);
            downloadService.putExtra(str3, topVideo.type);
            downloadService.putExtra(str4, topVideo.size);
            downloadService.putExtra(str5, topVideo.page);
            downloadService.putExtra(str7, topVideo.chunked);
            downloadService.putExtra(str6, topVideo.website);
            onHandleIntent(downloadService);
        }
    }

    private void handleChunkedDownload(Intent intent) {
        Vid_player_VideoDownloadManagerService vidplayerVideoDownloadManagerService = this;
        Intent intent2 = intent;
        String str = ".";
        try {
            String stringExtra = intent2.getStringExtra("name");
            String stringExtra2 = intent2.getStringExtra("type");
            File file = new File(Vid_player_DS_Helper.PATH_VIDEO_DOWNLOADER);
            int i = 1;
            Object obj = (file.exists() || file.mkdir() || file.createNewFile()) ? 1 : null;
            String str2 = "VDInfo";
            if (obj != null) {
                long readLong;
                videoDownloadNotifier.notifyDownloading();
                File cacheDir = getCacheDir();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(stringExtra);
                stringBuilder.append(".dat");
                file = new File(cacheDir, stringBuilder.toString());
                stringBuilder = new StringBuilder();
                stringBuilder.append(Vid_player_DS_Helper.PATH_VIDEO_DOWNLOADER);
                stringBuilder.append("/");
                stringBuilder.append(stringExtra);
                stringBuilder.append(str);
                stringBuilder.append(stringExtra2);
                cacheDir = new File(stringBuilder.toString());
                String str3 = "can not create file for download";
                long j = 0;
                if (file.exists()) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                    readLong = dataInputStream.readLong();
                    dataInputStream.close();
                    fileInputStream.close();
                    if (!(cacheDir.exists() || cacheDir.createNewFile())) {
                        Log.i(str2, str3);
                    }
                } else {
                    if (cacheDir.exists()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(stringExtra);
                        stringBuilder.append(str);
                        stringBuilder.append(stringExtra2);
                        vidplayerVideoDownloadManagerService.downloadFinished(stringBuilder.toString());
                    } else {
                        if (!cacheDir.createNewFile()) {
                            Log.i(str2, str3);
                        }
                        if (!file.createNewFile()) {
                            Log.i(str2, "can not create progressFile");
                        }
                    }
                    readLong = 0;
                }
                if (cacheDir.exists() && file.exists()) {
                    long j2 = readLong;
                    while (true) {
                        prevDownloaded = j;
                        String stringExtra3 = intent2.getStringExtra("website");
                        int i2;
                        switch (stringExtra3.hashCode()) {
                            case -1557529491:
                                if (stringExtra3.equals("myspace.com")) {
                                    i2 = 4;
                                    break;
                                }
                            case -1183889957:
                                if (stringExtra3.equals("favicon_twitter.com")) {
                                    i2 = 2;
                                    break;
                                }
                            case -820506955:
                                if (stringExtra3.equals("metacafe.com")) {
                                    i2 = 3;
                                    break;
                                }
                            case -159567326:
                                if (stringExtra3.equals("dailymotion.com")) {
                                    i2 = 0;
                                    break;
                                }
                            case 729407191:
                                if (stringExtra3.equals("vimeo.com")) {
                                    i2 = 1;
                                    break;
                                }
                            default:
                        }
                        i = 1;
                        j = 0;
                        intent2 = intent;
                    }
                }
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{cacheDir.getAbsolutePath()}, null, new OnScanCompletedListener() {
                    public void onScanCompleted(String str, Uri uri) {
                    }
                });
            } else {
                Log.e(str2, "directory doesn't exist");
            }
        } catch (IOException e) {
            IOException e2 = e;
            e2.printStackTrace();
        }
    }

    public void onDestroy() {
        downloadFile = null;
        Thread.currentThread().interrupt();
        super.onDestroy();
    }

    public interface OnDownloadFinishedListener {
        void onDownloadFinished();
    }

    public interface OnLinkNotFoundListener {
        void onLinkNotFound();
    }
}
