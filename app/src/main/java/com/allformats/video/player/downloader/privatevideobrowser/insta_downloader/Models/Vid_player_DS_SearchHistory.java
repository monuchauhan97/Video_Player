package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.allformats.video.player.downloader.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vid_player_DS_SearchHistory {
    private List<String> history = new ArrayList();
    private int maxSize;


    public Vid_player_DS_SearchHistory(int i) {
        this.maxSize = i;
    }

    public void saveHistory(Context context) {
        try {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getString(R.string.audio_pref_key_saved_search_history), new Gson().toJson((String[]) this.history.toArray(new String[0]), String[].class)).apply();
        } catch (Exception e) {
            Log.e(getClass().getName(), Log.getStackTraceString(e));
        }
    }

    public void loadHistory(Context context) {
        try {
            this.history.addAll(Arrays.asList((String[]) new Gson().fromJson(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.audio_pref_key_saved_search_history), ""), (Class) String[].class)));
        } catch (Exception e) {
            Log.e(getClass().getName(), Log.getStackTraceString(e));
        }
    }

    public void search(String str) {
        int indexOf = this.history.indexOf(str);
        if (indexOf >= 0) {
            List<String> list = this.history;
            list.add(0, list.remove(indexOf));
        } else {
            this.history.add(0, str);
        }
        if (this.history.size() > this.maxSize) {
            List<String> list2 = this.history;
            list2.remove(list2.size() - 1);
        }
    }

    public List<String> getHistory(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.audio_pref_key_clear_search_history), false)) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(context.getString(R.string.audio_pref_key_clear_search_history), false).apply();
            this.history.clear();
            loadHistory(context);
        }
        return this.history;
    }
}
