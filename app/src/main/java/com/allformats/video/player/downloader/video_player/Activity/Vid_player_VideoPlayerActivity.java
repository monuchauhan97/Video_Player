package com.allformats.video.player.downloader.video_player.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.video_player.Activity.test.Vid_player_DownshotOnSwipeTouchListener;
import com.allformats.video.player.downloader.video_player.Activity.test.Vid_player_DownshotPreferenceUtil;
import com.allformats.video.player.downloader.video_player.Activity.test.Vid_player_ListAdapter;
import com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;
import com.allformats.video.player.downloader.video_player.Util.Vid_player_Constant;
import com.appizona.yehiahd.fastsave.BuildConfig;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import plugin.adsdk.service.AdsUtility;
import plugin.adsdk.service.BaseActivity;
import plugin.adsdk.service.BaseCallback;

public class Vid_player_VideoPlayerActivity extends BaseActivity {

    static ArrayList<Vid_player_MediaData> list = new ArrayList<>();

    public static Intent getIntent(Context context, ArrayList<Vid_player_MediaData> arrayList, int i) {
        Intent intent = new Intent(context, Vid_player_VideoPlayerActivity.class);
        intent.putExtra(Vid_player_Constant.EXTRA_VIDEO_LIST, arrayList);
        intent.putExtra(Vid_player_Constant.EXTRA_VIDEO_POSITION, i);
        return intent;
    }

    public static Intent getInstance(Context context, int i, boolean z) {
        Intent intent = new Intent(context, Vid_player_VideoPlayerActivity.class);
        intent.putExtra(Vid_player_Constant.EXTRA_FLOATING_VIDEO, i);
        intent.putExtra(Vid_player_Constant.EXTRA_IS_FLOATING_VIDEO, z);
        return intent;
    }

