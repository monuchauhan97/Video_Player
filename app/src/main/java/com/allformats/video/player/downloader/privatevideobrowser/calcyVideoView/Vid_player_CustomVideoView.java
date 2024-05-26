package com.allformats.video.player.downloader.privatevideobrowser.calcyVideoView;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.privatevideobrowser.calcyVideoView.Vid_player_CustomMediaController.MediaPlayerControl;
import com.allformats.video.player.downloader.privatevideobrowser.calcyVideoView.Vid_player_OrientationDetector.Direction;
import com.allformats.video.player.downloader.privatevideobrowser.calcyVideoView.Vid_player_OrientationDetector.OrientationChangeListener;

import java.io.IOException;
import java.util.Map;

public class Vid_player_CustomVideoView extends SurfaceView implements MediaPlayerControl, OrientationChangeListener {
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PREPARING = 1;
    OnPreparedListener mPreparedListener;
    Callback mSHCallback;
    OnVideoSizeChangedListener mSizeChangedListener;
    private String TAG;
    private int mAudioSession;
    private boolean mAutoRotation;
    private OnBufferingUpdateListener mBufferingUpdateListener;
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;
    private OnCompletionListener mCompletionListener;
    private Context mContext;
    private int mCurrentBufferPercentage;
    private int mCurrentState;
    private OnErrorListener mErrorListener;
    private boolean mFitXY;
    private OnInfoListener mInfoListener;
    private Vid_player_CustomMediaController mMediaController;
    private MediaPlayer mMediaPlayer;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPreparedListener mOnPreparedListener;
    private Vid_player_OrientationDetector mVidplayerOrientationDetector;
    private boolean mPreparedBeforeStart;
    private int mSeekWhenPrepared;
    private int mSurfaceHeight;
    private SurfaceHolder mSurfaceHolder;
    private int mSurfaceWidth;
    private int mTargetState;
    private Uri mUri;
    private int mVideoHeight;
    private int mVideoViewLayoutHeight;
    private int mVideoViewLayoutWidth;
    private int mVideoWidth;
    private VideoViewCallback videoViewCallback;

    public Vid_player_CustomVideoView(Context context) {
        this(context, null);
    }

