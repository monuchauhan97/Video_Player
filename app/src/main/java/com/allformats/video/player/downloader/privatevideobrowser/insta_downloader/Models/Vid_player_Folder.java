package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models;

import com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;

import java.io.Serializable;
import java.util.ArrayList;


public class Vid_player_Folder implements Serializable {
    ArrayList<Vid_player_MediaData> mediadata;
    String name;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public ArrayList<Vid_player_MediaData> getMedia_data() {
        return this.mediadata;
    }

    public void setMedia_data(ArrayList<Vid_player_MediaData> arrayList) {
        this.mediadata = arrayList;
    }
}
