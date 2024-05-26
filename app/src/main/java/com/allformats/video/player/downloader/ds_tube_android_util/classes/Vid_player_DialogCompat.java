package com.allformats.video.player.downloader.ds_tube_android_util.classes;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;


public class Vid_player_DialogCompat extends AppCompatDialog {
    private Activity activity;

    public Vid_player_DialogCompat(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public Vid_player_DialogCompat(Activity activity, int i) {
        super(activity, i);
        this.activity = activity;
    }

    protected Vid_player_DialogCompat(Activity activity, boolean z, OnCancelListener onCancelListener) {
        super(activity, z, onCancelListener);
        this.activity = activity;
    }

    @Override
    public void show() {
        if (!isShowing() && check()) {
            try {
                super.show();
            } catch (WindowManager.BadTokenException | IllegalArgumentException | IllegalStateException e) {
                Log.e(getClass().getName(), Log.getStackTraceString(e));
            }
        }
    }

    @Override
    public void dismiss() {
        if (isShowing() && check()) {
            try {
                super.dismiss();
            } catch (WindowManager.BadTokenException | IllegalArgumentException | IllegalStateException e) {
                Log.e(getClass().getName(), Log.getStackTraceString(e));
            }
        }
    }

    private boolean check() {
        boolean z = !this.activity.isFinishing();
        return Build.VERSION.SDK_INT >= 17 ? z && !this.activity.isDestroyed() : z;
    }

    public Activity getActivity() {
        return this.activity;
    }
}
