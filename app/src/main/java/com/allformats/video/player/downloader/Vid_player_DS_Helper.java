package com.allformats.video.player.downloader;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.allformats.video.player.downloader.privatevideobrowser.Vid_player_MainBrowserActivity.OnBackPressedListener;
import com.allformats.video.player.downloader.privatevideobrowser.saveFragment.Vid_player_VideoDownloadManagerService;
import com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_HistoryVideo;
import com.allformats.video.player.downloader.video_player.Util.Vid_player_Constant;
import com.appizona.yehiahd.fastsave.FastSave;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection.Configuration;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection.Creator;

import org.schabi.newpipe.DownloaderImpl;
import org.schabi.newpipe.extractor.Downloader;
import org.schabi.newpipe.extractor.NewPipe;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import plugin.adsdk.service.BaseApp;

public class Vid_player_DS_Helper extends BaseApp {
    public static String PATH_VIDEO_DOWNLOADER = "";
    public static Vid_player_DS_Helper mInstance = null;
    public static boolean aBoolean = false;
    public static boolean wb = false;
    static Editor prefEditor = null;
    static SharedPreferences preferences = null;

    private Intent downloadService;
    private OnBackPressedListener onBackPressedListener;

    protected static boolean isDisposedRxExceptionsReported() {
        return false;
    }

    public static Vid_player_DS_Helper getInstance() {
        return mInstance;
    }

    private static void setInstance(Vid_player_DS_Helper dS_HelperVidplayer) {
        mInstance = dS_HelperVidplayer;
    }

    private static void initNotificationChannel(Context context) {
        if (VERSION.SDK_INT >= 26) {
            String string = context.getString(R.string.notification_channel_id);
            String string2 = context.getString(R.string.notification_channel_name);
            String string3 = context.getString(R.string.notification_channel_description);
            NotificationChannel notificationChannel = new NotificationChannel(string, string2, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(string3);
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
            setUpUpdateNotificationChannel(2, context);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void setUpUpdateNotificationChannel(int i, Context context) {
        String string = context.getString(R.string.app_update_notification_channel_id);
        String string2 = context.getString(R.string.app_update_notification_channel_name);
        String string3 = context.getString(R.string.app_update_notification_channel_description);
        NotificationChannel notificationChannel = new NotificationChannel(string, string2, i);
        notificationChannel.setDescription(string3);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
    }
    public static boolean hasAssignableCauseThrowable(Throwable th, Class<?>... clsArr) {
        Class cls = th.getClass();
        for (Class isAssignableFrom : clsArr) {
            if (isAssignableFrom.isAssignableFrom(cls)) {
                return true;
            }
        }
        Throwable th2 = th;
        while (true) {
            Throwable cause = th.getCause();
            if (cause == null || th2 == cause) {
                return false;
            }
            cls = cause.getClass();
            for (Class isAssignableFrom2 : clsArr) {
                if (isAssignableFrom2.isAssignableFrom(cls)) {
                    return true;
                }
            }
            th2 = cause;
        }
    }
    public static String getRecentPlay() {
        return preferences.getString("recent", "");
    }

    public static void putRecentPlay(String str) {
        prefEditor.putString("recent", str);
        prefEditor.commit();
    }

    public static int getViewBy() {
        return preferences.getInt("view_by", 0);
    }

    public static void putViewBy(int i) {
        prefEditor.putInt("view_by", i);
        prefEditor.commit();
    }

    public static String getSecurityQuestion() {
        return preferences.getString("security_question", "");
    }

    public static void putSecurityQuestion(String str) {
        prefEditor.putString("security_question", str);
        prefEditor.commit();
    }

    public static String getAnswerQuestion() {
        return preferences.getString("security_answer", "");
    }

    public static void putAnswerQuestion(String str) {
        prefEditor.putString("security_answer", str);
        prefEditor.commit();
    }

    public static String getPass() {
        return preferences.getString("pass", "");
    }

    public static void putPass(String str) {
        prefEditor.putString("pass", str);
        prefEditor.commit();
    }

    public static boolean getSetPass() {
        return preferences.getBoolean("set_pass", false);
    }

    public static void putSetPass(boolean z) {
        prefEditor.putBoolean("set_pass", z);
        prefEditor.commit();
    }

    public static boolean getSetQuestion() {
        return preferences.getBoolean("set_question", false);
    }

    public static void putSetQuestion(boolean z) {
        prefEditor.putBoolean("set_question", z);
        prefEditor.commit();
    }

    public static HashMap<String, Integer> getVideoLastPosition() {
        HashMap<String, Integer> hashMap = new HashMap();
        String string = preferences.getString(Vid_player_Constant.PREF_VIDEO_LAST_POSITION, "");
        return string != null ? (HashMap) new Gson().fromJson(string, HashMap.class) : hashMap;
    }
    public static ArrayList<Vid_player_MediaData> getVideoList() {
        ArrayList arrayList = new ArrayList();
        String string = preferences.getString(Vid_player_Constant.PREF_VIDEO_LIST, "");
        if (string != null) {
            Vid_player_HistoryVideo vidplayerHistoryVideo = (Vid_player_HistoryVideo) new Gson().fromJson(string, Vid_player_HistoryVideo.class);
            if (vidplayerHistoryVideo != null) {
                arrayList.addAll(vidplayerHistoryVideo.getVideoList());
            }
        }
        return arrayList;
    }

    public static void setIsFloatingVideo(boolean z) {
        prefEditor.putBoolean(Vid_player_Constant.PREF_IS_FLOATING_VIDEO, z);
        prefEditor.apply();
    }

    public static Vid_player_MediaData getLastPlayVideos() {
        String string = preferences.getString(Vid_player_Constant.PREF_LAST_PLAY_VIDEOS, "");
        return string != null ? (Vid_player_MediaData) new Gson().fromJson(string, Vid_player_MediaData.class) : null;
    }

    public static List<Vid_player_MediaData> getContinueWatchingVideos() {
        ArrayList arrayList = new ArrayList();
        String string = preferences.getString(Vid_player_Constant.PREF_CONTINUE_WATCHING_VIDEO, "");
        if (string != null) {
            Vid_player_HistoryVideo vidplayerHistoryVideo = (Vid_player_HistoryVideo) new Gson().fromJson(string, Vid_player_HistoryVideo.class);
            if (vidplayerHistoryVideo != null) {
                arrayList.addAll(vidplayerHistoryVideo.getVideoList());
            }
        }
        return arrayList;
    }


    public void onCreate() {
        super.onCreate();
        mInstance = this;
        SharedPreferences sharedPreferences = getSharedPreferences("video_player", 0);
        preferences = sharedPreferences;
        Editor edit = sharedPreferences.edit();
        prefEditor = edit;
        edit.commit();
        final UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable th) {
                if ((!thread.getName().equals("FinalizerWatchdogDaemon") && !thread.getName().equals("FinalizerDaemon")) || !(th instanceof TimeoutException)) {
                    defaultUncaughtExceptionHandler.uncaughtException(thread, th);
                }
            }
        });
        FastSave.init(getApplicationContext());
        Builder builder = new Builder();
        StrictMode.setVmPolicy(builder.build());
        if (VERSION.SDK_INT >= 18) {
            builder.detectFileUriExposure();
        }

        this.downloadService = new Intent(getApplicationContext(), Vid_player_VideoDownloadManagerService.class);
        if (mInstance == null) {
            setInstance(this);
        }
        FileDownloader.setupOnApplicationOnCreate(this).connectionCreator(new Creator(new Configuration().connectTimeout(15000).readTimeout(15000))).commit();
        configureRxJavaErrorHandler();
        initNotificationChannel(getApplicationContext());
        NewPipe.init(getDownloader());
//        Vid_player_Localization.init();
    }

