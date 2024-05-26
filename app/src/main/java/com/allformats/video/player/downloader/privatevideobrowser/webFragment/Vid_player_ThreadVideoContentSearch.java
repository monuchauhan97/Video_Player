package com.allformats.video.player.downloader.privatevideobrowser.webFragment;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.google.android.exoplayer2.util.MimeTypes;
import com.allformats.video.player.downloader.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;

public abstract class Vid_player_ThreadVideoContentSearch extends Thread {
    private Context context;
    private int numLinksInspected;
    private String page;
    private String title;
    private String url;

    public abstract void onFinishedInspectingURL(boolean z);

    public abstract void onStartInspectingURL();

    public abstract void onVideoFound(String str, String str2, String str3, String str4, String str5, boolean z, String str6, boolean z2);

    public Vid_player_ThreadVideoContentSearch(Context context, String str, String str2, String str3) {
        this.context = context;
        this.url = str;
        this.page = str2;
        this.title = str3;
        this.numLinksInspected = 0;
    }

    public void run() {
        Object obj;
        String toLowerCase = this.url.toLowerCase();
        boolean z = false;
        for (CharSequence contains : Vid_player_DS_Helper.getInstance().getResources().getStringArray(R.array.videourl_filters)) {
            if (toLowerCase.contains(contains)) {
                obj = 1;
                break;
            }
        }
        obj = null;
        if (obj != null) {
            this.numLinksInspected++;
            onStartInspectingURL();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("retreiving headers from ");
            stringBuilder.append(this.url);
            String str = "VDInfo";
            Log.i(str, stringBuilder.toString());
            URLConnection uRLConnection = null;
            try {
                uRLConnection = new URL(this.url).openConnection();
                uRLConnection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (uRLConnection != null) {
                String headerField = uRLConnection.getHeaderField("content-type");
                if (headerField != null) {
                    headerField = headerField.toLowerCase();
                    if (headerField.contains("video") || headerField.contains("audio")) {
                        addVideoToList(uRLConnection, this.page, this.title, headerField);
                    } else if (headerField.equals("application/x-mpegurl") || headerField.equals("application/vnd.apple.mpegurl")) {
                        addVideosToListFromM3U8(uRLConnection, this.page, this.title, headerField);
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Not a video. Content type = ");
                        stringBuilder.append(headerField);
                        Log.i(str, stringBuilder.toString());
                    }
                } else {
                    Log.i(str, "no content type");
                }
            } else {
                Log.i(str, "no connection");
            }
            int i = this.numLinksInspected - 1;
            this.numLinksInspected = i;
            if (i <= 0) {
                z = true;
            }
            onFinishedInspectingURL(z);
        }
    }

    private void addVideoToList(URLConnection uRLConnection, String str, String str2, String str3) {
        JSONException e;
        URLConnection uRLConnection2 = uRLConnection;
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        String str7 = "fb ";
        String str8 = "instagram ";
        String str9 = "youtube.com";
        String str10 = "content-length";
        try {
            int indexOf;
            StringBuilder stringBuilder;
            String str11;
            boolean z;
            String headerField = uRLConnection2.getHeaderField(str10);
            String headerField2 = uRLConnection2.getHeaderField("Location");
            if (headerField2 == null) {
                headerField2 = uRLConnection.getURL().toString();
            }
            String host = new URL(str4).getHost();
            boolean contains = host.contains("favicon_twitter.com");
            String str12 = "video/mp2t";
            if (contains) {
                if (str6.equals(str12)) {
                    return;
                }
            }
            String str13 = "[AUDIO ONLY]";
            String str14 = "video";
            String str15 = "audio";
            if (str5 == null) {
                str5 = str6.contains(str15) ? str15 : str14;
            } else if (str6.contains(str15)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str13);
                stringBuilder2.append(str5);
                str5 = stringBuilder2.toString();
            }
            String str16 = "facebook.com";
            str2 = str5;
            str5 = "vimeo.com";
            String str17 = headerField;
            headerField = "dailymotion.com";
            String str18 = str12;
            if (!host.contains(str9)) {
                if (!new URL(headerField2).getHost().contains("googlevideo.com")) {
                    if (host.contains(headerField)) {
                        headerField2 = headerField2.replaceAll("(frag\\()+(\\d+)+(\\))", "FRAGMENT");
                        str14 = str2;
                    } else if (!host.contains(str5) || !headerField2.endsWith("m4s")) {
                        MediaMetadataRetriever mediaMetadataRetriever;
                        str9 = "video link: ";
                        headerField = "audio link: ";
                        if (host.contains(str16)) {
                            if (headerField2.contains("bytestart")) {
                                boolean z2;
                                int lastIndexOf = headerField2.lastIndexOf("&bytestart");
                                indexOf = headerField2.indexOf("fbcdn");
                                if (lastIndexOf > 0) {
                                    StringBuilder stringBuilder3 = new StringBuilder();
                                    stringBuilder3.append("https://video.xx.");
                                    stringBuilder3.append(headerField2.substring(indexOf, lastIndexOf));
                                    headerField2 = stringBuilder3.toString();
                                }
                                uRLConnection2 = new URL(headerField2).openConnection();
                                uRLConnection2.connect();
                                str5 = uRLConnection2.getHeaderField(str10);
                                try {
                                    mediaMetadataRetriever = new MediaMetadataRetriever();
                                    mediaMetadataRetriever.setDataSource(headerField2, new HashMap());
                                    mediaMetadataRetriever.release();
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str9);
                                    stringBuilder.append(headerField2);
                                    Log.d(str7, stringBuilder.toString());
                                    z2 = false;
                                } catch (RuntimeException e2) {
                                    Log.d(str7, e2.getMessage());
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(headerField);
                                    stringBuilder.append(headerField2);
                                    Log.d(str7, stringBuilder.toString());
                                    z2 = true;
                                }
                                str14 = str2;
                                contains = z2;
                                str15 = str5;
                                str11 = headerField2;
                                headerField = str16;
                                z = false;
                                int i;
                                switch (str3.hashCode()) {
                                    case -1662384007:
                                        if (str6.equals(str18)) {
                                            i = 2;
                                            break;
                                        }
                                    case -1662095187:
                                        if (str6.equals(MimeTypes.VIDEO_WEBM)) {
                                            i = 1;
                                            break;
                                        }
                                    case 1331848029:
                                        if (str6.equals(MimeTypes.VIDEO_MP4)) {
                                            i = 0;
                                            break;
                                        }
                                    case 1505118770:
                                        if (str6.equals(MimeTypes.AUDIO_WEBM)) {
                                            i = 3;
                                            break;
                                        }
                                    default:
                                }
                            }
                        }
                        if (host.contains("instagram.com")) {
                            try {
                                mediaMetadataRetriever = new MediaMetadataRetriever();
                                mediaMetadataRetriever.setDataSource(headerField2, new HashMap());
                                mediaMetadataRetriever.release();
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str9);
                                stringBuilder.append(headerField2);
                                Log.d(str8, stringBuilder.toString());
                            } catch (RuntimeException e22) {
                                Log.d(str8, e22.getMessage());
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(headerField);
                                stringBuilder.append(headerField2);
                                Log.d(str8, stringBuilder.toString());
                                str14 = str2;
                                str11 = headerField2;
                                str15 = str17;
                                headerField = null;
                                contains = true;
                            }
                        }
                        str14 = str2;
                        str11 = headerField2;
                        str15 = str17;
                        headerField = null;
                        contains = false;
                        z = false;
                        switch (str3.hashCode()) {
                            case -1662384007:
                                break;
                            case -1662095187:
                                break;
                            case 1331848029:
                                break;
                            case 1505118770:
                                break;
                            default:
                                break;
                        }
                    } else {
                        headerField2 = headerField2.replaceAll("(segment-)+(\\d+)", "SEGMENT");
                        str14 = str2;
                        headerField = str5;
                    }
                    str11 = headerField2;
                    str15 = null;
                    contains = false;
                    z = true;
                    switch (str3.hashCode()) {
                        case -1662384007:
                            break;
                        case -1662095187:
                            break;
                        case 1331848029:
                            break;
                        case 1505118770:
                            break;
                        default:
                            break;
                    }
                }
            }
            indexOf = headerField2.lastIndexOf("&range");
            if (indexOf > 0) {
                headerField2 = headerField2.substring(0, indexOf);
            }
            URLConnection openConnection = new URL(headerField2).openConnection();
            openConnection.connect();
            headerField = openConnection.getHeaderField(str10);
            if (host.contains(str9)) {
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("http://www.youtube.com/oembed?url=");
                stringBuilder4.append(str4);
                stringBuilder4.append("&format=json");
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(new URL(stringBuilder4.toString()).openStream(), Charset.defaultCharset());
                    stringBuilder = new StringBuilder();
                    char[] cArr = new char[1024];
                    while (true) {
                        int read = inputStreamReader.read(cArr);
                        if (read == -1) {
                            break;
                        }
                        stringBuilder.append(cArr, 0, read);
                    }
                    str5 = new JSONObject(stringBuilder.toString()).getString("title");
                } catch (JSONException e4) {
                    e = e4;
                    e.printStackTrace();
                    str5 = str2;
                    if (str6.contains(str14)) {
                    }
                    str5 = host;
                    str14 = str5;
                    str15 = headerField;
                    str11 = headerField2;
                    contains = false;
                    z = false;
                    headerField = str9;
                    switch (str3.hashCode()) {
                        case -1662384007:
                            break;
                        case -1662095187:
                            break;
                        case 1331848029:
                            break;
                        case 1505118770:
                            break;
                        default:
                            break;
                    }
                }
                if (str6.contains(str14)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("[VIDEO ONLY]");
                    stringBuilder.append(str5);
                    host = stringBuilder.toString();
                } else if (str6.contains(str15)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str13);
                    stringBuilder.append(str5);
                    host = stringBuilder.toString();
                }
                str5 = host;
            } else {
                str5 = str2;
                str9 = null;
            }
            str14 = str5;
            str15 = headerField;
            str11 = headerField2;
            contains = false;
            z = false;
            headerField = str9;
            switch (str3.hashCode()) {
                case -1662384007:
                    break;
                case -1662095187:
                    break;
                case 1331848029:
                    break;
                case 1505118770:
                    break;
                default:
                    break;
            }
        } catch (IOException unused) {
            Log.e("TAG", "Exception in adding video to list");
        }
    }

    private void addVideosToListFromM3U8(URLConnection uRLConnection, String str, String str2, String str3) {
        String str4 = str3;
        String str5 = "favicon_twitter.com";
        try {
            String str6;
            String str7;
            Boolean valueOf = Boolean.valueOf(false);
            String host = new URL(str).getHost();
            String str8 = "myspace.com";
            String str9 = "VDInfo";
            String str10 = "metacafe.com";
            if (!host.contains(str5)) {
                if (!host.contains(str10)) {
                    if (!host.contains(str8)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Content type is ");
                        stringBuilder.append(str4);
                        stringBuilder.append(" but site is not supported");
                        Log.i(str9, stringBuilder.toString());
                    }
                }
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
            String str11 = str2 != null ? str2 : "video";
            String str12 = "\nsize: null";
            String str13 = "\ntype:";
            String str14 = "\nlink:";
            String str15 = "name:";
            String str16 = null;
            if (host.contains(str5)) {
                str6 = "ts";
                str7 = str12;
                host = str11;
                str12 = str15;
                str11 = str14;
                str16 = str5;
                str5 = str13;
                str13 = "https://video.twimg.com";
            } else if (host.contains(str10)) {
                str5 = uRLConnection.getURL().toString();
                str16 = str5.substring(0, str5.lastIndexOf("/") + 1);
                str6 = "mp4";
                str5 = str13;
                str7 = str12;
                host = str11;
                str13 = str16;
                str16 = str10;
                str12 = str15;
                str11 = str14;
            } else if (host.contains(str8)) {
                str5 = uRLConnection.getURL().toString();
                boolean booleanValue = valueOf.booleanValue();
                String str17 = str15;
                str6 = str14;
                String str18 = str13;
                String str19 = str12;
                host = str11;
                onVideoFound(null, "ts", str5, str11, str, true, "myspace.com", booleanValue);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str17);
                stringBuilder2.append(host);
                stringBuilder2.append(str6);
                stringBuilder2.append(str5);
                stringBuilder2.append(str18);
                stringBuilder2.append(str4);
                stringBuilder2.append(str19);
                Log.i(str9, stringBuilder2.toString());
                return;
            } else {
                str5 = str13;
                str7 = str12;
                host = str11;
                str12 = str15;
                str11 = str14;
                str13 = null;
                str6 = str13;
            }
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String str20;
                BufferedReader bufferedReader2;
                if (readLine.endsWith(".m3u8")) {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(str13);
                    stringBuilder3.append(readLine);
                    str14 = stringBuilder3.toString();
                    String str21 = str14;
                    str20 = str13;
                    String str22 = str12;
                    String str23 = str11;
                    bufferedReader2 = bufferedReader;
                    onVideoFound(null, str6, str14, host, str, true, str16, valueOf.booleanValue());
                    StringBuilder stringBuilder4 = new StringBuilder();
                    str8 = str22;
                    stringBuilder4.append(str8);
                    stringBuilder4.append(host);
                    str10 = str23;
                    stringBuilder4.append(str10);
                    stringBuilder4.append(str21);
                    stringBuilder4.append(str5);
                    stringBuilder4.append(str4);
                    stringBuilder4.append(str7);
                    Log.i(str9, stringBuilder4.toString());
                } else {
                    str20 = str13;
                    str8 = str12;
                    str10 = str11;
                    bufferedReader2 = bufferedReader;
                }
                str12 = str8;
                str11 = str10;
                bufferedReader = bufferedReader2;
                str13 = str20;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