    public Vid_player_CustomVideoView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public Vid_player_CustomVideoView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.TAG = "UniversalVideoView";
        this.mCurrentState = 0;
        this.mTargetState = 0;
        this.mSurfaceHolder = null;
        this.mMediaPlayer = null;
        this.mFitXY = false;
        this.mAutoRotation = false;
        this.mVideoViewLayoutWidth = 0;
        this.mVideoViewLayoutHeight = 0;
        this.mSizeChangedListener = new OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
                Vid_player_CustomVideoView.this.mVideoWidth = mediaPlayer.getVideoWidth();
                Vid_player_CustomVideoView.this.mVideoHeight = mediaPlayer.getVideoHeight();
                Log.d(Vid_player_CustomVideoView.this.TAG, String.format("onVideoSizeChanged width=%d,height=%d", new Object[]{Integer.valueOf(Vid_player_CustomVideoView.this.mVideoWidth), Integer.valueOf(Vid_player_CustomVideoView.this.mVideoHeight)}));
                if (Vid_player_CustomVideoView.this.mVideoWidth != 0 && Vid_player_CustomVideoView.this.mVideoHeight != 0) {
                    Vid_player_CustomVideoView.this.getHolder().setFixedSize(Vid_player_CustomVideoView.this.mVideoWidth, Vid_player_CustomVideoView.this.mVideoHeight);
                    Vid_player_CustomVideoView.this.requestLayout();
                }
            }
        };
        this.mPreparedListener = new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                Vid_player_CustomVideoView.this.mCurrentState = 2;
                Vid_player_CustomVideoView vidplayerCustomVideoView = Vid_player_CustomVideoView.this;
                vidplayerCustomVideoView.mCanPause = vidplayerCustomVideoView.mCanSeekBack = vidplayerCustomVideoView.mCanSeekForward = true;
                Vid_player_CustomVideoView.this.mPreparedBeforeStart = true;
                if (Vid_player_CustomVideoView.this.mMediaController != null) {
                    Vid_player_CustomVideoView.this.mMediaController.hideLoading();
                }
                if (Vid_player_CustomVideoView.this.mOnPreparedListener != null) {
                    Vid_player_CustomVideoView.this.mOnPreparedListener.onPrepared(Vid_player_CustomVideoView.this.mMediaPlayer);
                }
                if (Vid_player_CustomVideoView.this.mMediaController != null) {
                    Vid_player_CustomVideoView.this.mMediaController.setEnabled(true);
                }
                Vid_player_CustomVideoView.this.mVideoWidth = mediaPlayer.getVideoWidth();
                Vid_player_CustomVideoView.this.mVideoHeight = mediaPlayer.getVideoHeight();
                int access$1100 = Vid_player_CustomVideoView.this.mSeekWhenPrepared;
                if (access$1100 != 0) {
                    Vid_player_CustomVideoView.this.seekTo(access$1100);
                }
                if (Vid_player_CustomVideoView.this.mVideoWidth != 0 && Vid_player_CustomVideoView.this.mVideoHeight != 0) {
                    Vid_player_CustomVideoView.this.getHolder().setFixedSize(Vid_player_CustomVideoView.this.mVideoWidth, Vid_player_CustomVideoView.this.mVideoHeight);
                    if (Vid_player_CustomVideoView.this.mSurfaceWidth != Vid_player_CustomVideoView.this.mVideoWidth || Vid_player_CustomVideoView.this.mSurfaceHeight != Vid_player_CustomVideoView.this.mVideoHeight) {
                        return;
                    }
                    if (Vid_player_CustomVideoView.this.mTargetState == 3) {
                        Vid_player_CustomVideoView.this.start();
                        if (Vid_player_CustomVideoView.this.mMediaController != null) {
                            Vid_player_CustomVideoView.this.mMediaController.show();
                        }
                    } else if (!Vid_player_CustomVideoView.this.isPlaying()) {
                        if ((access$1100 != 0 || Vid_player_CustomVideoView.this.getCurrentPosition() > 0) && Vid_player_CustomVideoView.this.mMediaController != null) {
                            Vid_player_CustomVideoView.this.mMediaController.show(0);
                        }
                    }
                } else if (Vid_player_CustomVideoView.this.mTargetState == 3) {
                    Vid_player_CustomVideoView.this.start();
                }
            }
        };
        this.mCompletionListener = new OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                Vid_player_CustomVideoView.this.mCurrentState = 5;
                Vid_player_CustomVideoView.this.mTargetState = 5;
                if (Vid_player_CustomVideoView.this.mMediaController != null) {
                    boolean isPlaying = Vid_player_CustomVideoView.this.mMediaPlayer.isPlaying();
                    int access$300 = Vid_player_CustomVideoView.this.mCurrentState;
                    Vid_player_CustomVideoView.this.mMediaController.showComplete();
                    Log.d(Vid_player_CustomVideoView.this.TAG, String.format("a=%s,b=%d", new Object[]{Boolean.valueOf(isPlaying), Integer.valueOf(access$300)}));
                }
                if (Vid_player_CustomVideoView.this.mOnCompletionListener != null) {
                    Vid_player_CustomVideoView.this.mOnCompletionListener.onCompletion(Vid_player_CustomVideoView.this.mMediaPlayer);
                }
            }
        };
        this.mInfoListener = new OnInfoListener() {
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
                boolean z;
                boolean z2 = false;
                if (i == 701) {
                    Log.d(Vid_player_CustomVideoView.this.TAG, "onInfo MediaPlayer.MEDIA_INFO_BUFFERING_START");
                    if (Vid_player_CustomVideoView.this.videoViewCallback != null) {
                        Vid_player_CustomVideoView.this.videoViewCallback.onBufferingStart(Vid_player_CustomVideoView.this.mMediaPlayer);
                    }
                    if (Vid_player_CustomVideoView.this.mMediaController != null) {
                        Vid_player_CustomVideoView.this.mMediaController.showLoading();
                    }
                } else if (i != 702) {
                    z = false;
                    if (Vid_player_CustomVideoView.this.mOnInfoListener != null) {
                        return z;
                    }
                    if (Vid_player_CustomVideoView.this.mOnInfoListener.onInfo(mediaPlayer, i, i2) || z) {
                        z2 = true;
                    }
                    return z2;
                } else {
                    Log.d(Vid_player_CustomVideoView.this.TAG, "onInfo MediaPlayer.MEDIA_INFO_BUFFERING_END");
                    if (Vid_player_CustomVideoView.this.videoViewCallback != null) {
                        Vid_player_CustomVideoView.this.videoViewCallback.onBufferingEnd(Vid_player_CustomVideoView.this.mMediaPlayer);
                    }
                    if (Vid_player_CustomVideoView.this.mMediaController != null) {
                        Vid_player_CustomVideoView.this.mMediaController.hideLoading();
                    }
                }
                z = true;
                if (Vid_player_CustomVideoView.this.mOnInfoListener != null) {
                }
                return z;
            }
        };
        this.mErrorListener = new OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                String access$200 = Vid_player_CustomVideoView.this.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error: ");
                stringBuilder.append(i);
                stringBuilder.append(",");
                stringBuilder.append(i2);
                Log.d(access$200, stringBuilder.toString());
                Vid_player_CustomVideoView.this.mCurrentState = -1;
                Vid_player_CustomVideoView.this.mTargetState = -1;
                if (Vid_player_CustomVideoView.this.mMediaController != null) {
                    Vid_player_CustomVideoView.this.mMediaController.showError();
                }
                if (Vid_player_CustomVideoView.this.mOnErrorListener == null || Vid_player_CustomVideoView.this.mOnErrorListener.onError(Vid_player_CustomVideoView.this.mMediaPlayer, i, i2)) {
                }
                return true;
            }
        };
        this.mBufferingUpdateListener = new OnBufferingUpdateListener() {
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                Vid_player_CustomVideoView.this.mCurrentBufferPercentage = i;
            }
        };
        this.mSHCallback = new Callback() {
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
                Vid_player_CustomVideoView.this.mSurfaceWidth = i2;
                Vid_player_CustomVideoView.this.mSurfaceHeight = i3;
                Object obj = 1;
                Object obj2 = Vid_player_CustomVideoView.this.mTargetState == 3 ? 1 : null;
                if (!(Vid_player_CustomVideoView.this.mVideoWidth == i2 && Vid_player_CustomVideoView.this.mVideoHeight == i3)) {
                    obj = null;
                }
                if (Vid_player_CustomVideoView.this.mMediaPlayer != null && obj2 != null && obj != null) {
                    if (Vid_player_CustomVideoView.this.mSeekWhenPrepared != 0) {
                        Vid_player_CustomVideoView vidplayerCustomVideoView = Vid_player_CustomVideoView.this;
                        vidplayerCustomVideoView.seekTo(vidplayerCustomVideoView.mSeekWhenPrepared);
                    }
                    Vid_player_CustomVideoView.this.start();
                }
            }

            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Vid_player_CustomVideoView.this.mSurfaceHolder = surfaceHolder;
                Vid_player_CustomVideoView.this.openVideo();
                Vid_player_CustomVideoView.this.enableOrientationDetect();
            }

            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Vid_player_CustomVideoView.this.mSurfaceHolder = null;
                if (Vid_player_CustomVideoView.this.mMediaController != null) {
                    Vid_player_CustomVideoView.this.mMediaController.hide();
                }
                Vid_player_CustomVideoView.this.release(true);
                Vid_player_CustomVideoView.this.disableOrientationDetect();
            }
        };
        this.mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CustomMediaController, 0, 0);
        this.mFitXY = obtainStyledAttributes.getBoolean(1, false);
        this.mAutoRotation = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        initVideoView();
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mFitXY) {
            onMeasureFitXY(i, i2);
        } else {
            onMeasureKeepAspectRatio(i, i2);
        }
    }

    private void onMeasureFitXY(int i, int i2) {
        setMeasuredDimension(getDefaultSize(this.mVideoWidth, i), getDefaultSize(this.mVideoHeight, i2));
    }

    private void onMeasureKeepAspectRatio(int i, int i2) {
        int defaultSize = getDefaultSize(this.mVideoWidth, i);
        int defaultSize2 = getDefaultSize(this.mVideoHeight, i2);
        if (this.mVideoWidth > 0 && this.mVideoHeight > 0) {
            defaultSize = MeasureSpec.getMode(i);
            i = MeasureSpec.getSize(i);
            defaultSize2 = MeasureSpec.getMode(i2);
            i2 = MeasureSpec.getSize(i2);
            int i3;
            if (defaultSize == 1073741824 && defaultSize2 == 1073741824) {
                defaultSize = this.mVideoWidth;
                defaultSize2 = defaultSize * i2;
                i3 = this.mVideoHeight;
                if (defaultSize2 < i * i3) {
                    defaultSize = (defaultSize * i2) / i3;
                    defaultSize2 = i2;
                } else {
                    if (defaultSize * i2 > i * i3) {
                        defaultSize2 = (i3 * i) / defaultSize;
                    }
                    defaultSize = i;
                    defaultSize2 = i2;
                }
            } else if (defaultSize == 1073741824) {
                defaultSize = (this.mVideoHeight * i) / this.mVideoWidth;
                if (defaultSize2 != Integer.MIN_VALUE || defaultSize <= i2) {
                    defaultSize2 = defaultSize;
                }
                defaultSize = i;
                defaultSize2 = i2;
            } else {
                if (defaultSize2 == 1073741824) {
                    defaultSize2 = (this.mVideoWidth * i2) / this.mVideoHeight;
                    if (defaultSize == Integer.MIN_VALUE) {
                    }
                } else {
                    i3 = this.mVideoWidth;
                    int i4 = this.mVideoHeight;
                    if (defaultSize2 != Integer.MIN_VALUE || i4 <= i2) {
                        defaultSize2 = i3;
                        i2 = i4;
                    } else {
                        defaultSize2 = (i2 * i3) / i4;
                    }
                    if (defaultSize == Integer.MIN_VALUE /*&& r1 > i*/) {
                        defaultSize2 = (i4 * i) / i3;
                    }
                }
                defaultSize = defaultSize2;
                defaultSize2 = i2;
            }
            defaultSize = i;
        }
        setMeasuredDimension(defaultSize, defaultSize2);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(Vid_player_CustomVideoView.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (VERSION.SDK_INT >= 14) {
            accessibilityNodeInfo.setClassName(Vid_player_CustomVideoView.class.getName());
        }
    }

    public int resolveAdjustedSize(int i, int i2) {
        return getDefaultSize(i, i2);
    }

    private void initVideoView() {
        this.mVideoWidth = 0;
        this.mVideoHeight = 0;
        getHolder().addCallback(this.mSHCallback);
        getHolder().setType(3);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        this.mCurrentState = 0;
        this.mTargetState = 0;
    }

    public void onOrientationChanged(int i, Direction direction) {
        if (this.mAutoRotation) {
            if (direction == Direction.PORTRAIT) {
                setFullscreen(false, 1);
            } else if (direction == Direction.REVERSE_PORTRAIT) {
                setFullscreen(false, 7);
            } else if (direction == Direction.LANDSCAPE) {
                setFullscreen(true, 0);
            } else if (direction == Direction.REVERSE_LANDSCAPE) {
                setFullscreen(true, 8);
            }
        }
    }


    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    public void setVideoURI(Uri uri, Map<String, String> map) {
        this.mUri = uri;
        this.mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stopPlayback() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            this.mTargetState = 0;
        }
    }

    private void openVideo() {
        if (this.mUri != null && this.mSurfaceHolder != null) {
            ((AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE)).requestAudioFocus(null, 3, 1);
            release(false);
            try {
                MediaPlayer mediaPlayer = new MediaPlayer();
                this.mMediaPlayer = mediaPlayer;
                int i = this.mAudioSession;
                if (i != 0) {
                    mediaPlayer.setAudioSessionId(i);
                } else {
                    this.mAudioSession = mediaPlayer.getAudioSessionId();
                }
                this.mMediaPlayer.setOnPreparedListener(this.mPreparedListener);
                this.mMediaPlayer.setOnVideoSizeChangedListener(this.mSizeChangedListener);
                this.mMediaPlayer.setOnCompletionListener(this.mCompletionListener);
                this.mMediaPlayer.setOnErrorListener(this.mErrorListener);
                this.mMediaPlayer.setOnInfoListener(this.mInfoListener);
                this.mMediaPlayer.setOnBufferingUpdateListener(this.mBufferingUpdateListener);
                this.mCurrentBufferPercentage = 0;
                this.mMediaPlayer.setDataSource(this.mContext, this.mUri);
                this.mMediaPlayer.setDisplay(this.mSurfaceHolder);
                this.mMediaPlayer.setAudioStreamType(3);
                this.mMediaPlayer.setScreenOnWhilePlaying(true);
                this.mMediaPlayer.prepareAsync();
                this.mCurrentState = 1;
                attachMediaController();
            } catch (IOException e) {
                String str = this.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to open content: ");
                stringBuilder.append(this.mUri);
                Log.w(str, stringBuilder.toString(), e);
                this.mCurrentState = -1;
                this.mTargetState = -1;
                this.mErrorListener.onError(this.mMediaPlayer, 1, 0);
            }
        }
    }

    public void setMediaController(Vid_player_CustomMediaController vidplayerCustomMediaController) {
        Vid_player_CustomMediaController vidplayerCustomMediaController2 = this.mMediaController;
        if (vidplayerCustomMediaController2 != null) {
            vidplayerCustomMediaController2.hide();
        }
        this.mMediaController = vidplayerCustomMediaController;
        attachMediaController();
    }

    private void attachMediaController() {
        if (this.mMediaPlayer != null) {
            Vid_player_CustomMediaController vidplayerCustomMediaController = this.mMediaController;
            if (vidplayerCustomMediaController != null) {
                vidplayerCustomMediaController.setMediaPlayer(this);
                this.mMediaController.setEnabled(isInPlaybackState());
                this.mMediaController.hide();
            }
        }
    }


    private void enableOrientationDetect() {
        if (this.mAutoRotation && this.mVidplayerOrientationDetector == null) {
            Vid_player_OrientationDetector vidplayerOrientationDetector = new Vid_player_OrientationDetector(this.mContext);
            this.mVidplayerOrientationDetector = vidplayerOrientationDetector;
            vidplayerOrientationDetector.setOrientationChangeListener(this);
            this.mVidplayerOrientationDetector.enable();
        }
    }

    private void disableOrientationDetect() {
        Vid_player_OrientationDetector vidplayerOrientationDetector = this.mVidplayerOrientationDetector;
        if (vidplayerOrientationDetector != null) {
            vidplayerOrientationDetector.disable();
        }
    }

    private void release(boolean z) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            this.mCurrentState = 0;
            if (z) {
                this.mTargetState = 0;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisibility();
        }
        return false;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent) {
        if (isInPlaybackState() && this.mMediaController != null) {
            toggleMediaControlsVisibility();
        }
        return false;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        Object obj = (i == 4 || i == 24 || i == 25 || i == 164 || i == 82 || i == 5 || i == 6) ? null : 1;
        if (!(!isInPlaybackState() || obj == null || this.mMediaController == null)) {
            if (i == 79 || i == 85) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                } else {
                    start();
                    this.mMediaController.hide();
                }
                return true;
            } else if (i == 126) {
                if (!this.mMediaPlayer.isPlaying()) {
                    start();
                    this.mMediaController.hide();
                }
                return true;
            } else if (i == 86 || i == 127) {
                if (this.mMediaPlayer.isPlaying()) {
                    pause();
                    this.mMediaController.show();
                }
                return true;
            } else {
                toggleMediaControlsVisibility();
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void toggleMediaControlsVisibility() {
        if (this.mMediaController.isShowing()) {
            this.mMediaController.hide();
        } else {
            this.mMediaController.show();
        }
    }

    public void start() {
        if (!this.mPreparedBeforeStart) {
            Vid_player_CustomMediaController vidplayerCustomMediaController = this.mMediaController;
            if (vidplayerCustomMediaController != null) {
                vidplayerCustomMediaController.showLoading();
            }
        }
        if (isInPlaybackState()) {
            this.mMediaPlayer.start();
            this.mCurrentState = 3;
            VideoViewCallback videoViewCallback = this.videoViewCallback;
            if (videoViewCallback != null) {
                videoViewCallback.onStart(this.mMediaPlayer);
            }
        }
        this.mTargetState = 3;
    }

    public void pause() {
        if (isInPlaybackState() && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            this.mCurrentState = 4;
            VideoViewCallback videoViewCallback = this.videoViewCallback;
            if (videoViewCallback != null) {
                videoViewCallback.onPause(this.mMediaPlayer);
            }
        }
        this.mTargetState = 4;
    }

    public void suspend() {
        release(false);
    }

    public void resume() {
        openVideo();
    }

    public int getDuration() {
        return isInPlaybackState() ? this.mMediaPlayer.getDuration() : -1;
    }

    public int getCurrentPosition() {
        return isInPlaybackState() ? this.mMediaPlayer.getCurrentPosition() : 0;
    }

    public void seekTo(int i) {
        if (isInPlaybackState()) {
            this.mMediaPlayer.seekTo(i);
            this.mSeekWhenPrepared = 0;
            return;
        }
        this.mSeekWhenPrepared = i;
    }

    public boolean isPlaying() {
        return isInPlaybackState() && this.mMediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return this.mMediaPlayer != null ? this.mCurrentBufferPercentage : 0;
    }

    private boolean isInPlaybackState() {
        if (this.mMediaPlayer != null) {
            int i = this.mCurrentState;
            if (!(i == -1 || i == 0 || i == 1)) {
                return true;
            }
        }
        return false;
    }

    public boolean canPause() {
        return this.mCanPause;
    }

    public boolean canSeekBackward() {
        return this.mCanSeekBack;
    }

    public boolean canSeekForward() {
        return this.mCanSeekForward;
    }

    public void closePlayer() {
        release(true);
    }

    public void setFullscreen(boolean z) {
        setFullscreen(z, z ? 1 : 0);
    }

    public void setFullscreen(boolean z, int i) {
        Activity activity = (Activity) this.mContext;
        LayoutParams layoutParams;
        if (z) {
            if (this.mVideoViewLayoutWidth == 0 && this.mVideoViewLayoutHeight == 0) {
                layoutParams = getLayoutParams();
                this.mVideoViewLayoutWidth = layoutParams.width;
                this.mVideoViewLayoutHeight = layoutParams.height;
            }
            activity.getWindow().addFlags(1024);
            activity.setRequestedOrientation(i);
        } else {
            layoutParams = getLayoutParams();
            layoutParams.width = this.mVideoViewLayoutWidth;
            layoutParams.height = this.mVideoViewLayoutHeight;
            setLayoutParams(layoutParams);
            activity.getWindow().clearFlags(1024);
            activity.setRequestedOrientation(i);
        }
        this.mMediaController.toggleButtons(z);
        VideoViewCallback videoViewCallback = this.videoViewCallback;
        if (videoViewCallback != null) {
            videoViewCallback.onScaleChange(z);
        }
    }

    public void setVideoViewCallback(VideoViewCallback videoViewCallback) {
        this.videoViewCallback = videoViewCallback;
    }

    public interface VideoViewCallback {
        void onBufferingEnd(MediaPlayer mediaPlayer);

        void onBufferingStart(MediaPlayer mediaPlayer);

        void onPause(MediaPlayer mediaPlayer);

        void onScaleChange(boolean z);

        void onStart(MediaPlayer mediaPlayer);
    }
}