    public Downloader getDownloader() {
        DownloaderImpl init = DownloaderImpl.getInstance();
        setCookiesToDownloader(init);
        return init;
    }

    public Intent getDownloadService() {
        return this.downloadService;
    }

    public OnBackPressedListener getOnBackPressedListener() {
        return this.onBackPressedListener;
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    private void configureRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            public void accept(Throwable th) {
                Throwable th2 = null;
                String str = "DS_Helper";
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("RxJavaPlugins.ErrorHandler called with -> : throwable = [");
                stringBuilder.append(th2.getClass().getName());
                stringBuilder.append("]");
                Log.e(str, stringBuilder.toString());
                if (th2 instanceof TimeoutException) {
                    th2 = ((TimeoutException) th2).getCause();
                }
                List<Throwable> exceptions;
                if (th2 instanceof CompositeException) {
                    exceptions = ((CompositeException) th2).getExceptions();
                } else {
                    exceptions = Collections.singletonList(th2);
                }
                for (Throwable th3 : exceptions) {
                    if (!isThrowableIgnored(th3)) {
                        if (isThrowableCritical(th3)) {
                            reportException(th3);
                            return;
                        }
                    }
                    return;
                }
                if (Vid_player_DS_Helper.isDisposedRxExceptionsReported()) {
                    reportException(th2);
                } else {
                    Log.e("DS_Helper", "RxJavaPlugin: Undeliverable Exception received: ", th2);
                }
            }

            private boolean isThrowableIgnored(Throwable th) {
                return Vid_player_DS_Helper.hasAssignableCauseThrowable(th, IOException.class, SocketException.class, InterruptedException.class, InterruptedIOException.class);
            }

            private boolean isThrowableCritical(Throwable th) {
                return Vid_player_DS_Helper.hasAssignableCauseThrowable(th, NullPointerException.class, IllegalArgumentException.class, Exception.class, MissingBackpressureException.class, IllegalStateException.class);
            }

            private void reportException(Throwable th) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
            }
        });
    }

    public void setCookiesToDownloader(DownloaderImpl downloaderImpl) {
    }

    public boolean isFloatingVideo() {
        return preferences.getBoolean(Vid_player_Constant.PREF_IS_FLOATING_VIDEO, false);
    }

    public void updateContinueWatchingVideo(ArrayList<Vid_player_MediaData> arrayList) {
        prefEditor.putString(Vid_player_Constant.PREF_CONTINUE_WATCHING_VIDEO, new Gson().toJson(new Vid_player_HistoryVideo(arrayList)));
        prefEditor.apply();
    }
}
