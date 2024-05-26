package com.allformats.video.player.downloader.video_player.Extra;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import android.view.TextureView;


public abstract class Vid_player_MediaInterface implements TextureView.SurfaceTextureListener {
    public static SurfaceTexture SAVED_SURFACE;
    public Handler handler;
    public Handler mMediaHandler;
    public HandlerThread mMediaHandlerThread;
    public Vid_player_Video vd;

    public abstract long getCurrentPosition();

    public abstract long getDuration();

    public abstract boolean isPlaying();

    public abstract void pause();

    public abstract void prepare();

    public abstract void release();

    public abstract void seekTo(long j);

    public abstract void setSpeed(float f);

    public abstract void setSurface(Surface surface);

    public abstract void setVolume(float f, float f2);

    public abstract void start();

    public Vid_player_MediaInterface(Vid_player_Video vidplayerVideo) {
        this.vd = vidplayerVideo;
    }
}
