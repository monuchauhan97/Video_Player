package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models;

import java.util.ArrayList;
import java.util.List;


public class JsonObjectRootModel {
    private Object hd;
    private String hosting;
    private Meta meta;
    private Object sd;
    private String thumb;
    private Integer timestamp;
    private List<Url> url = new ArrayList();

    public List<Url> getUrl() {
        return this.url;
    }

    public void setUrl(List<Url> list) {
        this.url = list;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getThumb() {
        return this.thumb;
    }

    public void setThumb(String str) {
        this.thumb = str;
    }

    public Object getSd() {
        return this.sd;
    }

    public void setSd(Object obj) {
        this.sd = obj;
    }

    public String getHosting() {
        return this.hosting;
    }

    public void setHosting(String str) {
        this.hosting = str;
    }

    public Object getHd() {
        return this.hd;
    }

    public void setHd(Object obj) {
        this.hd = obj;
    }

    public Integer getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Integer num) {
        this.timestamp = num;
    }
}
