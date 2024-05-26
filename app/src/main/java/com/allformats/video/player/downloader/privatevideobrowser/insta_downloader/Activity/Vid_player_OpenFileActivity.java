package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.video_player.Activity.Vid_player_VideoPlayerActivity;
import com.bumptech.glide.Glide;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Adapter.Vid_player_HorizontalGalleryAdapter;

import java.io.File;
import java.util.ArrayList;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_OpenFileActivity extends BaseActivity {
    public static int DELETE_REQUEST_CODE = 1003;
    public String removableLists = new String();
    LinearLayout btn_delete;
    TextView btn_share;
    CardView cardView;
    ImageView currentiamgeview;
    int currentpossition;
    VideoView currentvideoview;
    String data;
    ArrayList<String> filelist;
    FrameLayout frameLayout;
    Vid_player_HorizontalGalleryAdapter vidplayerHorizontalGalleryAdapter;
    RecyclerView recyclerview;
    ArrayList<Uri> removableList = new ArrayList<>();
    ActivityResultLauncher<IntentSenderRequest> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback() {
        @Override
        public final void onActivityResult(Object obj) {
            Vid_player_OpenFileActivity.this.m45lambda$new$0$commicronstarsyoinstaActivityOpenFileActivity((ActivityResult) obj);
        }
    });
    private AlertDialog dialog;

    public void m45lambda$new$0$commicronstarsyoinstaActivityOpenFileActivity(ActivityResult activityResult) {
        int i;
        Log.i("TAG", "onActivityResult: ");
        if (activityResult == null || activityResult.getResultCode() != -1) {
            Log.i("TAG", "onActivityResult: can not delete");
            return;
        }
        for (int i2 = 0; i2 < this.filelist.size(); i2++) {
            if (this.filelist.get(i2).equalsIgnoreCase(this.removableLists)) {
                this.filelist.remove(i2);
                this.vidplayerHorizontalGalleryAdapter.notifyDataSetChanged();
                if (this.filelist.size() == 0) {
                } else if (this.currentpossition > this.filelist.size() || (i = this.currentpossition) == 0) {
                    int i3 = this.currentpossition;
                    if (i3 == 0) {
                        if (this.filelist.get(i3).endsWith(".mp4")) {
                            this.currentiamgeview.setVisibility(View.GONE);
                            this.currentvideoview.setVisibility(View.VISIBLE);
                            this.currentvideoview.setVideoPath(this.filelist.get(this.currentpossition));
                            this.currentvideoview.start();
                        } else {
                            this.currentiamgeview.setVisibility(View.VISIBLE);
                            this.currentvideoview.setVisibility(View.GONE);
                            Glide.with(this).load(this.filelist.get(this.currentpossition)).into(this.currentiamgeview);
                        }
                    }
                } else {
                    int i4 = i - 1;
                    this.currentpossition = i4;
                    if (this.filelist.get(i4).endsWith(".mp4")) {
                        this.currentiamgeview.setVisibility(View.GONE);
                        this.currentvideoview.setVisibility(View.VISIBLE);
                        this.currentvideoview.setVideoPath(this.filelist.get(this.currentpossition));
                        this.currentvideoview.start();
                    } else {
                        this.currentiamgeview.setVisibility(View.VISIBLE);
                        this.currentvideoview.setVisibility(View.GONE);
                        Glide.with(this).load(this.filelist.get(this.currentpossition)).into(this.currentiamgeview);
                    }
                }
            }
        }
        Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
        Log.i("TAG", "onActivityResult: deleted");
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_insta_open_file);
        bannerAd();
        findid();
        init();
        click();
    }

    private void findid() {
        this.recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        this.currentiamgeview = (ImageView) findViewById(R.id.currentiamgeview);
        this.currentvideoview = (VideoView) findViewById(R.id.currentvideoview);
        this.btn_share = (TextView) findViewById(R.id.btn_share);
        this.btn_delete = (LinearLayout) findViewById(R.id.btn_delete);
        this.frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
        this.cardView = (CardView) findViewById(R.id.card_view);
        ((ImageView) findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_OpenFileActivity.this.onBackPressed();
            }
        });
    }

    private void init() {
        this.currentpossition = getIntent().getIntExtra("CURRENTPOSSITION", -1);
        ArrayList<String> stringArrayList = getIntent().getExtras().getStringArrayList("FILELIST");
        this.filelist = stringArrayList;
        this.vidplayerHorizontalGalleryAdapter = new Vid_player_HorizontalGalleryAdapter(stringArrayList, this, AdsUtility.config.listNativeCount);
        this.recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerview.setAdapter(this.vidplayerHorizontalGalleryAdapter);
        this.recyclerview.scrollToPosition(this.currentpossition - 1);
        if (this.filelist.get(this.currentpossition).endsWith(".mp4")) {
            this.currentiamgeview.setVisibility(View.GONE);
            this.currentvideoview.setVisibility(View.VISIBLE);
            this.currentvideoview.setVideoPath(this.filelist.get(this.currentpossition));
            this.currentvideoview.start();
            return;
        }
        this.currentiamgeview.setVisibility(View.VISIBLE);
        this.currentvideoview.setVisibility(View.GONE);
        Glide.with(this).load(this.filelist.get(this.currentpossition)).into(this.currentiamgeview);
    }

    private void click() {
        this.currentvideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        this.vidplayerHorizontalGalleryAdapter.setOnClickListener(new Vid_player_HorizontalGalleryAdapter.OnClickListener() {
            @Override
            public void onClick(int i) {
                Vid_player_OpenFileActivity.this.currentpossition = i;
                if (Vid_player_OpenFileActivity.this.filelist.get(i).endsWith(".mp4")) {
                    Vid_player_OpenFileActivity.this.currentiamgeview.setVisibility(View.GONE);
                    Vid_player_OpenFileActivity.this.currentvideoview.setVisibility(View.VISIBLE);
                    Vid_player_OpenFileActivity.this.currentvideoview.setVideoPath(Vid_player_OpenFileActivity.this.filelist.get(i));
                    Vid_player_OpenFileActivity.this.currentvideoview.start();
                    return;
                }
                Vid_player_OpenFileActivity.this.currentiamgeview.setVisibility(View.VISIBLE);
                Vid_player_OpenFileActivity.this.currentvideoview.setVisibility(View.GONE);
                Glide.with(Vid_player_OpenFileActivity.this).load(Vid_player_OpenFileActivity.this.filelist.get(i)).into(Vid_player_OpenFileActivity.this.currentiamgeview);
            }
        });
        this.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Vid_player_OpenFileActivity vidplayerOpenFileActivity = Vid_player_OpenFileActivity.this;
                    MediaScannerConnection.scanFile(vidplayerOpenFileActivity, new String[]{vidplayerOpenFileActivity.filelist.get(Vid_player_OpenFileActivity.this.currentpossition)}, null, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Activity.Vid_player_OpenFileActivity.5.1
                        @Override
                        public void onScanCompleted(String str, Uri uri) {

                            Intent intent = new Intent("android.intent.action.SEND");
                            intent.setType("video_downshot/*");
                            intent.putExtra("android.intent.extra.STREAM", Uri.parse(str));
                            intent.putExtra("android.intent.extra.TEXT", "");
                            intent.putExtra("android.intent.extra.SUBJECT", "vid");
                            Vid_player_OpenFileActivity.this.startActivity(Intent.createChooser(intent, "Share Video"));
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 30) {
                    Vid_player_OpenFileActivity vidplayerOpenFileActivity = Vid_player_OpenFileActivity.this;
                    vidplayerOpenFileActivity.delete(vidplayerOpenFileActivity.filelist.get(Vid_player_OpenFileActivity.this.currentpossition));
                    return;
                }
                Vid_player_OpenFileActivity vidplayerOpenFileActivity2 = Vid_player_OpenFileActivity.this;
                vidplayerOpenFileActivity2.deletin10(vidplayerOpenFileActivity2.filelist.get(Vid_player_OpenFileActivity.this.currentpossition));
            }
        });
    }

    private void hideDialog() {
        AlertDialog alertDialog = this.dialog;
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                this.dialog.dismiss();
            }
            this.dialog = null;
        }
    }

    public void deletin10(final String str) {
        final File file = new File(str);
        hideDialog();
        View inflate = LayoutInflater.from(this).inflate(R.layout.vid_player_custom_layout, (ViewGroup) null);

        ((TextView) inflate.findViewById(R.id.negative)).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                hideDialog();
            }
        });
        ((TextView) inflate.findViewById(R.id.positive)).setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_OpenFileActivity.this.m44x1396bef4(file, str, view);
            }
        });
        AlertDialog show = new AlertDialog.Builder(this).setView(inflate).setCancelable(false).show();
        this.dialog = show;
        show.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    public void m44x1396bef4(File file, String str, View view) {
        hideDialog();
        File file1 = new File(str);
        if (file1.delete()) {
            for (int i = 0; i < this.filelist.size(); i++) {
                if (this.filelist.get(i).equalsIgnoreCase(str)) {
                    this.filelist.remove(i);
                    this.vidplayerHorizontalGalleryAdapter.notifyItemRemoved(i);
                    Toast.makeText(Vid_player_OpenFileActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            }
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String str2, Uri uri) {
                    Toast.makeText(Vid_player_OpenFileActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            vidplayerHorizontalGalleryAdapter.notifyDataSetChanged();
        }
    }

    public void delete(String str) {
        this.removableList.add(Uri.parse(str));
        this.removableLists = str;
        final ArrayList arrayList = new ArrayList();
        MediaScannerConnection.scanFile(this, new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String str2, Uri uri) {
                PendingIntent pendingIntent;
                arrayList.add(uri);
                if (Build.VERSION.SDK_INT >= 30) {
                    pendingIntent = MediaStore.createDeleteRequest(Vid_player_OpenFileActivity.this.getContentResolver(), arrayList);
                    Vid_player_OpenFileActivity.this.resultLauncher.launch(new IntentSenderRequest.Builder(pendingIntent).build());
                } else {
                    pendingIntent = null;
                }
                try {
                    vidplayerHorizontalGalleryAdapter.notifyItemRemoved(currentpossition);
                    Vid_player_OpenFileActivity.this.startIntentSenderForResult(pendingIntent.getIntentSender(), Vid_player_OpenFileActivity.DELETE_REQUEST_CODE, null, 0, 0, 0, null);
                    vidplayerHorizontalGalleryAdapter.notifyDataSetChanged();
                } catch (Exception unused) {
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.filelist.get(this.currentpossition).endsWith(".mp4")) {
            this.currentvideoview.start();
        }
        this.vidplayerHorizontalGalleryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (AdsUtility.config.adOnBack) {
            showInterstitial(new BaseCallback() {
                @Override
                public void completed() {
                    Vid_player_OpenFileActivity.super.onBackPressed();
                }
            });
        } else {
            Vid_player_OpenFileActivity.super.onBackPressed();
        }
    }
}
