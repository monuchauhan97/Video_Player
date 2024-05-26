package com.allformats.video.player.downloader.ds_tube_android_util.classes;

import android.os.Build;
import android.util.Log;

import plugin.adsdk.service.BaseActivity;


public class Vid_player_ActivityCompat extends BaseActivity {
    public void runOnUiThreadCompat(Runnable runnable) {
        if (check()) {
            try {
                runOnUiThread(runnable);
            } catch (IllegalArgumentException | IllegalStateException e) {
                Log.e(getClass().getName(), Log.getStackTraceString(e));
            }
        }
    }

    private boolean check() {
        boolean z = !isFinishing();
        return Build.VERSION.SDK_INT >= 17 ? z && !isDestroyed() : z;
    }
}
