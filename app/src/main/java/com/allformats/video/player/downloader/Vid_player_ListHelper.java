package com.allformats.video.player.downloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import org.schabi.newpipe.extractor.MediaFormat;
import org.schabi.newpipe.extractor.stream_info.AudioStream;
import org.schabi.newpipe.extractor.stream_info.VideoStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public final class Vid_player_ListHelper {

    private static final List<MediaFormat> VIDEO_FORMAT_QUALITY_RANKING = Arrays.asList(MediaFormat.v3GPP, MediaFormat.WEBM, MediaFormat.MPEG_4);
    private static final List<MediaFormat> AUDIO_FORMAT_QUALITY_RANKING = Arrays.asList(MediaFormat.v3GPP, MediaFormat.WEBMA, MediaFormat.M4A);
    private static final List<String> HIGH_RESOLUTION_LIST = Arrays.asList("1440p", "2160p", "1440p60", "2160p60");

    public static int getDefaultResolutionIndex(Context context, List<VideoStream> list) {
        return getDefaultResolutionWithDefaultFormat(context, computeDefaultResolution(context, R.string.default_resolution_key, R.string.default_resolution_value), list);
    }

    public static List<VideoStream> getSortedStreamVideosList(Context context, List<VideoStream> list, List<VideoStream> list2, boolean z) {
        return getSortedStreamVideosList(getDefaultFormat(context, R.string.default_video_format_key, R.string.default_video_format_value), PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.show_higher_resolutions_key), false), list, list2, z);
    }

    private static String computeDefaultResolution(Context context, int i, int i2) {
        String str;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (defaultSharedPreferences != null) {
            str = defaultSharedPreferences.getString(context.getString(i), context.getString(i2));
        } else {
            str = context.getString(R.string.best_resolution_key);
        }
        String resolutionLimit = getResolutionLimit(context);
        return resolutionLimit != null ? (str.equals(context.getString(R.string.best_resolution_key)) || compareVideoStreamResolution(resolutionLimit, str) < 1) ? resolutionLimit : str : str;
    }

    static int getDefaultResolutionIndex(String str, String str2, MediaFormat mediaFormat, List<VideoStream> list) {
        int videoStreamIndex;
        if (list == null || list.isEmpty()) {
            return -1;
        }
        sortStreamList(list, false);
        if (!str.equals(str2) && (videoStreamIndex = getVideoStreamIndex(str, mediaFormat, list)) != -1) {
            return videoStreamIndex;
        }
        return 0;
    }

    static List<VideoStream> getSortedStreamVideosList(MediaFormat mediaFormat, boolean z, List<VideoStream> list, List<VideoStream> list2, boolean z2) {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        if (list2 != null) {
            for (VideoStream videoStream : list2) {
                if (z || !HIGH_RESOLUTION_LIST.contains(videoStream.resolution)) {
                    arrayList.add(videoStream);
                }
            }
        }
        if (list != null) {
            for (VideoStream videoStream2 : list) {
                if (z || !HIGH_RESOLUTION_LIST.contains(videoStream2.resolution)) {
                    arrayList.add(videoStream2);
                }
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            VideoStream videoStream3 = (VideoStream) it.next();
            hashMap.put(videoStream3.resolution, videoStream3);
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            VideoStream videoStream4 = (VideoStream) it2.next();
                hashMap.put(videoStream4.resolution, videoStream4);

        }
        arrayList.clear();
        arrayList.addAll(hashMap.values());
        sortStreamList(arrayList, z2);
        return arrayList;
    }

    private static void sortStreamList(List<VideoStream> list, final boolean z) {
        Collections.sort(list, new Comparator() {
            @Override
            public final int compare(Object obj, Object obj2) {
                return Vid_player_ListHelper.lambda$sortStreamList$0(z, (VideoStream) obj, (VideoStream) obj2);
            }
        });
    }


    public static int lambda$sortStreamList$0(boolean z, VideoStream videoStream, VideoStream videoStream2) {
        int compareVideoStreamResolution = compareVideoStreamResolution(videoStream, videoStream2);
        if (compareVideoStreamResolution == 0) {
            return 0;
        }
        return z ? compareVideoStreamResolution : -compareVideoStreamResolution;
    }

    static int getHighestQualityAudioIndex(MediaFormat mediaFormat, List<AudioStream> list) {
        if (list == null) {
            return -1;
        }
        int i = -1;
        while (i == -1) {
            AudioStream audioStream = null;
            for (int i2 = 0; i2 < list.size(); i2++) {
                AudioStream audioStream2 = list.get(i2);
                if ((mediaFormat == null) && (audioStream == null || compareAudioStreamBitrate(audioStream, audioStream2, AUDIO_FORMAT_QUALITY_RANKING) < 0)) {
                    i = i2;
                    audioStream = audioStream2;
                }
            }
            if (i == -1 && mediaFormat == null) {
                break;
            }
            mediaFormat = null;
        }
        return i;
    }


    static int getVideoStreamIndex(String str, MediaFormat mediaFormat, List<VideoStream> list) {
        String replaceAll = str.replaceAll("p\\d+$", "p");
        int i = -1;
        int i2 = -1;
        int i3 = -1;
        int i4 = -1;
        int i5 = -1;
        for (int i6 = 0; i6 < list.size(); i6++) {
            Integer format = mediaFormat == null ? null : list.get(i6).format;
            String resolution = list.get(i6).resolution;
            String replaceAll2 = resolution.replaceAll("p\\d+$", "p");
            if (resolution.equals(str)) {
                i = i6;
            }
            if (replaceAll2.equals(replaceAll)) {
                i2 = i6;
            }
            if (i3 == -1 && resolution.equals(str)) {
                i3 = i6;
            }
            if (i4 == -1 && replaceAll2.equals(replaceAll)) {
                i4 = i6;
            }
            if (i5 == -1 && compareVideoStreamResolution(replaceAll2, replaceAll) < 0) {
                i5 = i6;
            }
        }
        return i != -1 ? i : i2 != -1 ? i2 : i3 != -1 ? i3 : i4 != -1 ? i4 : i5;
    }

    private static int getDefaultResolutionWithDefaultFormat(Context context, String str, List<VideoStream> list) {
        return getDefaultResolutionIndex(str, context.getString(R.string.best_resolution_key), getDefaultFormat(context, R.string.default_video_format_key, R.string.default_video_format_value), list);
    }

    private static MediaFormat getDefaultFormat(Context context, int i, int i2) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = context.getString(i2);
        MediaFormat mediaFormatFromKey = getMediaFormatFromKey(context, defaultSharedPreferences.getString(context.getString(i), string));
        if (mediaFormatFromKey != null) {
            return mediaFormatFromKey;
        }
        defaultSharedPreferences.edit().putString(context.getString(i), string).apply();
        return getMediaFormatFromKey(context, string);
    }

    private static MediaFormat getMediaFormatFromKey(Context context, String str) {
        if (str.equals(context.getString(R.string.video_webm_key))) {
            return MediaFormat.WEBM;
        }
        if (str.equals(context.getString(R.string.video_mp4_key))) {
            return MediaFormat.MPEG_4;
        }
        if (str.equals(context.getString(R.string.video_3gp_key))) {
            return MediaFormat.v3GPP;
        }
        if (str.equals(context.getString(R.string.audio_webm_key))) {
            return MediaFormat.WEBMA;
        }
        if (str.equals(context.getString(R.string.audio_m4a_key))) {
            return MediaFormat.M4A;
        }
        return null;
    }

    private static int compareAudioStreamBitrate(AudioStream audioStream, AudioStream audioStream2, List<MediaFormat> list) {
        if (audioStream == null) {
            return -1;
        }
        if (audioStream2 == null) {
            return 1;
        }
        if (audioStream.avgBitrate < audioStream2.avgBitrate) {
            return -1;
        }
        if (audioStream.avgBitrate > audioStream2.avgBitrate) {
            return 1;
        }
        return list.indexOf(audioStream.format) - list.indexOf(audioStream2.format);
    }

    private static int compareVideoStreamResolution(String str, String str2) {
        return Integer.parseInt(str.replaceAll("0p\\d+$", "1").replaceAll("[^\\d.]", "")) - Integer.parseInt(str2.replaceAll("0p\\d+$", "1").replaceAll("[^\\d.]", ""));
    }

    private static int compareVideoStreamResolution(VideoStream videoStream, VideoStream videoStream2) {
        if (videoStream == null) {
            return -1;
        }
        if (videoStream2 == null) {
            return 1;
        }
        int compareVideoStreamResolution = compareVideoStreamResolution(videoStream.resolution, videoStream2.resolution);
        if (compareVideoStreamResolution != 0) {
            return compareVideoStreamResolution;
        }
        List<MediaFormat> list = VIDEO_FORMAT_QUALITY_RANKING;
        return list.indexOf(videoStream.format) - list.indexOf(videoStream2.format);
    }


    private static String getResolutionLimit(Context context) {
        if (!isMeteredNetwork(context)) {
            return null;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = context.getString(R.string.limit_data_usage_none_key);
        String string2 = defaultSharedPreferences.getString(context.getString(R.string.limit_mobile_data_usage_key), string);
        if (string.equals(string2)) {
            return null;
        }
        return string2;
    }

    private static boolean isMeteredNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null) {
            return false;
        }
        return connectivityManager.isActiveNetworkMetered();
    }
}
