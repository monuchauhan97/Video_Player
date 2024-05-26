package com.allformats.video.player.downloader.video_player.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_Folder;
import com.allformats.video.player.downloader.video_player.Adapter.Vid_player_VideoAdapter;
import com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;

import java.util.ArrayList;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_VideolistActivity extends BaseActivity implements View.OnClickListener {
    public static Activity activity;
    public static ImageView ivnodata;
    public static RecyclerView videolist;
    private ImageView backfolder;
    private Vid_player_Folder mediadata;
    private ProgressBar progress;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_videolist);
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            activeNetworkInfo.isConnected();
        }
        this.mediadata = (Vid_player_Folder) getIntent().getSerializableExtra("data");
        initView();
        initListener();
    }

    private void initListener() {
        this.backfolder.setOnClickListener(this);
    }

    private void initView() {
        videolist = (RecyclerView) findViewById(R.id.video_list);
        this.progress = (ProgressBar) findViewById(R.id.progress);
        this.backfolder = (ImageView) findViewById(R.id.back_folder);
        ivnodata = (ImageView) findViewById(R.id.iv_nodata);
        ((TextView) findViewById(R.id.folder_name)).setText(this.mediadata.getName());
        initAdapter(this.mediadata.getMedia_data(), Vid_player_DS_Helper.getViewBy());
    }

    private void initAdapter(ArrayList<Vid_player_MediaData> arrayList, int i) {
        this.progress.setVisibility(View.GONE);
        if (arrayList.size() > 0) {
            videolist.setVisibility(View.VISIBLE);
            ivnodata.setVisibility(View.GONE);
            Vid_player_VideoAdapter vidplayerVideoAdapter = new Vid_player_VideoAdapter(this, arrayList, i, 1, AdsUtility.config.listNativeCount);
            videolist.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            videolist.setAdapter(vidplayerVideoAdapter);
            return;
        }
        videolist.setVisibility(View.GONE);
        ivnodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_folder) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (AdsUtility.config.adOnBack) {
                showInterstitial(new BaseCallback() {
                    @Override
                    public void completed() {
                        Vid_player_VideolistActivity.super.onBackPressed();
                    }
                });
        } else {
            Vid_player_VideolistActivity.super.onBackPressed();
        }
    }

    public void myStartActivity(Intent intent) {
        showInterstitial(intent);
    }
}
