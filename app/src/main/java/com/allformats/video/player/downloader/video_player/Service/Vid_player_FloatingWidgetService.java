package com.allformats.video.player.downloader.video_player.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.allformats.video.player.downloader.Vid_player_DS_Helper;
import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.video_player.Activity.Vid_player_VideoPlayerActivity;
import com.allformats.video.player.downloader.video_player.Extra.Vid_player_MediaData;
import com.allformats.video.player.downloader.video_player.Util.Vid_player_Constant;

import java.util.ArrayList;

public class Vid_player_FloatingWidgetService extends Service {
    ImageView closeWindow;
    View floatingView;
    ImageView floatingWindow;
    ImageView next;
    WindowManager.LayoutParams params;
    ImageView playPause;
    ImageView previous;
    Vid_player_MediaData video;
    public ArrayList<Vid_player_MediaData> videoList;
    public int videoPosition;
    VideoView videoView;
    WindowManager windowManager;

    @Override 
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override 
    public int onStartCommand(Intent intent, int i, int i2) {
        this.videoPosition = intent.getIntExtra(Vid_player_Constant.EXTRA_VIDEO_POSITION, 0);
        return super.onStartCommand(intent, i, i2);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.floatingView = LayoutInflater.from(this).inflate(R.layout.vid_player_floating_widget_layout, (ViewGroup) null);
        this.videoList = Vid_player_DS_Helper.getVideoList();
        this.video = Vid_player_DS_Helper.getLastPlayVideos();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, Build.VERSION.SDK_INT >= 26 ? 2038 : 2002, 8, -3);
        this.params = layoutParams;
        layoutParams.gravity = Gravity.CENTER;
        this.params.x = 0;
        this.params.y = 100;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        this.windowManager = windowManager;
        windowManager.addView(this.floatingView, this.params);
        this.playPause = (ImageView) this.floatingView.findViewById(R.id.playPause);
        this.closeWindow = (ImageView) this.floatingView.findViewById(R.id.closeWindow);
        this.floatingWindow = (ImageView) this.floatingView.findViewById(R.id.floatingWindow);
        this.next = (ImageView) this.floatingView.findViewById(R.id.next);
        this.previous = (ImageView) this.floatingView.findViewById(R.id.previous);
        VideoView videoView = (VideoView) this.floatingView.findViewById(R.id.videoView);
        this.videoView = videoView;
        Vid_player_MediaData vidplayerMediaData = this.video;
        if (vidplayerMediaData != null) {
            videoView.setVideoPath(vidplayerMediaData.getPath());
            this.videoView.seekTo(this.video.getVideoLastPlayPosition());
            this.videoView.start();
        }
        this.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override 
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (Vid_player_FloatingWidgetService.this.videoPosition < Vid_player_FloatingWidgetService.this.videoList.size() - 1) {
                    Vid_player_FloatingWidgetService.this.videoPosition++;
                    Vid_player_FloatingWidgetService.this.videoView.setVideoPath(Vid_player_FloatingWidgetService.this.videoList.get(Vid_player_FloatingWidgetService.this.videoPosition).getPath());
                    Vid_player_FloatingWidgetService.this.videoView.start();
                    return;
                }
                Vid_player_FloatingWidgetService.this.videoView.pause();
                Vid_player_FloatingWidgetService.this.playPause.setImageResource(R.drawable.hplib_ic_play_download);
            }
        });
        this.next.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (Vid_player_FloatingWidgetService.this.videoPosition < Vid_player_FloatingWidgetService.this.videoList.size() - 1) {
                    Vid_player_FloatingWidgetService.this.videoPosition++;
                    Vid_player_FloatingWidgetService.this.videoView.setVideoPath(Vid_player_FloatingWidgetService.this.videoList.get(Vid_player_FloatingWidgetService.this.videoPosition).getPath());
                    Vid_player_FloatingWidgetService.this.videoView.start();
                }
            }
        });
        this.previous.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (Vid_player_FloatingWidgetService.this.videoPosition > 0) {
                    Vid_player_FloatingWidgetService vidplayerFloatingWidgetService = Vid_player_FloatingWidgetService.this;
                    vidplayerFloatingWidgetService.videoPosition--;
                    Vid_player_FloatingWidgetService.this.videoView.setVideoPath(Vid_player_FloatingWidgetService.this.videoList.get(Vid_player_FloatingWidgetService.this.videoPosition).getPath());
                    Vid_player_FloatingWidgetService.this.videoView.start();
                }
            }
        });
        this.closeWindow.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                Vid_player_DS_Helper.setIsFloatingVideo(false);
                Vid_player_FloatingWidgetService.this.stopSelf();
            }
        });
        this.playPause.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (Vid_player_FloatingWidgetService.this.videoView.isPlaying()) {
                    Vid_player_FloatingWidgetService.this.videoView.pause();
                    Vid_player_FloatingWidgetService.this.playPause.setImageResource(R.drawable.hplib_ic_play_download);
                    return;
                }
                Vid_player_FloatingWidgetService.this.videoView.start();
                Vid_player_FloatingWidgetService.this.playPause.setImageResource(R.drawable.hplib_ic_pause);
            }
        });
        this.floatingWindow.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                Vid_player_DS_Helper.setIsFloatingVideo(false);
                Vid_player_FloatingWidgetService vidplayerFloatingWidgetService = Vid_player_FloatingWidgetService.this;
                vidplayerFloatingWidgetService.startActivity(Vid_player_VideoPlayerActivity.getInstance(vidplayerFloatingWidgetService.getApplicationContext(), Vid_player_FloatingWidgetService.this.videoView.getCurrentPosition(), true).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Vid_player_FloatingWidgetService.this.stopSelf();
            }
        });
        this.floatingView.findViewById(R.id.mainParentRelativeLayout).setOnTouchListener(new View.OnTouchListener() {
            float mTouchX;
            float mTouchY;
            int mXAxis;
            int mYAxis;

            @Override 
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action != 0) {
                    if (action != 1) {
                        if (action != 2) {
                            return false;
                        }
                        Vid_player_FloatingWidgetService.this.params.x = this.mXAxis + ((int) (motionEvent.getRawX() - this.mTouchX));
                        Vid_player_FloatingWidgetService.this.params.y = this.mYAxis + ((int) (motionEvent.getRawY() - this.mTouchY));
                        Vid_player_FloatingWidgetService.this.windowManager.updateViewLayout(Vid_player_FloatingWidgetService.this.floatingView, Vid_player_FloatingWidgetService.this.params);
                    }
                    return true;
                }
                this.mXAxis = Vid_player_FloatingWidgetService.this.params.x;
                this.mYAxis = Vid_player_FloatingWidgetService.this.params.y;
                this.mTouchX = motionEvent.getRawX();
                this.mTouchY = motionEvent.getRawY();
                return true;
            }
        });
    }

    @Override 
    public void onDestroy() {
        super.onDestroy();
        View view = this.floatingView;
        if (view != null) {
            this.windowManager.removeView(view);
        }
    }
}
