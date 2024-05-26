package com.allformats.video.player.downloader.video_player.Extra;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.util.Map;


public class Vid_player_VidplayerMediaSystem1 extends Vid_player_MediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {
    public MediaPlayer mediaPlayer;

    @Override 
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override 
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    @Override 
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public Vid_player_VidplayerMediaSystem1(Vid_player_Video vidplayerVideo) {
        super(vidplayerVideo);
    }

    @Override 
    public void prepare() {
        release();
        this.mMediaHandlerThread = new HandlerThread("JZVD");
        this.mMediaHandlerThread.start();
        this.mMediaHandler = new Handler(this.mMediaHandlerThread.getLooper());
        this.handler = new Handler();
        this.mMediaHandler.post(new Runnable() { 
            @Override 
            public final void run() {
                Vid_player_VidplayerMediaSystem1.this.hand();
            }
        });
    }

    public void hand() {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.mediaPlayer = mediaPlayer;
            mediaPlayer.setAudioStreamType(3);
            this.mediaPlayer.setLooping(this.vd.vidplayerDataSource.looping);
            this.mediaPlayer.setOnPreparedListener(this);
            this.mediaPlayer.setOnCompletionListener(this);
            this.mediaPlayer.setOnBufferingUpdateListener(this);
            this.mediaPlayer.setScreenOnWhilePlaying(true);
            this.mediaPlayer.setOnSeekCompleteListener(this);
            this.mediaPlayer.setOnErrorListener(this);
            this.mediaPlayer.setOnInfoListener(this);
            this.mediaPlayer.setOnVideoSizeChangedListener(this);
            MediaPlayer.class.getDeclaredMethod("setDataSource", String.class, Map.class).invoke(this.mediaPlayer, this.vd.vidplayerDataSource.getCurrentUrl().toString(), this.vd.vidplayerDataSource.headerMap);
            this.mediaPlayer.prepareAsync();
            this.mediaPlayer.setSurface(new Surface(SAVED_SURFACE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override 
    public void start() {
        this.mMediaHandler.post(new Runnable() { 
            @Override 
            public final void run() {
                Vid_player_VidplayerMediaSystem1.this.mediaPlayer.start();
            }
        });
    }

    @Override 
    public void pause() {
        this.mMediaHandler.post(new Runnable() { 
            @Override 
            public final void run() {
                Vid_player_VidplayerMediaSystem1.this.mediaPlayer.pause();
            }
        });
    }

    @Override 
    public boolean isPlaying() {
        return this.mediaPlayer.isPlaying();
    }

    @Override 
    public void seekTo(final long j) {
        this.mMediaHandler.post(new Runnable() { 
            @Override 
            public final void run() {
                try {
                    Vid_player_VidplayerMediaSystem1.this.mediaPlayer.seekTo((int) j);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override 
    public void release() {
        if (this.mMediaHandler != null && this.mMediaHandlerThread != null && this.mediaPlayer != null) {
            final HandlerThread handlerThread = this.mMediaHandlerThread;
            final MediaPlayer mediaPlayer = this.mediaPlayer;
            Vid_player_MediaInterface.SAVED_SURFACE = null;
            this.mMediaHandler.post(new Runnable() { 
                @Override 
                public final void run() {
                    mediaPlayer.setSurface(null);
                    mediaPlayer.release();
                    handlerThread.quit();
                }
            });
            this.mediaPlayer = null;
        }
    }

    @Override 
    public long getCurrentPosition() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0L;
    }

    @Override 
    public long getDuration() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0L;
    }

    @Override 
    public void setVolume(final float f, final float f2) {
        if (this.mMediaHandler != null) {
            this.mMediaHandler.post(new Runnable() { 
                @Override 
                public void run() {
                    MediaPlayer mediaPlayer = Vid_player_VidplayerMediaSystem1.this.mediaPlayer;
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(f, f2);
                    }
                }
            });
        }
    }

    @Override 
    public void setSpeed(float f) {
        if (Build.VERSION.SDK_INT >= 23) {
            PlaybackParams playbackParams = this.mediaPlayer.getPlaybackParams();
            playbackParams.setSpeed(f);
            this.mediaPlayer.setPlaybackParams(playbackParams);
        }
    }

    @Override 
    public void onPrepared(MediaPlayer mediaPlayer) {
        this.handler.post(new Runnable() { 
            @Override 
            public final void run() {
                Vid_player_VidplayerMediaSystem1.this.vd.onPrepared();
            }
        });
    }

    @Override 
    public void onCompletion(MediaPlayer mediaPlayer) {
        this.handler.post(new Runnable() { 
            @Override 
            public final void run() {
                Vid_player_VidplayerMediaSystem1.this.vd.onCompletion();
            }
        });
    }

    @Override 
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int i) {
        this.handler.post(new Runnable() { 
            @Override 
            public final void run() {
                Vid_player_VidplayerMediaSystem1.this.vd.setBufferProgress(i);
            }
        });
    }

    @Override 
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        this.handler.post(new Runnable() { 
            @Override 
            public final void run() {
                Vid_player_VidplayerMediaSystem1.this.vd.onSeekComplete();
            }
        });
    }

    @Override 
    public boolean onError(MediaPlayer mediaPlayer, final int i, final int i2) {
        this.handler.post(new Runnable() { 
            @Override 
            public final void run() {
                Vid_player_VidplayerMediaSystem1.this.vd.onError(i, i2);
            }
        });
        return true;
    }

    @Override 
    public boolean onInfo(MediaPlayer mediaPlayer, final int i, final int i2) {
        this.handler.post(new Runnable() { 
            @Override 
            public final void run() {
                Vid_player_VidplayerMediaSystem1.this.vd.onInfo(i, i2);
            }
        });
        return false;
    }

    @Override 
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, final int i, final int i2) {
        this.handler.post(new Runnable() { 
            @Override 
            public final void run() {
                Vid_player_VidplayerMediaSystem1.this.vd.onVideoSizeChanged(i, i2);
            }
        });
    }

    @Override 
    public void setSurface(Surface surface) {
        this.mediaPlayer.setSurface(surface);
    }

    @Override 
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        if (SAVED_SURFACE == null) {
            SAVED_SURFACE = surfaceTexture;
            prepare();
            return;
        }
        this.vd.textureView.setSurfaceTexture(SAVED_SURFACE);
    }
}
