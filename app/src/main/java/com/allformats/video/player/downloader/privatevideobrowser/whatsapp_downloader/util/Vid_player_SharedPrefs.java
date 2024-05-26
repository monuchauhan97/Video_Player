package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Vid_player_SharedPrefs {
    public static final String WA_TREE_URI = "wa_tree_uri";
    public static final String WB_TREE_URI = "wb_tree_uri";
    private static SharedPreferences mPreferences;

    private static SharedPreferences getInstance(Context context) {
        if (mPreferences == null) {
            mPreferences = context.getApplicationContext().getSharedPreferences("wa_data", 0);
        }
        return mPreferences;
    }

    public static void setLang(Context context, String str) {
        getInstance(context).edit().putString("language", str).apply();
    }

    public static String getLang(Context context) {
        return getInstance(context).getString("language", "en");
    }

    public static void setWATree(Context context, String str) {
        getInstance(context).edit().putString(WA_TREE_URI, str).apply();
    }

    public static String getWATree(Context context) {
        return getInstance(context).getString(WA_TREE_URI, "");
    }

    public static void setWBTree(Context context, String str) {
        getInstance(context).edit().putString(WB_TREE_URI, str).apply();
    }

    public static String getWBTree(Context context) {
        return getInstance(context).getString(WB_TREE_URI, "");
    }
}
