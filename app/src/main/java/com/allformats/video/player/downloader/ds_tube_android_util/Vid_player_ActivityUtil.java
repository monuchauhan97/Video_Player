package com.allformats.video.player.downloader.ds_tube_android_util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class Vid_player_ActivityUtil {
    public static void hideKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(final Context context, final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 1);
            }
        });
    }

    public static void hideStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(1024, 1024);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(4);
        }
    }
}
