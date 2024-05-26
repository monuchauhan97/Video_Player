package com.allformats.video.player.downloader.privatevideobrowser;

import android.util.Patterns;
import android.widget.EditText;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpHost;


public class Vid_player_WebConnect {
    private Vid_player_MainBrowserActivity activity;
    private EditText textBox;

    public Vid_player_WebConnect(EditText editText, Vid_player_MainBrowserActivity vidplayerMainBrowserActivity) {
        this.textBox = editText;
        this.activity = vidplayerMainBrowserActivity;
    }

    public void connect() {
        String obj = this.textBox.getText().toString();
        if (Patterns.WEB_URL.matcher(obj).matches()) {
            if (!obj.startsWith(HttpHost.DEFAULT_SCHEME_NAME)) {
                obj = "http://" + obj;
            }
            this.activity.getVideoBrowserManagerFragment().newWindow(obj);
            return;
        }
        this.activity.getVideoBrowserManagerFragment().newWindow("https://google.com/search?q=" + obj);
    }
}
