package com.allformats.video.player.downloader.privatevideobrowser.saveFragment.lists;

import android.content.Context;

import com.allformats.video.player.downloader.privatevideobrowser.saveFragment.Vid_player_DownloadVideo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vid_player_InactiveDownloadsData implements Serializable {
    private List<Vid_player_DownloadVideo> inactiveDownloads = new ArrayList();

    public void add(Context context, Vid_player_DownloadVideo vidplayerDownloadVideo) {
        this.inactiveDownloads.add(vidplayerDownloadVideo);
        save(context);
    }

    public static Vid_player_InactiveDownloadsData load(Context context) {
        Vid_player_InactiveDownloadsData vidplayerInactiveDownloadsData;
        Exception e;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        File file = new File(context.getFilesDir(), "inactive.dat");
        Vid_player_InactiveDownloadsData vidplayerInactiveDownloadsData2 = new Vid_player_InactiveDownloadsData();
        if (!file.exists()) {
            return vidplayerInactiveDownloadsData2;
        }
        try {
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            vidplayerInactiveDownloadsData = (Vid_player_InactiveDownloadsData) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e2) {
            e = e2;
            vidplayerInactiveDownloadsData = vidplayerInactiveDownloadsData2;
        }
        try {
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return vidplayerInactiveDownloadsData;
        }
        return vidplayerInactiveDownloadsData;
    }

    public void save(Context context) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getFilesDir(), "inactive.dat"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
