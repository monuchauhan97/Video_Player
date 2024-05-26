package com.allformats.video.player.downloader.ds_tube_android_util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;


public class Vid_player_IntentUtil {
    public static void disableDeathOnFileExposure() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }
        }
    }
    public static void intentGooglePlay(Context context, String str) {
        try {
            context.startActivity(googlePlayIntent(context, "market://details", str));
        } catch (ActivityNotFoundException unused) {
            context.startActivity(googlePlayIntent(context, "https://play.google.com/store/apps/details", str));
        }
    }

    private static Intent googlePlayIntent(Context context, String str, String str2) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(String.format("%s?id=%s", str, str2)));
        int i = Build.VERSION.SDK_INT;
        intent.addFlags(!(context instanceof Activity) ? 1476919296 : 1208483840);
        return intent;
    }

}
