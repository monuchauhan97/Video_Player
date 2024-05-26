package com.allformats.video.player.downloader.ds_tube_android_util.classes;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


public class Vid_player_ToastCompat {
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_SHORT = 0;

    
    public static boolean checkContext(Context context) {
        return context != null;
    }

    public static void showText(final Context context, final String str, final int i) {
        if (checkContext(context)) {
            if (!(context instanceof Activity)) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override 
                    public void run() {
                        try {
                            if (Vid_player_ToastCompat.checkContext(context)) {
                                Toast.makeText(context, str, i).show();
                            }
                        } catch (WindowManager.BadTokenException | IllegalArgumentException | IllegalStateException e) {
                            Log.e(Vid_player_ToastCompat.class.getName(), Log.getStackTraceString(e));
                        }
                    }
                });
            } else if (checkActivity(context)) {
                try {
                    Toast.makeText(context, str, i).show();
                } catch (WindowManager.BadTokenException | IllegalArgumentException | IllegalStateException e) {
                    Log.e(Vid_player_ToastCompat.class.getName(), Log.getStackTraceString(e));
                }
            }
        }
    }

    private static boolean checkActivity(Context context) {
        Activity activity = (Activity) context;
        boolean z = !activity.isFinishing();
        return Build.VERSION.SDK_INT >= 17 ? z && !activity.isDestroyed() : z;
    }
}
