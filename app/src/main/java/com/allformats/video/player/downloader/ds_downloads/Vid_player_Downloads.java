package com.allformats.video.player.downloader.ds_downloads;

import android.util.SparseArray;

import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_DownloadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Vid_player_Downloads {
    private List<Vid_player_DownloadTask> downloadsList = new ArrayList();
    private SparseArray<Vid_player_DownloadTask> downloadsSparse = new SparseArray<>();

    public void add(int i, Vid_player_DownloadTask vidplayerDownloadTask) {
        this.downloadsSparse.append(i, vidplayerDownloadTask);
        this.downloadsList.add(vidplayerDownloadTask);
    }

    public void remove(int i) {
        this.downloadsSparse.remove(i);
        this.downloadsList.remove(this.downloadsSparse.get(i));
    }

    public boolean containsFile(File file) {
        for (Vid_player_DownloadTask vidplayerDownloadTask : this.downloadsList) {
            if (vidplayerDownloadTask != null && vidplayerDownloadTask.getFile().equals(file)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return this.downloadsList.size();
    }

    public Vid_player_DownloadTask get(int i) {
        return this.downloadsSparse.get(i);
    }

    public boolean isEmpty() {
        return this.downloadsSparse.size() == 0;
    }
}
