package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Adapter.Vid_player_DownloadMediaAdapter;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.JsonArrayRootModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;
import plugin.adsdk.service.NativeAdsAdapter;

public class Vid_player_PhotosVideoDownloadActivity extends BaseActivity {
    public static Activity activity;
    TextView btn_download;
    TextView btn_download_all;
    TextView btn_try_new;
    CardView cardView;
    AlertDialog dialog;
    Vid_player_DownloadMediaAdapter downloaded_media_adapter;
    FrameLayout frameLayout;
    RecyclerView image_recyclerview;
    ProgressBar progressbar;
    String stringobj;
    View view;
    List<JsonArrayRootModel> downloadlist = new ArrayList();
    List<JsonArrayRootModel> finalresponse = new ArrayList();


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_insta_photos_video);
        View inflate = LayoutInflater.from(this).inflate(R.layout.vid_player_progress_dailog, (ViewGroup) null);
        this.view = inflate;
        this.progressbar = (ProgressBar) inflate.findViewById(R.id.progressbar);
        findid();
        init();
        onclick();

    }

    private void findid() {
        this.image_recyclerview = (RecyclerView) findViewById(R.id.image_recyclerview);
        this.btn_download_all = (TextView) findViewById(R.id.btn_download_all);
        this.btn_download = (TextView) findViewById(R.id.btn_download);
        this.btn_try_new = (TextView) findViewById(R.id.btn_try_new);
        this.frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
        this.cardView = (CardView) findViewById(R.id.card_view);
        ((ImageView) findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void init() {
        this.stringobj = getIntent().getStringExtra("FINALRESPONSE");
        List<JsonArrayRootModel> list = (List) new Gson().fromJson(this.stringobj, new TypeToken<List<JsonArrayRootModel>>() {
        }.getType());
        this.finalresponse = list;

        this.downloaded_media_adapter = new Vid_player_DownloadMediaAdapter(list, this, AdsUtility.config.listNativeCount);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = downloaded_media_adapter.getItemViewType(position);
                if (itemViewType == NativeAdsAdapter.AD) {
                    return 2;
                }
                return 1;
            }
        });
        this.image_recyclerview.setLayoutManager(layoutManager);
        this.image_recyclerview.setAdapter(this.downloaded_media_adapter);
    }

    private void onclick() {
        this.btn_try_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.downloaded_media_adapter.setOnClickListener(new Vid_player_DownloadMediaAdapter.OnClickListener() {
            @Override
            public void onClick(List<JsonArrayRootModel> list) {
                Log.i("TAG", "onClick: ");
                Vid_player_PhotosVideoDownloadActivity.this.downloadlist = list;
                if (Vid_player_PhotosVideoDownloadActivity.this.downloadlist.size() == 0) {
                    Vid_player_PhotosVideoDownloadActivity.this.btn_download_all.setVisibility(View.VISIBLE);
                    Vid_player_PhotosVideoDownloadActivity.this.btn_download.setVisibility(View.GONE);
                    return;
                }
                Vid_player_PhotosVideoDownloadActivity.this.btn_download_all.setVisibility(View.GONE);
                Vid_player_PhotosVideoDownloadActivity.this.btn_download.setVisibility(View.VISIBLE);
            }
        });
        this.btn_download_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_PhotosVideoDownloadActivity.this.progressdailog();
                for (int i = 0; i < Vid_player_PhotosVideoDownloadActivity.this.finalresponse.size(); i++) {
                    if (Vid_player_PhotosVideoDownloadActivity.this.finalresponse.get(i).getUrl().get(0).getExt().equals("mp4")) {
                        Vid_player_PhotosVideoDownloadActivity vidplayerPhotosVideoDownloadActivity = Vid_player_PhotosVideoDownloadActivity.this;
                        vidplayerPhotosVideoDownloadActivity.downloading(vidplayerPhotosVideoDownloadActivity.finalresponse.get(i).getUrl().get(0).getUrl(), ".mp4", Environment.DIRECTORY_DOWNLOADS);
                    } else {
                        Vid_player_PhotosVideoDownloadActivity vidplayerPhotosVideoDownloadActivity2 = Vid_player_PhotosVideoDownloadActivity.this;
                        vidplayerPhotosVideoDownloadActivity2.downloading(vidplayerPhotosVideoDownloadActivity2.finalresponse.get(i).getUrl().get(0).getUrl(), ".jpg", Environment.DIRECTORY_PICTURES);
                    }
                }
            }
        });
        this.btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_PhotosVideoDownloadActivity.this.progressdailog();
                for (int i = 0; i < Vid_player_PhotosVideoDownloadActivity.this.downloadlist.size(); i++) {
                    if (Vid_player_PhotosVideoDownloadActivity.this.downloadlist.get(i).getUrl().get(0).getExt().equals("mp4")) {
                        Vid_player_PhotosVideoDownloadActivity vidplayerPhotosVideoDownloadActivity = Vid_player_PhotosVideoDownloadActivity.this;
                        vidplayerPhotosVideoDownloadActivity.downloading(vidplayerPhotosVideoDownloadActivity.downloadlist.get(i).getUrl().get(0).getUrl(), ".mp4", Environment.DIRECTORY_DOWNLOADS);
                    } else {
                        Vid_player_PhotosVideoDownloadActivity vidplayerPhotosVideoDownloadActivity2 = Vid_player_PhotosVideoDownloadActivity.this;
                        vidplayerPhotosVideoDownloadActivity2.downloading(vidplayerPhotosVideoDownloadActivity2.downloadlist.get(i).getUrl().get(0).getUrl(), ".jpg", Environment.DIRECTORY_PICTURES);
                    }
                }
            }
        });
    }

    public void downloading(final String str, final String str2, final String str3) {
        ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        newSingleThreadScheduledExecutor.execute(new Runnable() {
            int count;

            @Override
            public void run() {
                try {
                    URLConnection openConnection = new URL(str).openConnection();
                    openConnection.connect();
                    int contentLength = openConnection.getContentLength();
                    InputStream inputStream = openConnection.getInputStream();
                    File file = new File(Environment.getExternalStoragePublicDirectory(str3).getAbsolutePath() + File.separator + "YoInsta_");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(file + "/YoInsta_" + Vid_player_PhotosVideoDownloadActivity.this.generateFileName() + str2);
                    byte[] bArr = new byte[1024];
                    long j = 0;
                    while (true) {
                        int read = inputStream.read(bArr);
                        this.count = read;
                        if (read != -1) {
                            j += read;
                            Vid_player_PhotosVideoDownloadActivity vidplayerPhotosVideoDownloadActivity = Vid_player_PhotosVideoDownloadActivity.this;
                            vidplayerPhotosVideoDownloadActivity.publishProgress(Integer.valueOf("" + ((int) ((100 * j) / contentLength))));
                            fileOutputStream.write(bArr, 0, this.count);
                        } else {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            inputStream.close();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Vid_player_PhotosVideoDownloadActivity.this.hideDialog();
                                    onBackPressed();
                                }
                            });
                            return;
                        }
                    }
                } catch (Exception unused) {
                }
            }
        });
    }

    public String generateFileName() {
        return String.valueOf(System.nanoTime());
    }

    public void publishProgress(Integer... numArr) {
        this.progressbar.setProgress(numArr[0].intValue());
    }

    public void progressdailog() {
        if (this.dialog == null) {
            AlertDialog show = new AlertDialog.Builder(this).setView(this.view).setCancelable(false).show();
            this.dialog = show;
            show.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    public void hideDialog() {
        AlertDialog alertDialog = this.dialog;
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                this.dialog.dismiss();
            }
            this.dialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (AdsUtility.config.adOnBack) {
            showInterstitial(new BaseCallback() {
                @Override
                public void completed() {
                    Vid_player_PhotosVideoDownloadActivity.super.onBackPressed();
                }
            });
        } else {
            Vid_player_PhotosVideoDownloadActivity.super.onBackPressed();
        }
    }
}
