package com.allformats.video.player.downloader.video_player.Util;

import android.os.Environment;

public class Vid_player_Constant {
    public static final String EXTRA_BACKGROUND_VIDEO_PLAY_POSITION = "background_video_play_position";
    public static final String EXTRA_FLOATING_VIDEO = "floating_video";
    public static final String EXTRA_IS_CONTINUE_WATCHING_VIDEO = "is_continue_watching_video";
    public static final String EXTRA_IS_FLOATING_VIDEO = "is_floating_video";
    public static final String EXTRA_VIDEO = "extra_video";
    public static final String EXTRA_VIDEO_LIST = "video_list";
    public static final String EXTRA_VIDEO_POSITION = "video_position";
    public static final String PREF_CONTINUE_WATCHING_VIDEO = "pref_continue_watching_video";
    public static final String PREF_FLOATING_VIDEO_POSITION = "pref_floating_video_position";
    public static final String PREF_IS_FLOATING_VIDEO = "pref_is_floating_video";
    public static final String PREF_LAST_PLAY_VIDEOS = "last_play_videos";
    public static final String PREF_VIDEO_LAST_POSITION = "pref_video_last_position";
    public static final String PREF_VIDEO_LIST = "pref_video_list";
    public static String HIDE_PATH = Environment.getExternalStorageDirectory().toString() + "/.Hide_Video";
    public static String YOUR_PATH = Environment.getExternalStorageDirectory().toString() + "/Your_Video";
}
