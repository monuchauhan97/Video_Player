package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Fragment;

import androidx.documentfile.provider.DocumentFile;

import java.util.Comparator;

public final class Vid_player_RecentWapp_BusloadDataAsync implements Comparator {
    public static final Vid_player_RecentWapp_BusloadDataAsync INSTANCE = new Vid_player_RecentWapp_BusloadDataAsync();

    private Vid_player_RecentWapp_BusloadDataAsync() {
    }

    @Override 
    public final int compare(Object obj, Object obj2) {
        return Long.compare(((DocumentFile) obj2).lastModified(), ((DocumentFile) obj).lastModified());
    }
}
