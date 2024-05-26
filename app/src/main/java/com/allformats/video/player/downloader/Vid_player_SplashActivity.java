package com.allformats.video.player.downloader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.allformats.video.player.downloader.video_player.Activity.Vid_player_HomeActivity;

import io.michaelrocks.paranoid.Obfuscate;
import plugin.adsdk.extras.BaseLauncherActivity;

@SuppressLint("CustomSplashScreen")
@Obfuscate
public class Vid_player_SplashActivity extends BaseLauncherActivity {
    public static final String BASE_URL = "https://my.adstrunk.com/";
    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

//    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
//    public static String[] storge_permissions_33 = {
//            Manifest.permission.READ_MEDIA_IMAGES,
//            Manifest.permission.READ_MEDIA_AUDIO,
//            Manifest.permission.READ_MEDIA_VIDEO,
//    };


    public Vid_player_SplashActivity() {
        super(R.layout.vid_player_activity_splash, plugin.adsdk.R.layout.ad_activity_extra_dashboard);
    }

    @Override
    protected Intent destinationIntent() {
        return new Intent(this, Vid_player_HomeActivity.class);
    }

    @Override
    protected String extraAppContentText() {
        return getString(plugin.adsdk.R.string.app_content);
    }

    @Override
    protected int extraAppContentImage() {
        return R.mipmap.ic_launcher_round;
    }

    @Override
    protected String[] permissions() {
//        String[] p;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            p = storge_permissions_33;
//        } else {
//            p = storge_permissions;
//        }
        return storge_permissions;
    }

    @Override
    protected String baseURL() {
        return BASE_URL;
    }


}