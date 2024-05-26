package com.allformats.video.player.downloader.privatevideobrowser.webFragment;

import static com.allformats.video.player.downloader.privatevideobrowser.Vid_player_MainBrowserActivity.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.privatevideobrowser.Vid_player_DS_BrowserFragment;
import com.allformats.video.player.downloader.privatevideobrowser.Vid_player_MainBrowserActivity;
import com.allformats.video.player.downloader.privatevideobrowser.calcyVideoView.Vid_player_CustomMediaController;
import com.allformats.video.player.downloader.privatevideobrowser.calcyVideoView.Vid_player_CustomVideoView;
import com.allformats.video.player.downloader.privatevideobrowser.historyFragment.Vid_player_HistorySQLite;
import com.allformats.video.player.downloader.privatevideobrowser.historyFragment.Vid_player_VisitedPage;
import com.allformats.video.player.downloader.privatevideobrowser.utils.Vid_player_Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import plugin.adsdk.service.BaseActivity;

public class Vid_player_VideoBrowserWindowFragmentVidplayer extends Vid_player_DS_BrowserFragment implements Vid_player_MainBrowserActivity.OnBackPressedListener {

    private List<String> blockedWebsites;
    private SSLSocketFactory defaultSSLSF;
    private boolean loadedFirsTime;
    private ProgressBar loadingPageProgress;
    private Vid_player_CustomMediaController mediaFoundController;
    private boolean moved = false;
    private int orientation;
    private Vid_player_TouchableWebView page;
    private String url;
    private FrameLayout videoFoundTV;
    private Vid_player_CustomVideoView videoFoundView;
    private Vid_player_VideoListData vidplayerVideoListData;
    private FloatingActionButton videosFoundHUD;
    private View view;
    BottomSheetDialog bottomSheetDialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.url = getArguments().getString("url");
        this.defaultSSLSF = HttpsURLConnection.getDefaultSSLSocketFactory();
        this.blockedWebsites = Arrays.asList(getResources().getStringArray(R.array.blocked_sites));
        setRetainInstance(true);
    }

    private void createVideosFoundTV() {
        this.videoFoundTV = (FrameLayout) this.view.findViewById(R.id.video_Found_TV);
        this.videoFoundView = (Vid_player_CustomVideoView) this.view.findViewById(R.id.video_Found_View);
        Vid_player_CustomMediaController vidplayerCustomMediaController = (Vid_player_CustomMediaController) this.view.findViewById(R.id.media_Found_Controller);
        this.mediaFoundController = vidplayerCustomMediaController;
        vidplayerCustomMediaController.setFullscreenEnabled(false);
        this.videoFoundView.setMediaController(this.mediaFoundController);
        this.videoFoundTV.setVisibility(View.GONE);
    }

    public void createFoundVideosWindow() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        this.bottomSheetDialog = bottomSheetDialog;
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        Vid_player_VideoListData videoListData = this.vidplayerVideoListData;
        if (videoListData != null) {
            videoListData.recreateVideoList((RecyclerView) this.bottomSheetDialog.findViewById(R.id.videoList));
        } else {
            this.vidplayerVideoListData = new Vid_player_VideoListData(((BaseActivity) getActivity()), (RecyclerView) this.bottomSheetDialog.findViewById(R.id.videoList)) {
                @Override
                void onItemDeleted() {
                    Vid_player_VideoBrowserWindowFragmentVidplayer.this.updateFoundVideosBar();
                }
            };
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            int visibility = view != null ? view.getVisibility() : 0;
            View inflate = layoutInflater.inflate(R.layout.vid_player_vid_player_activity_ds_browse_browser, viewGroup, false);
            this.view = inflate;
            inflate.setVisibility(visibility);
            if (this.page == null) {
                this.page = (Vid_player_TouchableWebView) this.view.findViewById(R.id.page);
            } else {
                ((ViewGroup) this.view).removeView(this.view.findViewById(R.id.page));
                ((ViewGroup) this.page.getParent()).removeView(this.page);
                ((ViewGroup) this.view).addView(this.page);
                View view2 = this.view;
            }
            ProgressBar progressBar = (ProgressBar) this.view.findViewById(R.id.loading_Page_Progress);
            this.loadingPageProgress = progressBar;
            progressBar.setVisibility(View.GONE);
            createVideosFoundTV();
            createFoundVideosWindow();
            updateFoundVideosBar();

        return this.view;
    }

    @Override
    public void onViewCreated(final View view, Bundle bundle) {
        if (!this.loadedFirsTime) {
            WebSettings settings = this.page.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            this.page.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                    if (!Vid_player_VideoBrowserWindowFragmentVidplayer.this.blockedWebsites.contains(Vid_player_Utils.getBaseDomain(webResourceRequest.getUrl().toString()))) {
                        return super.shouldOverrideUrlLoading(webView, webResourceRequest);
                    }
                    Log.d("vdd", "URL : " + webResourceRequest.getUrl());
                    return super.shouldOverrideUrlLoading(webView, webResourceRequest);
                }

                @Override
                public void onPageStarted(WebView webView, final String str, Bitmap bitmap) {
                   Activity activity1= activity ;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            EditText editText = (EditText) activity1.findViewById(R.id.et_search_bar);
                            editText.setText(str);
                            editText.setSelection(editText.getText().length());
                            Vid_player_VideoBrowserWindowFragmentVidplayer.this.url = str;
                        }
                    });
                    view.findViewById(R.id.loading_Progress).setVisibility(View.GONE);
                    Vid_player_VideoBrowserWindowFragmentVidplayer.this.loadingPageProgress.setVisibility(View.VISIBLE);
                    super.onPageStarted(webView, str, bitmap);
                }

                @Override
                public void onPageFinished(WebView webView, String str) {
                    super.onPageFinished(webView, str);
                    Vid_player_VideoBrowserWindowFragmentVidplayer.this.loadingPageProgress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadResource(WebView webView, String str) {
                    Log.d("fb :", "URL: " + str);
                    new Vid_player_ThreadVideoContentSearch(Vid_player_VideoBrowserWindowFragmentVidplayer.this.getActivity(), str, webView.getUrl(), webView.getTitle()) {
                        @Override
                        public void onStartInspectingURL() {
                            Vid_player_Utils.disableSSLCertificateChecking();
                        }

                        @Override
                        public void onFinishedInspectingURL(boolean z) {
                            HttpsURLConnection.setDefaultSSLSocketFactory(Vid_player_VideoBrowserWindowFragmentVidplayer.this.defaultSSLSF);
                        }

                        @Override
                        public void onVideoFound(String str2, String str3, String str4, String str5, String str6, boolean z, String str7, boolean z2) {
                            Vid_player_VideoBrowserWindowFragmentVidplayer.this.vidplayerVideoListData.addItem(str2, str3, str4, str5, str6, z, str7, z2);
                            Vid_player_VideoBrowserWindowFragmentVidplayer.this.updateFoundVideosBar();
                        }
                    }.start();
                }

                @Override
                public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
                    if (Vid_player_VideoBrowserWindowFragmentVidplayer.this.getActivity() != null) {
                        Log.d("VDDebug", "Url: " + str);
                        if (Vid_player_VideoBrowserWindowFragmentVidplayer.this.getActivity().getSharedPreferences("activity_ds_browse_settings", 0).getBoolean("adBlockON", true) && ((str.contains("ad") || str.contains("banner") || str.contains("pop")) && Vid_player_VideoBrowserWindowFragmentVidplayer.this.getVDActivity().getVideoBrowserManagerFragment().checkUrlIfAds(str))) {
                            Log.d("VDDebug", "Ads detected: " + str);
                            return new WebResourceResponse(null, null, null);
                        }
                    }
                    return super.shouldInterceptRequest(webView, str);
                }

                @Override
                public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                    if (Build.VERSION.SDK_INT < 21 || Vid_player_VideoBrowserWindowFragmentVidplayer.this.getVDActivity() == null) {
                        return shouldInterceptRequest(webView, webResourceRequest.getUrl().toString());
                    }
                    if (!Vid_player_DS_Helper.getInstance().getSharedPreferences("activity_ds_browse_settings", 0).getBoolean("adBlockON", true) || ((!webResourceRequest.getUrl().toString().contains("ad") && !webResourceRequest.getUrl().toString().contains("banner") && !webResourceRequest.getUrl().toString().contains("pop")) || !Vid_player_VideoBrowserWindowFragmentVidplayer.this.getVDActivity().getVideoBrowserManagerFragment().checkUrlIfAds(webResourceRequest.getUrl().toString()))) {
                        return null;
                    }
                    Log.i("VDInfo", "Ads detected: " + webResourceRequest.getUrl().toString());
                    return new WebResourceResponse(null, null, null);
                }
            });
            this.page.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView webView, int i) {
                    Vid_player_VideoBrowserWindowFragmentVidplayer.this.loadingPageProgress.setProgress(i);
                }

                @Override
                public void onReceivedTitle(WebView webView, String str) {
                    super.onReceivedTitle(webView, str);
                    Vid_player_VideoBrowserWindowFragmentVidplayer.this.vidplayerVideoListData.deleteAllItems();
                    Vid_player_VideoBrowserWindowFragmentVidplayer.this.updateFoundVideosBar();
                    Vid_player_VisitedPage vidplayerVisitedPage = new Vid_player_VisitedPage();
                    vidplayerVisitedPage.title = str;
                    vidplayerVisitedPage.link = webView.getUrl();
                    new Vid_player_HistorySQLite(activity).addPageToHistory(vidplayerVisitedPage);
                }
            });
            this.page.loadUrl(this.url);
            this.loadedFirsTime = true;
            return;
        }
        EditText editText = (EditText) getVDActivity().findViewById(R.id.et_search_bar);
        editText.setText(this.url);
        editText.setSelection(editText.getText().length());
    }


    public void updateFoundVideosBar() {
        if (getActivity() == null) {
            Log.d("debug", "No activity found");
            return;
        }
        Log.d("debug", "Activity found");
        if (this.vidplayerVideoListData.getSize() > 0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Vid_player_VideoBrowserWindowFragmentVidplayer.this.videosFoundHUD.setBackgroundTintList(Vid_player_VideoBrowserWindowFragmentVidplayer.this.getResources().getColorStateList(R.color.colorAccent));
                    Vid_player_VideoBrowserWindowFragmentVidplayer.this.videosFoundHUD.setEnabled(true);
                    Vid_player_VideoBrowserWindowFragmentVidplayer.this.videosFoundHUD.startAnimation(AnimationUtils.loadAnimation(Vid_player_VideoBrowserWindowFragmentVidplayer.this.getActivity().getApplicationContext(), R.anim.expand_in));
                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    public void updateVideoPlayer(String str) {
        this.videoFoundTV.setVisibility(View.VISIBLE);
        Uri parse = Uri.parse(str);
        Log.d("debug", str);
        this.videoFoundView.setVideoURI(parse);
        this.videoFoundView.start();
    }

    @Override
    public void onBackpressed() {
        if (this.videoFoundView.isPlaying() || this.videoFoundTV.getVisibility() == View.VISIBLE) {
            this.videoFoundView.closePlayer();
            this.videoFoundTV.setVisibility(View.GONE);
        } else if (this.page.canGoBack()) {
            this.page.goBack();
        } else {
            getVDActivity().getVideoBrowserManagerFragment().closeWindow(this);
        }
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.page.onPause();
        Log.d("debug", "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        this.page.onResume();
        Log.d("debug", "onResume: ");
    }
}
