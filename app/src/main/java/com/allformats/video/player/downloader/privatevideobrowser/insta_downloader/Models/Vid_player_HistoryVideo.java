package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models;

import com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;

import java.util.ArrayList;


public class Vid_player_HistoryVideo {
    ArrayList<Vid_player_MediaData> videoList;

    public Vid_player_HistoryVideo(ArrayList<Vid_player_MediaData> arrayList) {
        this.videoList = arrayList;
    }

    public ArrayList<Vid_player_MediaData> getVideoList() {
        return this.videoList;
    }

    public void setVideoList(ArrayList<Vid_player_MediaData> arrayList) {
        this.videoList = arrayList;
    }
}
