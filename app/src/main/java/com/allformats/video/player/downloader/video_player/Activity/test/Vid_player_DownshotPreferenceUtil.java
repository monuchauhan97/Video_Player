package com.allformats.video.player.downloader.video_player.Activity.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Vid_player_DownshotPreferenceUtil {
    static final String LAST_BRIGHTNESS = "last_brightness";
    static final String LAST_SPEED = "last_speed";
    static final String LOCK = "video_lock";
    static final String SORT_ORDER = "sort_order";
    static final String THEME = "theme";
    static final String VIEW_TYPE = "view_type";
    static SharedPreferences mPreferences;
    static Vid_player_DownshotPreferenceUtil sInstance;

    Vid_player_DownshotPreferenceUtil(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Vid_player_DownshotPreferenceUtil getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Vid_player_DownshotPreferenceUtil(context.getApplicationContext());
        }
        return sInstance;
    }

    public void saveLastBrightness(float f) {
        mPreferences.edit().putFloat(LAST_BRIGHTNESS, f).commit();
    }

    public float getLastBrightness() {
        return mPreferences.getFloat(LAST_BRIGHTNESS, 0.5f);
    }

    public void saveLastSpeed(float f) {
        mPreferences.edit().putFloat(LAST_SPEED, f).commit();
    }

    public float getLastSpeed() {
        return mPreferences.getFloat(LAST_SPEED, 1.0f);
    }

    public static void saveSortOrder(int i) {
        mPreferences.edit().putInt(SORT_ORDER, i).commit();
    }

    public static int getSortOrder() {
        return mPreferences.getInt(SORT_ORDER, 0);
    }

    public void saveViewType(Boolean bool) {
        mPreferences.edit().putBoolean(VIEW_TYPE, bool.booleanValue()).commit();
    }

    public static boolean getViewType() {
        return mPreferences.getBoolean(VIEW_TYPE, true);
    }

    public void setTheme(int i) {
        mPreferences.edit().putInt(THEME, i).commit();
    }

    public int getTheme() {
        return mPreferences.getInt(THEME, 11);
    }

    public void setLock(Boolean bool) {
        mPreferences.edit().putBoolean(LOCK, bool.booleanValue()).commit();
    }

    public boolean getLock() {
        return mPreferences.getBoolean(LOCK, false);
    }
}
