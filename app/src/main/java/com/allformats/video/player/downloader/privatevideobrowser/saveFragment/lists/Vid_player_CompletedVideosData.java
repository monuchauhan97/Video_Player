package com.allformats.video.player.downloader.privatevideobrowser.saveFragment.lists;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vid_player_CompletedVideosData implements Serializable {
    private List<String> videos = new ArrayList();

    public void addVideo(Context context, String str) {
        this.videos.add(0, str);
        save(context);
    }

    public static Vid_player_CompletedVideosData load(Context context) {
        Exception e;
        Vid_player_CompletedVideosData vidplayerCompletedVideosData = new Vid_player_CompletedVideosData();
        File file = new File(context.getFilesDir(), "completed.dat");
        Log.d("surabhi", "load: " + context.getFilesDir());
        if (!file.exists()) {
            return vidplayerCompletedVideosData;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Vid_player_CompletedVideosData vidplayerCompletedVideosData2 = (Vid_player_CompletedVideosData) objectInputStream.readObject();
            try {
                objectInputStream.close();
                fileInputStream.close();
                return vidplayerCompletedVideosData2;
            } catch (IOException e2) {
                e = e2;
                vidplayerCompletedVideosData = vidplayerCompletedVideosData2;
                e.printStackTrace();
                return vidplayerCompletedVideosData;
            }
        } catch (IOException e3) {
            e = e3;
        } catch (ClassNotFoundException e4) {
            e = e4;
        }
        return vidplayerCompletedVideosData;
    }

    public void save(Context context) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getFilesDir(), "completed.dat"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
