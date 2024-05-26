package com.allformats.video.player.downloader.ds_downloads;


public class Vid_player_MediaFormat {
    public static Vid_player_MediaFormat findMediaFormat(String str) {
        if (str.length() > 0) {
            for (String str2 : Vid_player_AudioVidplayerMedia.FILE_EXTENSIONS) {
                if (str2.equals(str)) {
                    return new Vid_player_AudioVidplayerMedia();
                }
            }
        }
        return new Vid_player_OtherFiles();
    }
}
