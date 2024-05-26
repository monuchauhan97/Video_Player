package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.JsonObjectRootModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_ReelDownlodActivity extends BaseActivity {
    public static Activity activity;
    TextView btn_download;
    TextView btn_try_new;
    CardView cardView;
    JsonObjectRootModel finalresponse;
    FrameLayout frameLayout;
    CardView img_card_view;
    ProgressBar pbHeaderProgress;
    ProgressBar progressbar;
    ImageView reelthumbnil;
    String stringobj;
    View view;
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_insta_reel_downlod);
        View inflate = LayoutInflater.from(this).inflate(R.layout.vid_player_progress_dailog, (ViewGroup) null);
        this.view = inflate;
        this.progressbar = (ProgressBar) inflate.findViewById(R.id.progressbar);
        nativeAd();
        findid();
        init();
        onclick();
        ((ImageView) findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_ReelDownlodActivity.this.onBackPressed();
            }
        });
    }

    private void findid() {
        this.reelthumbnil = (ImageView) findViewById(R.id.reelthumbnil);
        this.pbHeaderProgress = (ProgressBar) findViewById(R.id.pbHeaderProgress);
        this.img_card_view = (CardView) findViewById(R.id.img_card_view);
        this.btn_download = (TextView) findViewById(R.id.btn_download);
        this.btn_try_new = (TextView) findViewById(R.id.btn_try_new);
        this.frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
        this.cardView = (CardView) findViewById(R.id.card_view);
    }

    private void init() {
        this.stringobj = getIntent().getStringExtra("FINALRESPONSE");
        this.finalresponse = (JsonObjectRootModel) new Gson().fromJson(this.stringobj, new TypeToken<JsonObjectRootModel>() {
        }.getType());
        Picasso.get().load(this.finalresponse.getThumb()).into(this.reelthumbnil, new Callback() {
            @Override 
            public void onError(Exception exc) {
            }

            @Override
            public void onSuccess() {
                Vid_player_ReelDownlodActivity.this.pbHeaderProgress.setVisibility(View.GONE);
                Vid_player_ReelDownlodActivity.this.img_card_view.setVisibility(View.VISIBLE);
                Vid_player_ReelDownlodActivity.this.btn_download.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onclick() {
        this.btn_try_new.setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View view) {
                Vid_player_ReelDownlodActivity.this.onBackPressed();
            }
        });


        this.btn_download.setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View view) {
                if (Vid_player_ReelDownlodActivity.this.finalresponse.getUrl().get(0).getExt().equals("mp4")) {
                    Vid_player_ReelDownlodActivity vidplayerReelDownlodActivity = Vid_player_ReelDownlodActivity.this;
                    vidplayerReelDownlodActivity.downloading(vidplayerReelDownlodActivity.finalresponse.getUrl().get(0).getUrl(), ".mp4", Environment.DIRECTORY_DOWNLOADS);
                    return;
                }
                Vid_player_ReelDownlodActivity vidplayerReelDownlodActivity2 = Vid_player_ReelDownlodActivity.this;
                vidplayerReelDownlodActivity2.downloading(vidplayerReelDownlodActivity2.finalresponse.getUrl().get(0).getUrl(), ".jpg", Environment.DIRECTORY_PICTURES);
            }
        });
    }

    public void downloading(final String str, final String str2, final String str3) {
        progressdailog();
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
                    FileOutputStream fileOutputStream = new FileOutputStream(file + "/YoInsta_" + Vid_player_ReelDownlodActivity.this.generateFileName() + str2);
                    byte[] bArr = new byte[1024];
                    long j = 0;
                    while (true) {
                        int read = inputStream.read(bArr);
                        this.count = read;
                        if (read != -1) {
                            j += read;
                            Vid_player_ReelDownlodActivity vidplayerReelDownlodActivity = Vid_player_ReelDownlodActivity.this;
                            vidplayerReelDownlodActivity.publishProgress(Integer.valueOf("" + ((int) ((100 * j) / contentLength))));
                            fileOutputStream.write(bArr, 0, this.count);
                        } else {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            inputStream.close();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Vid_player_ReelDownlodActivity.this.hideDialog();
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
        return new SimpleDateFormat("hh_mm_ss").format(new Date());
    }

    public void publishProgress(Integer... numArr) {
        this.progressbar.setProgress(numArr[0].intValue());
    }

    private void progressdailog() {
        AlertDialog show = new AlertDialog.Builder(this).setView(this.view).setCancelable(false).show();
        this.dialog = show;
        show.getWindow().setBackgroundDrawable(new ColorDrawable(0));
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
                    Vid_player_ReelDownlodActivity.super.onBackPressed();
                }
            });
        } else {
            Vid_player_ReelDownlodActivity.super.onBackPressed();
        }
    }
}
