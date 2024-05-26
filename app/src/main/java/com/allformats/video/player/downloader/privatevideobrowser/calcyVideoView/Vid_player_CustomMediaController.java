package com.allformats.video.player.downloader.privatevideobrowser.calcyVideoView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.allformats.video.player.downloader.R;

import java.util.Formatter;
import java.util.Locale;

public class Vid_player_CustomMediaController extends FrameLayout {
    private static final int FADE_OUT = 1;
    private static final int HIDE_COMPLETE = 8;
    private static final int HIDE_ERROR = 6;
    private static final int HIDE_LOADING = 4;
    private static final int SHOW_COMPLETE = 7;
    private static final int SHOW_ERROR = 5;
    private static final int SHOW_LOADING = 3;
    private static final int SHOW_PROGRESS = 2;
    private static final int STATE_COMPLETE = 5;
    private static final int STATE_ERROR = 4;
    private static final int STATE_LOADING = 3;
    private static final int STATE_PAUSE = 2;
    private static final int STATE_PLAYING = 1;
    private static final int sDefaultTimeout = 3000;
    public static ImageButton mCloseButton = null;
    boolean handled;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private ViewGroup errorLayout;
    private ViewGroup loadingLayout;
    private View mBackButton;
    private OnClickListener mBackListener;
    private View mCenterPlayButton;
    private OnClickListener mCenterPlayListener;
    private Context mContext;
    private View mControlLayout;
    private TextView mCurrentTime;
    private boolean mDragging;
    private TextView mEndTime;
    private boolean mFullscreenEnabled;
    private Handler mHandler;
    private boolean mIsFullScreen;
    private OnClickListener mPauseListener;
    private MediaPlayerControl mPlayer;
    private ProgressBar mProgress;
    private boolean mScalable;
    private ImageButton mScaleButton;
    private OnClickListener mScaleListener;
    private SeekBar.OnSeekBarChangeListener mSeekListener;
    private boolean mShowing;
    private int mState;
    private TextView mTitle;
    private View mTitleLayout;
    private OnTouchListener mTouchListener;
    private ImageButton mTurnButton;


