package com.allformats.video.player.downloader.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.allformats.video.player.downloader.R;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_PlayerActivity extends BaseActivity {
    public static Activity songPlayerActivity;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_empty_layout);
        bannerAd();
        songPlayerActivity = this;
        Uri parse = Uri.parse(getIntent().getStringExtra("videoUrl"));

        WebView mWebView = (WebView) findViewById(R.id.YoutubeVideoView);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.loadUrl(String.valueOf(parse));
        mWebView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public void onBackPressed() {
        if (AdsUtility.config.adOnBack) {
            showInterstitial(new BaseCallback() {
                @Override
                public void completed() {
                    Vid_player_PlayerActivity.super.onBackPressed();
                }
            });
        } else {
            super.onBackPressed();
        }
    }
}
