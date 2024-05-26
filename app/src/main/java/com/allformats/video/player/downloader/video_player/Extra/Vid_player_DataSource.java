package com.allformats.video.player.downloader.video_player.Extra;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class Vid_player_DataSource {
    public static final String URLKEYDEFAULT = "URL_KEY_DEFAULT";
    public int currentUrlIndex;
    public HashMap<String, String> headerMap;
    public boolean looping;
    public Object[] objects;
    public String title;
    public LinkedHashMap urlsMap;

    public Vid_player_DataSource(String str) {
        this.headerMap = new HashMap<>();
        this.looping = false;
        this.title = "";
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        this.urlsMap = linkedHashMap;
        linkedHashMap.put(URLKEYDEFAULT, str);
        this.currentUrlIndex = 0;
    }

    public Vid_player_DataSource(String str, String str2) {
        this.headerMap = new HashMap<>();
        this.looping = false;
        this.title = "";
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        this.urlsMap = linkedHashMap;
        linkedHashMap.put(URLKEYDEFAULT, str);
        this.title = str2;
        this.currentUrlIndex = 0;
    }

    public Vid_player_DataSource(Object obj) {
        this.headerMap = new HashMap<>();
        this.looping = false;
        this.title = "";
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        this.urlsMap = linkedHashMap;
        linkedHashMap.put(URLKEYDEFAULT, obj);
        this.currentUrlIndex = 0;
    }

    public Vid_player_DataSource(LinkedHashMap linkedHashMap) {
        this.headerMap = new HashMap<>();
        this.looping = false;
        this.title = "";
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        this.urlsMap = linkedHashMap2;
        linkedHashMap2.clear();
        this.urlsMap.putAll(linkedHashMap);
        this.currentUrlIndex = 0;
    }

    public Vid_player_DataSource(LinkedHashMap linkedHashMap, String str) {
        this.headerMap = new HashMap<>();
        this.looping = false;
        this.title = "";
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        this.urlsMap = linkedHashMap2;
        linkedHashMap2.clear();
        this.urlsMap.putAll(linkedHashMap);
        this.title = str;
        this.currentUrlIndex = 0;
    }

    public Object getCurrentUrl() {
        return getValueFromLinkedMap(this.currentUrlIndex);
    }

    public Object getCurrentKey() {
        return getKeyFromDataSource(this.currentUrlIndex);
    }

    public String getKeyFromDataSource(int i) {
        int i2 = 0;
        for (Object obj : this.urlsMap.keySet()) {
            if (i2 == i) {
                return obj.toString();
            }
            i2++;
        }
        return null;
    }

    public Object getValueFromLinkedMap(int i) {
        int i2 = 0;
        for (Object obj : this.urlsMap.keySet()) {
            if (i2 == i) {
                return this.urlsMap.get(obj);
            }
            i2++;
        }
        return null;
    }

    public boolean containsTheUrl(Object obj) {
        if (obj != null) {
            return this.urlsMap.containsValue(obj);
        }
        return false;
    }

    public Vid_player_DataSource cloneMe() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.putAll(this.urlsMap);
        return new Vid_player_DataSource(linkedHashMap, this.title);
    }
}
