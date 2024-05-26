package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models;


public class Url {
    private String ext;
    private String name;
    private String type;
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getExt() {
        return this.ext;
    }

    public void setExt(String str) {
        this.ext = str;
    }
}
