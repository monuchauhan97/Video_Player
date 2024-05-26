package com.allformats.video.player.downloader.view;


public class Vid_player_ModelClass {
    String str_name;
    String str_path;

    public Vid_player_ModelClass(String str, String str2) {
        this.str_name = str;
        this.str_path = str2;
    }

    public String getStr_name() {
        return this.str_name;
    }

    public void setStr_name(String str) {
        this.str_name = str;
    }

    public String getStr_path() {
        return this.str_path;
    }

    public void setStr_path(String str) {
        this.str_path = str;
    }
}
