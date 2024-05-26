package com.allformats.video.player.downloader.privatevideobrowser.saveFragment.lists;

import android.content.Context;
import android.os.Environment;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.R;
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


public class Vid_player_DownloadQueuesData implements Serializable {
    private List<Vid_player_DownloadVideo> downloads = new ArrayList();

    public static Vid_player_DownloadQueuesData load(Context context) {
        Vid_player_DownloadQueuesData vidplayerDownloadQueuesData;
        Exception e;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        File file = new File(context.getFilesDir(), "activity_ds_browse_downloads.dat");
        Vid_player_DownloadQueuesData vidplayerDownloadQueuesData2 = new Vid_player_DownloadQueuesData();
        if (!file.exists()) {
            return vidplayerDownloadQueuesData2;
        }
        try {
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            vidplayerDownloadQueuesData = (Vid_player_DownloadQueuesData) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e2) {
            e = e2;
            vidplayerDownloadQueuesData = vidplayerDownloadQueuesData2;
        }
        try {
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return vidplayerDownloadQueuesData;
        }
        return vidplayerDownloadQueuesData;
    }

    public void save(Context context) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getFilesDir(), "activity_ds_browse_downloads.dat"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertToTop(String str, String str2, String str3, String str4, String str5, boolean z, String str6) {
        String validName = getValidName(str4, str2);
        Vid_player_DownloadVideo vidplayerDownloadVideo = new Vid_player_DownloadVideo();
        vidplayerDownloadVideo.link = str3;
        vidplayerDownloadVideo.name = validName;
        vidplayerDownloadVideo.page = str5;
        vidplayerDownloadVideo.size = str;
        vidplayerDownloadVideo.type = str2;
        vidplayerDownloadVideo.chunked = z;
        vidplayerDownloadVideo.website = str6;
        this.downloads.add(0, vidplayerDownloadVideo);
    }

    public void add(String str, String str2, String str3, String str4, String str5, boolean z, String str6) {
        String validName = getValidName(str4, str2);
        Vid_player_DownloadVideo vidplayerDownloadVideo = new Vid_player_DownloadVideo();
        vidplayerDownloadVideo.link = str3;
        vidplayerDownloadVideo.name = validName;
        vidplayerDownloadVideo.page = str5;
        vidplayerDownloadVideo.size = str;
        vidplayerDownloadVideo.type = str2;
        vidplayerDownloadVideo.chunked = z;
        vidplayerDownloadVideo.website = str6;
        this.downloads.add(vidplayerDownloadVideo);
    }

    private String getValidName(String str, String str2) {
        String trim = str.replaceAll("[^\\w ()'!\\[\\]\\-]", "").trim();
        int i = 0;
        if (trim.length() > 127) {
            trim = trim.substring(0, 127);
        } else if (trim.equals("")) {
            trim = "video";
        }
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Vid_player_DS_Helper.getInstance().getApplicationContext().getString(R.string.app_name));
        File file = new File(externalStoragePublicDirectory, trim + "." + str2);
        StringBuilder sb = new StringBuilder(trim);
        while (file.exists()) {
            i++;
            sb = new StringBuilder(trim);
            sb.append(" ");
            sb.append(i);
            File externalStoragePublicDirectory2 = Environment.getExternalStoragePublicDirectory(Vid_player_DS_Helper.getInstance().getApplicationContext().getString(R.string.app_name));
            file = new File(externalStoragePublicDirectory2, ((Object) sb) + "." + str2);
        }
        while (nameAlreadyExists(sb.toString())) {
            i++;
            sb = new StringBuilder(trim);
            sb.append(" ");
            sb.append(i);
        }
        return sb.toString();
    }

    public List<Vid_player_DownloadVideo> getList() {
        return this.downloads;
    }

    public Vid_player_DownloadVideo getTopVideo() {
        if (this.downloads.size() > 0) {
            return this.downloads.get(0);
        }
        return null;
    }

    public void deleteTopVideo(Context context) {
        if (this.downloads.size() > 0) {
            this.downloads.remove(0);
            save(context);
        }
    }

    private boolean nameAlreadyExists(String str) {
        for (Vid_player_DownloadVideo vidplayerDownloadVideo : this.downloads) {
            if (vidplayerDownloadVideo.name.equals(str)) {
                return true;
            }
        }
        return false;
    }
}