    public static Intent getIntent(Context context, ArrayList<Vid_player_MediaData> arrayList, int i, boolean z) {
        Intent intent = new Intent(context, Vid_player_VideoPlayerActivity.class);
        intent.putExtra(Vid_player_Constant.EXTRA_VIDEO_LIST, arrayList);
        intent.putExtra(Vid_player_Constant.EXTRA_VIDEO_POSITION, i);
        intent.putExtra(Vid_player_Constant.EXTRA_BACKGROUND_VIDEO_PLAY_POSITION, z);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            getWindow().setFlags(1024, 1024);
        }
    }

    private float brightnessv;
    public AudioManager audioManager;
    private float speedv;
    private PlayerView playerView;
    public int position;
    private long current;

    public SimpleExoPlayer player;
    private MediaSource videoSource;
    public WindowManager.LayoutParams attributes;
    ImageView lock;
    private ImageView back;
    private ImageView crop;
    private ImageView playlist;
    private ImageView pspeed;
    private ImageView repeat;
    private ImageView rotate;
    private ImageView share;
    private ImageView unlock;
    private ImageView volume;
    public BottomSheetDialog bottomSheetDialog;
    private TextView playlist_txt;
    public TextView title;
    private int view = 0;
    public static boolean PAUSE_ALL = false;
    public TextView tvolume;
    private SeekBar vseekBar;
    private TextView dspeed;
    private Boolean repeatstatus = false;
    private int width;
    private Boolean lockstatus = false;
    public static void setarray(List<Vid_player_MediaData> list2) {
        list = (ArrayList<Vid_player_MediaData>) list2;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        supportRequestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.vid_player_video_activity_videoplayer);
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(displayMetrics);
        this.brightnessv = Vid_player_DownshotPreferenceUtil.getInstance(this).getLastBrightness();
        this.audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.speedv = Vid_player_DownshotPreferenceUtil.getInstance(this).getLastSpeed();
        this.playerView = (PlayerView) findViewById(R.id.player_view);
        this.playerView.setFastForwardIncrementMs(10000);
        this.playerView.setRewindIncrementMs(10000);
        this.position = getIntent().getIntExtra(Vid_player_Constant.EXTRA_VIDEO_POSITION, 0);
        this.current = getIntent().getLongExtra(Vid_player_Constant.EXTRA_FLOATING_VIDEO, 0L);
        this.player = ExoPlayerFactory.newSimpleInstance(this);
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, BuildConfig.APPLICATION_ID));
        List<Vid_player_MediaData> list2 = list;
        if (list2 != null) {
            int size = list2.size();
            MediaSource[] mediaSourceArr = new MediaSource[size];
            for (int i = 0; i < list.size(); i++) {
                mediaSourceArr[i] = new ExtractorMediaSource.Factory(defaultDataSourceFactory).createMediaSource(Uri.parse(list.get(i).getPath()/*.get(i).DATA*/));
            }
            this.videoSource = size == 1 ? mediaSourceArr[0] : new ConcatenatingMediaSource(mediaSourceArr);
        } else {
            this.videoSource = new ExtractorMediaSource.Factory(defaultDataSourceFactory).createMediaSource(Uri.parse(getIntent().getDataString()));
        }
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        this.attributes = attributes;
        attributes.screenBrightness = this.brightnessv;
        getWindow().setAttributes(this.attributes);
        PlaybackParameters playbackParameters = new PlaybackParameters(this.speedv);
        this.player.setPlaybackParameters(playbackParameters);
        this.playerView.setPlayer(this.player);
        this.player.prepare(this.videoSource);
        this.player.setPlayWhenReady(true);
        this.player.seekTo(this.position, this.current);
        this.rotate = (ImageView) findViewById(R.id.rotate);
        this.lock = (ImageView) findViewById(R.id.lock);
        this.unlock = (ImageView) findViewById(R.id.unlock);
        this.crop = (ImageView) findViewById(R.id.exo_crop);
        this.back = (ImageView) findViewById(R.id.back);
        this.share = (ImageView) findViewById(R.id.share);
        this.volume = (ImageView) findViewById(R.id.exo_volume);
        this.pspeed = (ImageView) findViewById(R.id.pspeed);
        this.repeat = (ImageView) findViewById(R.id.repeat);
        this.playlist = (ImageView) findViewById(R.id.playlist);

        this.bottomSheetDialog = new BottomSheetDialog(this,R.style.CustomBottomSheetDialogTheme);
        View inflate = getLayoutInflater().inflate(R.layout.vid_player_playlist_sheet, (ViewGroup) null);

        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.r1);
        this.playlist_txt = (TextView) inflate.findViewById(R.id.playlist_txt);
        if (this.audioManager.getStreamVolume(3) == 0) {
            this.volume.setImageResource(R.drawable.volume_mute_icon);
        } else {
            this.volume.setImageResource(R.drawable.volume);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.getLayoutManager().scrollToPosition(this.position);
        recyclerView.setAdapter(new Vid_player_ListAdapter(this, list, this.player));
        this.bottomSheetDialog.setContentView(inflate);
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_VideoPlayerActivity.this.onBackPressed();
            }
        });
        this.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_VideoPlayerActivity.this.player.setPlayWhenReady(false);
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("video_downshot/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.parse(Vid_player_VideoPlayerActivity.list.get(Vid_player_VideoPlayerActivity.this.position).getPath()));
                intent.putExtra("android.intent.extra.TEXT", "");
                intent.putExtra("android.intent.extra.SUBJECT", "vid");
                Vid_player_VideoPlayerActivity.this.startActivity(Intent.createChooser(intent, "Share Video"));
            }
        });

        this.lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Vid_player_VideoPlayerActivity.this.lock();
            }
        });
        this.unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Vid_player_VideoPlayerActivity.this.unlock();
            }
        });
        this.crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                if (Vid_player_VideoPlayerActivity.this.view == 0) {
                    Vid_player_VideoPlayerActivity.this.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    Vid_player_VideoPlayerActivity.this.crop.setImageResource(R.drawable.zoom);
                    Vid_player_VideoPlayerActivity.this.view = 3;
                } else if (Vid_player_VideoPlayerActivity.this.view == 3) {
                    Vid_player_VideoPlayerActivity.this.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
                    Vid_player_VideoPlayerActivity.this.crop.setImageResource(R.drawable.video_fit);
                    Vid_player_VideoPlayerActivity.this.view = 4;
                } else if (Vid_player_VideoPlayerActivity.this.view == 4) {
                    Vid_player_VideoPlayerActivity.this.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    Vid_player_VideoPlayerActivity.this.crop.setImageResource(R.drawable.video_full);
                    Vid_player_VideoPlayerActivity.this.view = 0;
                }
            }
        });
        this.volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                BottomSheetDialog builder = new BottomSheetDialog(Vid_player_VideoPlayerActivity.this, R.style.CustomBottomSheetDialogTheme);
                View inflate2 = LayoutInflater.from(Vid_player_VideoPlayerActivity.this).inflate(R.layout.vid_player_video_dialog_volume, (ViewGroup) null);
                Vid_player_VideoPlayerActivity.this.tvolume = (TextView) inflate2.findViewById(R.id.progress);
                Vid_player_VideoPlayerActivity.this.vseekBar = (SeekBar) inflate2.findViewById(R.id.seekBar);
                int streamVolume = Vid_player_VideoPlayerActivity.this.audioManager.getStreamVolume(3);
                Vid_player_VideoPlayerActivity.this.vseekBar.setMax(Vid_player_VideoPlayerActivity.this.audioManager.getStreamMaxVolume(3));
                Vid_player_VideoPlayerActivity.this.vseekBar.setProgress(streamVolume);
                if (streamVolume == 0) {
                   Vid_player_VideoPlayerActivity.this.volume.setImageResource(R.drawable.volume_mute_icon);
                } else {
                    Vid_player_VideoPlayerActivity.this.volume.setImageResource(R.drawable.volume);
                }
                Vid_player_VideoPlayerActivity.this.tvolume.setText(Integer.toString(streamVolume));
                Vid_player_VideoPlayerActivity.this.vseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                        seekBar.setProgress(i2);
                        Vid_player_VideoPlayerActivity.this.tvolume.setText(Integer.toString(i2));
                        Vid_player_VideoPlayerActivity.this.audioManager.setStreamVolume(3, i2, 0);
                        if (i2 == 0) {
                            Vid_player_VideoPlayerActivity.this.volume.setImageResource(R.drawable.volume_mute_icon);
                        } else {
                           Vid_player_VideoPlayerActivity.this.volume.setImageResource(R.drawable.volume);
                        }
                        Vid_player_VideoPlayerActivity.this.getWindow().getDecorView().setSystemUiVisibility(4102);
                    }
                });
                builder.setContentView(inflate2);
                builder.show();
            }
        });

        this.pspeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
               BottomSheetDialog builder = new BottomSheetDialog(Vid_player_VideoPlayerActivity.this, R.style.CustomBottomSheetDialogTheme);
                View inflate2 = LayoutInflater.from(Vid_player_VideoPlayerActivity.this).inflate(R.layout.vid_player_playback_dialog, (ViewGroup) null);
                Vid_player_VideoPlayerActivity.this.dspeed = (TextView) inflate2.findViewById(R.id.dspeed);

                final AtomicInteger atomicInteger = new AtomicInteger((int) (Vid_player_DownshotPreferenceUtil.getInstance(Vid_player_VideoPlayerActivity.this).getLastSpeed() * 100.0f));

                Vid_player_VideoPlayerActivity.this.dspeed.setText(Integer.toString(atomicInteger.get()));
                ((ImageView) inflate2.findViewById(R.id.sdown)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public final void onClick(View view2) {
                        if (atomicInteger.get() <= 100) {
                            AtomicInteger atomicInteger2 = atomicInteger;
                            atomicInteger2.set(atomicInteger2.get() - 10);
                            if (atomicInteger.get() < 24) {
                                AtomicInteger atomicInteger3 = atomicInteger;
                                atomicInteger3.set(atomicInteger3.get() + 10);
                            }
                        } else {
                            AtomicInteger atomicInteger4 = atomicInteger;
                            atomicInteger4.set(atomicInteger4.get() - 10);
                        }
                        Vid_player_VideoPlayerActivity.this.setSpeed(atomicInteger.get());
                        Vid_player_VideoPlayerActivity.this.getWindow().getDecorView().setSystemUiVisibility(4102);
                    }
                });
                ((ImageView) inflate2.findViewById(R.id.sup)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public final void onClick(View view2) {
                        if (atomicInteger.get() < 100) {
                            AtomicInteger atomicInteger2 = atomicInteger;
                            atomicInteger2.set(atomicInteger2.get() + 10);
                        } else {
                            AtomicInteger atomicInteger3 = atomicInteger;
                            atomicInteger3.set(atomicInteger3.get() + 10);
                            if (atomicInteger.get() > 401) {
                                AtomicInteger atomicInteger4 = atomicInteger;
                                atomicInteger4.set(atomicInteger4.get() - 10);
                            }
                        }
                        Vid_player_VideoPlayerActivity.this.setSpeed(atomicInteger.get());
                        Vid_player_VideoPlayerActivity.this.getWindow().getDecorView().setSystemUiVisibility(4102);
                    }
                });
                builder.setContentView(inflate2);
                builder.show();
                Vid_player_VideoPlayerActivity.this.getWindow().getDecorView().setSystemUiVisibility(4102);
            }
        });
        this.repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                if (!Vid_player_VideoPlayerActivity.this.repeatstatus.booleanValue()) {
                    Vid_player_VideoPlayerActivity.this.repeat.setImageResource(R.drawable.repeatone);
                    Vid_player_VideoPlayerActivity.this.player.setRepeatMode(Player.REPEAT_MODE_ONE);
                    Vid_player_VideoPlayerActivity downshotVidplayerVideoPlayerActivity = Vid_player_VideoPlayerActivity.this;
                    downshotVidplayerVideoPlayerActivity.repeatstatus = Boolean.valueOf(true ^ downshotVidplayerVideoPlayerActivity.repeatstatus.booleanValue());
                    return;
                }
                Vid_player_VideoPlayerActivity.this.repeat.setImageResource(R.drawable.repeat);
                Vid_player_VideoPlayerActivity.this.player.setRepeatMode(Player.REPEAT_MODE_OFF);
                Vid_player_VideoPlayerActivity downshotVidplayerVideoPlayerActivity2 = Vid_player_VideoPlayerActivity.this;
                downshotVidplayerVideoPlayerActivity2.repeatstatus = Boolean.valueOf(true ^ downshotVidplayerVideoPlayerActivity2.repeatstatus.booleanValue());
            }
        });
        this.title = (TextView) findViewById(R.id.title);
        if (getResources().getConfiguration().orientation == 1) {
            this.width = displayMetrics.widthPixels;
            titlepot();
        } else {
            this.width = displayMetrics.heightPixels;
            titleland();
        }
        List<Vid_player_MediaData> list3 = list;
        if (list3 != null) {
            this.title.setText(list3.get(this.position).getName());
        } else {
            this.title.setText(uri2filename());
        }
        this.rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                if (Vid_player_VideoPlayerActivity.this.getResources().getConfiguration().orientation == 1) {
                    Vid_player_VideoPlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    Vid_player_VideoPlayerActivity.this.getWindow().getDecorView().setSystemUiVisibility(4102);
                    return;
                }
                Vid_player_VideoPlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
        this.player.addListener(new Player.EventListener() {

            @Override
            public void onPlayerError(ExoPlaybackException exoPlaybackException) {
            }

            @Override
            public void onRepeatModeChanged(int i2) {
            }

            @Override
            public void onSeekProcessed() {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean z) {
            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object obj, int i2) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
            }

            @Override
            public void onPlayerStateChanged(boolean z, int i2) {
                if (i2 == 1 || i2 == 4 || !z) {
                    Vid_player_VideoPlayerActivity.this.playerView.setKeepScreenOn(false);
                } else {
                    Vid_player_VideoPlayerActivity.this.playerView.setKeepScreenOn(true);
                }
            }

            @Override
            public void onPositionDiscontinuity(int i2) {
                int currentWindowIndex = Vid_player_VideoPlayerActivity.this.player.getCurrentWindowIndex();
                if (currentWindowIndex != Vid_player_VideoPlayerActivity.this.position) {
                    Vid_player_VideoPlayerActivity.this.position = currentWindowIndex;
                    Vid_player_VideoPlayerActivity.this.title.setText(Vid_player_VideoPlayerActivity.list.get(currentWindowIndex).getName());
                    Vid_player_VideoPlayerActivity.this.bottomSheetDialog.dismiss();
                    Vid_player_VideoPlayerActivity.this.playlist_txt.setVisibility(View.GONE);
                    Vid_player_VideoPlayerActivity.this.getWindow().getDecorView().setSystemUiVisibility(4102);
                }
            }
        });
        PlayerView playerView = this.playerView;
        playerView.setOnTouchListener(new Vid_player_DownshotOnSwipeTouchListener(this, this.player, playerView, this.audioManager));
        this.playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                Vid_player_VideoPlayerActivity.this.bottomSheetDialog.show();
                Vid_player_VideoPlayerActivity.this.playlist_txt.setVisibility(View.VISIBLE);
            }
        });}

    @SuppressLint("Range")
    private String uri2filename() {
        Cursor query;
        String scheme = getIntent().getData().getScheme();
        if (scheme.equals("file")) {
            return getIntent().getData().getLastPathSegment();
        }
        return (!scheme.equals("select_content") || (query = getContentResolver().query(getIntent().getData(), null, null, null, null)) == null || !query.moveToFirst()) ? "" : query.getString(query.getColumnIndex("_display_name"));
    }


    public void lock() {
        this.lockstatus = Boolean.valueOf(!this.lockstatus.booleanValue());
        ((LinearLayout) findViewById(R.id.bottom_control)).setVisibility(View.INVISIBLE);
        ((LinearLayout) findViewById(R.id.center_left_control)).setVisibility(View.INVISIBLE);
        ((LinearLayout) findViewById(R.id.top_control)).setVisibility(View.INVISIBLE);
        this.unlock.setVisibility(View.VISIBLE);
        Vid_player_DownshotPreferenceUtil.getInstance(getApplicationContext()).setLock(true);
    }


    public void unlock() {
        this.lockstatus = Boolean.valueOf(!this.lockstatus.booleanValue());
        ((LinearLayout) findViewById(R.id.bottom_control)).setVisibility(View.VISIBLE);
        ((LinearLayout) findViewById(R.id.center_left_control)).setVisibility(View.VISIBLE);
        ((LinearLayout) findViewById(R.id.top_control)).setVisibility(View.VISIBLE);
        this.unlock.setVisibility(View.INVISIBLE);
        Vid_player_DownshotPreferenceUtil.getInstance(getApplicationContext()).setLock(false);
    }

    private void titlepot() {
        ViewGroup.LayoutParams layoutParams = this.title.getLayoutParams();
        layoutParams.width = this.width / 7;
        this.title.setLayoutParams(layoutParams);
    }

    private void titleland() {
        ViewGroup.LayoutParams layoutParams = this.title.getLayoutParams();
        layoutParams.width = this.width;
        this.title.setLayoutParams(layoutParams);
        getWindow().getDecorView().setSystemUiVisibility(4102);
    }


    public void setSpeed(int i) {
        this.dspeed.setText(Integer.toString(i));
        float f = i / 100.0f;
        PlaybackParameters playbackParameters = new PlaybackParameters(f);
        this.player.setPlaybackParameters(playbackParameters);
        Vid_player_DownshotPreferenceUtil.getInstance(getApplicationContext()).saveLastSpeed(f);
    }

    @Override

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = getResources().getConfiguration().orientation;
        if (i == 2) {
            titleland();
        } else if (i == 1) {
            titlepot();
        }
    }

    @Override
    public void onPause() {
        if (this.lockstatus.booleanValue()) {
            unlock();
        }
        this.player.setPlayWhenReady(false);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (!this.lockstatus.booleanValue()) {
            this.player.release();
            if (AdsUtility.config.adOnBack) {
                    showInterstitial(new BaseCallback() {
                        @Override
                        public void completed() {
                            if (PAUSE_ALL) {
                                finish();
                            } else {
                                Vid_player_VideoPlayerActivity.super.onBackPressed();
                            }
                        }
                    });
            } else {
                Vid_player_VideoPlayerActivity.super.onBackPressed();
            }
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 2084) {
            super.onActivityResult(i, i2, intent);
        } else {
            exitActivity();
        }
    }

    public static boolean isOnline(Context context) {
        try {
            return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception unused) {
            return false;
        }
    }
    private void exitActivity() {
        if (!isOnline(this)) {
            super.onBackPressed();
            Toast.makeText(this, " Sorry, No Internet Connection..!", Toast.LENGTH_SHORT).show();
        }  else {
            super.onBackPressed();
        }
    }
}