    public Vid_player_CustomMediaController(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mShowing = true;
        this.mScalable = false;
        this.mIsFullScreen = false;
        this.mFullscreenEnabled = false;
        this.mState = 3;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        Vid_player_CustomMediaController.this.hide();
                        return;
                    case 2:
                        int progress = Vid_player_CustomMediaController.this.setProgress();
                        if (!Vid_player_CustomMediaController.this.mDragging && Vid_player_CustomMediaController.this.mShowing && Vid_player_CustomMediaController.this.mPlayer != null && Vid_player_CustomMediaController.this.mPlayer.isPlaying()) {
                            sendMessageDelayed(obtainMessage(2), 1000 - (progress % 1000));
                            return;
                        }
                        return;
                    case 3:
                        Vid_player_CustomMediaController.this.show();
                        Vid_player_CustomMediaController.this.showCenterView(R.id.loading_layout);
                        return;
                    case 4:
                    case 6:
                    case 8:
                        Vid_player_CustomMediaController.this.hide();
                        Vid_player_CustomMediaController.this.hideCenterView();
                        return;
                    case 5:
                        Vid_player_CustomMediaController.this.show();
                        Vid_player_CustomMediaController.this.showCenterView(R.id.error_layout);
                        return;
                    case 7:
                        Vid_player_CustomMediaController.this.showCenterView(R.id.center_playbtn);
                        return;
                    default:
                        return;
                }
            }
        };
        this.handled = false;
        this.mTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 || !Vid_player_CustomMediaController.this.mShowing) {
                    return false;
                }
                Vid_player_CustomMediaController.this.hide();
                Vid_player_CustomMediaController.this.handled = true;
                return true;
            }
        };
        this.mPauseListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vid_player_CustomMediaController.this.mPlayer != null) {
                    Vid_player_CustomMediaController.this.doPauseResume();
                    Vid_player_CustomMediaController.this.show(3000);
                }
            }
        };
        this.mScaleListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_CustomMediaController vidplayerCustomMediaController = Vid_player_CustomMediaController.this;
                vidplayerCustomMediaController.mIsFullScreen = !vidplayerCustomMediaController.mIsFullScreen;
                Vid_player_CustomMediaController.this.updateScaleButton();
                Vid_player_CustomMediaController.this.updateBackButton();
                Vid_player_CustomMediaController.this.mPlayer.setFullscreen(Vid_player_CustomMediaController.this.mIsFullScreen);
            }
        };
        this.mBackListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vid_player_CustomMediaController.this.mIsFullScreen) {
                    Vid_player_CustomMediaController.this.mIsFullScreen = false;
                    Vid_player_CustomMediaController.this.updateScaleButton();
                    Vid_player_CustomMediaController.this.updateBackButton();
                    Vid_player_CustomMediaController.this.mPlayer.setFullscreen(false);
                }
            }
        };
        this.mCenterPlayListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_CustomMediaController.this.hideCenterView();
                Vid_player_CustomMediaController.this.mPlayer.start();
            }
        };
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() {
            int newPosition = 0;
            boolean change = false;

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (Vid_player_CustomMediaController.this.mPlayer != null) {
                    Vid_player_CustomMediaController.this.show(3600000);
                    Vid_player_CustomMediaController.this.mDragging = true;
                    Vid_player_CustomMediaController.this.mHandler.removeMessages(2);
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (Vid_player_CustomMediaController.this.mPlayer != null && z) {
                    this.newPosition = (int) ((Vid_player_CustomMediaController.this.mPlayer.getDuration() * i) / 1000);
                    this.change = true;
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (Vid_player_CustomMediaController.this.mPlayer != null) {
                    if (this.change) {
                        Vid_player_CustomMediaController.this.mPlayer.seekTo(this.newPosition);
                        if (Vid_player_CustomMediaController.this.mCurrentTime != null) {
                            Vid_player_CustomMediaController.this.mCurrentTime.setText(Vid_player_CustomMediaController.this.stringForTime(this.newPosition));
                        }
                    }
                    Vid_player_CustomMediaController.this.mDragging = false;
                    Vid_player_CustomMediaController.this.setProgress();
                    Vid_player_CustomMediaController.this.updatePausePlay();
                    Vid_player_CustomMediaController.this.show(3000);
                    Vid_player_CustomMediaController.this.mShowing = true;
                    Vid_player_CustomMediaController.this.mHandler.sendEmptyMessage(2);
                }
            }
        };
        this.mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CustomMediaController);
        this.mScalable = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        init(context);
    }

    public Vid_player_CustomMediaController(Context context) {
        super(context);
        this.mShowing = true;
        this.mScalable = false;
        this.mIsFullScreen = false;
        this.mFullscreenEnabled = false;
        this.mState = 3;
        this.mHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        Vid_player_CustomMediaController.this.hide();
                        return;
                    case 2:
                        int progress = Vid_player_CustomMediaController.this.setProgress();
                        if (!Vid_player_CustomMediaController.this.mDragging && Vid_player_CustomMediaController.this.mShowing && Vid_player_CustomMediaController.this.mPlayer != null && Vid_player_CustomMediaController.this.mPlayer.isPlaying()) {
                            sendMessageDelayed(obtainMessage(2), 1000 - (progress % 1000));
                            return;
                        }
                        return;
                    case 3:
                        Vid_player_CustomMediaController.this.show();
                        Vid_player_CustomMediaController.this.showCenterView(R.id.loading_layout);
                        return;
                    case 4:
                    case 6:
                    case 8:
                        Vid_player_CustomMediaController.this.hide();
                        Vid_player_CustomMediaController.this.hideCenterView();
                        return;
                    case 5:
                        Vid_player_CustomMediaController.this.show();
                        Vid_player_CustomMediaController.this.showCenterView(R.id.error_layout);
                        return;
                    case 7:
                        Vid_player_CustomMediaController.this.showCenterView(R.id.center_playbtn);
                        return;
                    default:
                        return;
                }
            }
        };
        this.handled = false;
        this.mTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0 || !Vid_player_CustomMediaController.this.mShowing) {
                    return false;
                }
                Vid_player_CustomMediaController.this.hide();
                Vid_player_CustomMediaController.this.handled = true;
                return true;
            }
        };
        this.mPauseListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vid_player_CustomMediaController.this.mPlayer != null) {
                    Vid_player_CustomMediaController.this.doPauseResume();
                    Vid_player_CustomMediaController.this.show(3000);
                }
            }
        };
        this.mScaleListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_CustomMediaController vidplayerCustomMediaController = Vid_player_CustomMediaController.this;
                vidplayerCustomMediaController.mIsFullScreen = !vidplayerCustomMediaController.mIsFullScreen;
                Vid_player_CustomMediaController.this.updateScaleButton();
                Vid_player_CustomMediaController.this.updateBackButton();
                Vid_player_CustomMediaController.this.mPlayer.setFullscreen(Vid_player_CustomMediaController.this.mIsFullScreen);
            }
        };
        this.mBackListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vid_player_CustomMediaController.this.mIsFullScreen) {
                    Vid_player_CustomMediaController.this.mIsFullScreen = false;
                    Vid_player_CustomMediaController.this.updateScaleButton();
                    Vid_player_CustomMediaController.this.updateBackButton();
                    Vid_player_CustomMediaController.this.mPlayer.setFullscreen(false);
                }
            }
        };
        this.mCenterPlayListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                Vid_player_CustomMediaController.this.hideCenterView();
                Vid_player_CustomMediaController.this.mPlayer.start();
            }
        };
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() {
            int newPosition = 0;
            boolean change = false;

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (Vid_player_CustomMediaController.this.mPlayer != null) {
                    Vid_player_CustomMediaController.this.show(3600000);
                    Vid_player_CustomMediaController.this.mDragging = true;
                    Vid_player_CustomMediaController.this.mHandler.removeMessages(2);
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (Vid_player_CustomMediaController.this.mPlayer != null && z) {
                    this.newPosition = (int) ((Vid_player_CustomMediaController.this.mPlayer.getDuration() * i) / 1000);
                    this.change = true;
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (Vid_player_CustomMediaController.this.mPlayer != null) {
                    if (this.change) {
                        Vid_player_CustomMediaController.this.mPlayer.seekTo(this.newPosition);
                        if (Vid_player_CustomMediaController.this.mCurrentTime != null) {
                            Vid_player_CustomMediaController.this.mCurrentTime.setText(Vid_player_CustomMediaController.this.stringForTime(this.newPosition));
                        }
                    }
                    Vid_player_CustomMediaController.this.mDragging = false;
                    Vid_player_CustomMediaController.this.setProgress();
                    Vid_player_CustomMediaController.this.updatePausePlay();
                    Vid_player_CustomMediaController.this.show(3000);
                    Vid_player_CustomMediaController.this.mShowing = true;
                    Vid_player_CustomMediaController.this.mHandler.sendEmptyMessage(2);
                }
            }
        };
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View inflate = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.vid_player_custom_player_controller, this);
        inflate.setOnTouchListener(this.mTouchListener);
        initControllerView(inflate);
    }

    private void initControllerView(View view) {
        this.mTitleLayout = view.findViewById(R.id.title_part);
        this.mControlLayout = view.findViewById(R.id.control_layout);
        this.loadingLayout = (ViewGroup) view.findViewById(R.id.loading_layout);
        this.errorLayout = (ViewGroup) view.findViewById(R.id.error_layout);
        this.mTurnButton = (ImageButton) view.findViewById(R.id.turn_btn);
        this.mScaleButton = (ImageButton) view.findViewById(R.id.scale_btn);
        this.mCenterPlayButton = view.findViewById(R.id.center_playbtn);
        this.mBackButton = view.findViewById(R.id.back_btn);
        ImageButton imageButton = this.mTurnButton;
        if (imageButton != null) {
            imageButton.requestFocus();
            this.mTurnButton.setOnClickListener(this.mPauseListener);
        }
        if (this.mScalable) {
            ImageButton imageButton2 = this.mScaleButton;
            if (imageButton2 != null) {
                imageButton2.setVisibility(View.VISIBLE);
                this.mScaleButton.setOnClickListener(this.mScaleListener);
            }
        } else {
            ImageButton imageButton3 = this.mScaleButton;
            if (imageButton3 != null) {
                imageButton3.setVisibility(View.GONE);
            }
        }
        View view2 = this.mCenterPlayButton;
        if (view2 != null) {
            view2.setOnClickListener(this.mCenterPlayListener);
        }
        View view3 = this.mBackButton;
        if (view3 != null) {
            view3.setOnClickListener(this.mBackListener);
        }
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.seekbar);
        this.mProgress = progressBar;
        if (progressBar != null) {
            if (progressBar instanceof SeekBar) {
                ((SeekBar) progressBar).setOnSeekBarChangeListener(this.mSeekListener);
            }
            this.mProgress.setMax(1000);
        }
        this.mEndTime = (TextView) view.findViewById(R.id.duration);
        this.mCurrentTime = (TextView) view.findViewById(R.id.has_played);
        this.mTitle = (TextView) view.findViewById(R.id.title);
        this.mFormatBuilder = new StringBuilder();
        this.mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
    }

    public void setMediaPlayer(MediaPlayerControl mediaPlayerControl) {
        this.mPlayer = mediaPlayerControl;
        updatePausePlay();
    }

    public void show() {
        show(3000);
    }

    private void disableUnsupportedButtons() {
        MediaPlayerControl mediaPlayerControl;
        try {
            if (this.mTurnButton != null && (mediaPlayerControl = this.mPlayer) != null && !mediaPlayerControl.canPause()) {
                this.mTurnButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError unused) {
        }
    }

    public void show(int i) {
        if (!this.mShowing) {
            setProgress();
            ImageButton imageButton = this.mTurnButton;
            if (imageButton != null) {
                imageButton.requestFocus();
            }
            disableUnsupportedButtons();
            this.mShowing = true;
        }
        updatePausePlay();
        updateBackButton();
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        if (this.mTitleLayout.getVisibility() != View.VISIBLE) {
            this.mTitleLayout.setVisibility(View.VISIBLE);
        }
        if (this.mControlLayout.getVisibility() != View.VISIBLE) {
            this.mControlLayout.setVisibility(View.VISIBLE);
        }
        this.mHandler.sendEmptyMessage(2);
        Message obtainMessage = this.mHandler.obtainMessage(1);
        if (i != 0) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(obtainMessage, i);
        }
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void hide() {
        if (this.mShowing) {
            this.mHandler.removeMessages(2);
            this.mTitleLayout.setVisibility(View.GONE);
            this.mControlLayout.setVisibility(View.GONE);
            this.mShowing = false;
        }
    }

    public void showCenterView(int i) {
        if (i == R.id.loading_layout) {
            if (this.loadingLayout.getVisibility() != View.VISIBLE) {
                this.loadingLayout.setVisibility(View.VISIBLE);
            }
            if (this.mCenterPlayButton.getVisibility() == View.VISIBLE) {
                this.mCenterPlayButton.setVisibility(View.GONE);
            }
            if (this.errorLayout.getVisibility() == View.VISIBLE) {
                this.errorLayout.setVisibility(View.GONE);
            }
        } else if (i == R.id.center_playbtn) {
            if (this.mCenterPlayButton.getVisibility() != View.VISIBLE) {
                this.mCenterPlayButton.setVisibility(View.VISIBLE);
            }
            if (this.loadingLayout.getVisibility() == View.VISIBLE) {
                this.loadingLayout.setVisibility(View.GONE);
            }
            if (this.errorLayout.getVisibility() == View.VISIBLE) {
                this.errorLayout.setVisibility(View.GONE);
            }
        } else if (i == R.id.error_layout) {
            if (this.errorLayout.getVisibility() != View.VISIBLE) {
                this.errorLayout.setVisibility(View.VISIBLE);
            }
            if (this.mCenterPlayButton.getVisibility() == View.VISIBLE) {
                this.mCenterPlayButton.setVisibility(View.GONE);
            }
            if (this.loadingLayout.getVisibility() == View.VISIBLE) {
                this.loadingLayout.setVisibility(View.GONE);
            }
        }
    }

    public void hideCenterView() {
        if (this.mCenterPlayButton.getVisibility() == View.VISIBLE) {
            this.mCenterPlayButton.setVisibility(View.GONE);
        }
        if (this.errorLayout.getVisibility() == View.VISIBLE) {
            this.errorLayout.setVisibility(View.GONE);
        }
        if (this.loadingLayout.getVisibility() == View.VISIBLE) {
            this.loadingLayout.setVisibility(View.GONE);
        }
    }

    public void reset() {
        this.mCurrentTime.setText("00:00");
        this.mEndTime.setText("00:00");
        this.mProgress.setProgress(0);
        this.mTurnButton.setImageResource(R.drawable.uvv_player_player_btn);
        setVisibility(View.VISIBLE);
        hideLoading();
    }

    public String stringForTime(int i) {
        int i2 = i / 1000;
        int i3 = i2 % 60;
        int i4 = (i2 / 60) % 60;
        int i5 = i2 / 3600;
        this.mFormatBuilder.setLength(0);
        return i5 > 0 ? this.mFormatter.format("%d:%02d:%02d", Integer.valueOf(i5), Integer.valueOf(i4), Integer.valueOf(i3)).toString() : this.mFormatter.format("%02d:%02d", Integer.valueOf(i4), Integer.valueOf(i3)).toString();
    }

    public int setProgress() {
        MediaPlayerControl mediaPlayerControl = this.mPlayer;
        if (mediaPlayerControl == null || this.mDragging) {
            return 0;
        }
        int currentPosition = mediaPlayerControl.getCurrentPosition();
        int duration = this.mPlayer.getDuration();
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            if (duration > 0) {
                progressBar.setProgress((int) ((currentPosition * 1000) / duration));
            }
            this.mProgress.setSecondaryProgress(this.mPlayer.getBufferPercentage() * 10);
        }
        TextView textView = this.mEndTime;
        if (textView != null) {
            textView.setText(stringForTime(duration));
        }
        TextView textView2 = this.mCurrentTime;
        if (textView2 != null) {
            textView2.setText(stringForTime(currentPosition));
        }
        return currentPosition;
    }

    @Override 
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            show(0);
            this.handled = false;
        } else if (action != 1) {
            if (action == 3) {
                hide();
            }
        } else if (!this.handled) {
            this.handled = false;
            show(3000);
        }
        return true;
    }

    @Override 
    public boolean onTrackballEvent(MotionEvent motionEvent) {
        show(3000);
        return false;
    }

    @Override 
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        boolean z = keyEvent.getRepeatCount() == 0 && keyEvent.getAction() == 0;
        if (keyCode == 79 || keyCode == 85 || keyCode == 62) {
            if (z) {
                doPauseResume();
                show(3000);
                ImageButton imageButton = this.mTurnButton;
                if (imageButton != null) {
                    imageButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == 126) {
            if (z && !this.mPlayer.isPlaying()) {
                this.mPlayer.start();
                updatePausePlay();
                show(3000);
            }
            return true;
        } else if (keyCode == 86 || keyCode == 127) {
            if (z && this.mPlayer.isPlaying()) {
                this.mPlayer.pause();
                updatePausePlay();
                show(3000);
            }
            return true;
        } else if (keyCode == 25 || keyCode == 24 || keyCode == 164 || keyCode == 27) {
            return super.dispatchKeyEvent(keyEvent);
        } else {
            if (keyCode == 4 || keyCode == 82) {
                if (z) {
                    hide();
                }
                return true;
            }
            show(3000);
            return super.dispatchKeyEvent(keyEvent);
        }
    }

    public void updatePausePlay() {
        MediaPlayerControl mediaPlayerControl = this.mPlayer;
        if (mediaPlayerControl == null || !mediaPlayerControl.isPlaying()) {
            this.mTurnButton.setImageResource(R.drawable.uvv_player_player_btn);
        } else {
            this.mTurnButton.setImageResource(R.drawable.uvv_stop_btn);
        }
    }

    void updateScaleButton() {
        if (this.mIsFullScreen) {
            this.mScaleButton.setImageResource(R.drawable.uvv_star_zoom_in);
        } else {
            this.mScaleButton.setImageResource(R.drawable.uvv_player_scale_btn);
        }
    }

    public void toggleButtons(boolean z) {
        this.mIsFullScreen = z;
        updateScaleButton();
        updateBackButton();
    }

    void updateBackButton() {
        this.mBackButton.setVisibility(this.mIsFullScreen ? View.VISIBLE : View.INVISIBLE);
    }

    boolean isFullScreen() {
        return this.mIsFullScreen;
    }

    public void doPauseResume() {
        if (this.mPlayer.isPlaying()) {
            this.mPlayer.pause();
        } else {
            this.mPlayer.start();
        }
        updatePausePlay();
    }

    @Override 
    public void setEnabled(boolean z) {
        ImageButton imageButton = this.mTurnButton;
        if (imageButton != null) {
            imageButton.setEnabled(z);
        }
        ProgressBar progressBar = this.mProgress;
        if (progressBar != null) {
            progressBar.setEnabled(z);
        }
        if (this.mScalable) {
            this.mScaleButton.setEnabled(z);
        }
        this.mBackButton.setEnabled(true);
    }

    public void showLoading() {
        this.mHandler.sendEmptyMessage(3);
    }

    public void hideLoading() {
        this.mHandler.sendEmptyMessage(4);
    }

    public void showError() {
        this.mHandler.sendEmptyMessage(5);
    }

    public void hideError() {
        this.mHandler.sendEmptyMessage(6);
    }

    public void showComplete() {
        this.mHandler.sendEmptyMessage(7);
    }

    public void hideComplete() {
        this.mHandler.sendEmptyMessage(8);
    }

    public void setTitle(String str) {
        this.mTitle.setText(str);
    }

    public void setFullscreenEnabled(boolean z) {
        this.mFullscreenEnabled = z;
        this.mScaleButton.setVisibility(this.mIsFullScreen ?
                View.VISIBLE :
                View.GONE);
    }

    public void setOnErrorView(int i) {
        this.errorLayout.removeAllViews();
        LayoutInflater.from(this.mContext).inflate(i, this.errorLayout, true);
    }

    public void setOnErrorView(View view) {
        this.errorLayout.removeAllViews();
        this.errorLayout.addView(view);
    }

    public void setOnLoadingView(int i) {
        this.loadingLayout.removeAllViews();
        LayoutInflater.from(this.mContext).inflate(i, this.loadingLayout, true);
    }

    public void setOnLoadingView(View view) {
        this.loadingLayout.removeAllViews();
        this.loadingLayout.addView(view);
    }

    public void setOnErrorViewClick(OnClickListener onClickListener) {
        this.errorLayout.setOnClickListener(onClickListener);
    }

    public interface MediaPlayerControl {
        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        void closePlayer();

        int getBufferPercentage();

        int getCurrentPosition();

        int getDuration();

        boolean isPlaying();

        void pause();

        void seekTo(int i);

        void setFullscreen(boolean z);

        void setFullscreen(boolean z, int i);

        void start();
    }
}
