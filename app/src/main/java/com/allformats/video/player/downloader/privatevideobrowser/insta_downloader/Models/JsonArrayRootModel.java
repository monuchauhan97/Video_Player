package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models;

import java.util.ArrayList;
import java.util.List;

public class JsonArrayRootModel {
    private List<Url> url = new ArrayList();

    public List<Url> getUrl() {
        return this.url;
    }

    public void setUrl(List<Url> list) {
        this.url = list;
    }
}