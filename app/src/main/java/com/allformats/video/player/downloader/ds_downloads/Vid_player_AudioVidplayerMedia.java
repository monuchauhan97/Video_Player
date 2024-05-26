package com.allformats.video.player.downloader.ds_downloads;

import com.google.android.exoplayer2.util.MimeTypes;


public class Vid_player_AudioVidplayerMedia extends Vid_player_MediaFormat {
    public static String[] FILE_EXTENSIONS = {"mp3", "aac", "m4a", "opus", "ogg", "oga", "wav", "amr", "flac", "wma", "ac3", "ape", "mka", "aiff"};
    public static String[] MIME_TYPES = {MimeTypes.AUDIO_MPEG, "audio/aac", "audio/m4a", MimeTypes.AUDIO_OPUS, "application/ogg", "audio/ogg", "audio/x-wav", "audio/amr", MimeTypes.AUDIO_FLAC, "audio/x-ms-wma", MimeTypes.AUDIO_AC3, "audio/ape", "audio/x-matroska", "audio/aiff"};

    public static String getMimeTypeFromExtension(String str) {
        for (int i = 0; i < FILE_EXTENSIONS.length; i++) {
            if (str.toLowerCase().equals(FILE_EXTENSIONS[i])) {
                return MIME_TYPES[i];
            }
        }
        return null;
    }
}
