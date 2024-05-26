package com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.whatsapp_downloader.Adapter.Vid_player_Utils;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_VideoPlayerActivity extends BaseActivity {

    public static Activity activity;
    ImageView backIV;
    VideoView displayVV;
    String videoPath;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_video_player);
        getWindow().setFlags(1024, 1024);
        ImageView imageView = (ImageView) findViewById(R.id.backIV);
        this.backIV = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.videoPath = Vid_player_Utils.mPath;
        VideoView videoView = (VideoView) findViewById(R.id.displayVV);
        this.displayVV = videoView;
        videoView.setVideoPath(this.videoPath);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(this.displayVV);
        this.displayVV.setMediaController(mediaController);
        this.displayVV.start();
    }

    @Override
    public void onBackPressed() {
        if (AdsUtility.config.adOnBack) {
            showInterstitial(new BaseCallback() {
                @Override
                public void completed() {
                    Vid_player_VideoPlayerActivity.super.onBackPressed();
                }
            });
        } else {
            Vid_player_VideoPlayerActivity.super.onBackPressed();
        }
    }
}
