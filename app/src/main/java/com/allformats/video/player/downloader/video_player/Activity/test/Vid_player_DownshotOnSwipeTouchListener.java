package com.allformats.video.player.downloader.video_player.Activity.test;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.allformats.video.player.downloader.R;

import java.util.concurrent.TimeUnit;

public class Vid_player_DownshotOnSwipeTouchListener implements View.OnTouchListener {
    final AudioManager audioManager;
    public final Context context;
    final GestureDetector gestureDetector;
    final SimpleExoPlayer player;
    public final PlayerView playerView;


    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        static final int SWIPE_THRESHOLD = 50;
        static final int SWIPE_VELOCITY_THRESHOLD = 50;

        GestureListener() {
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            try {
                float y = motionEvent2.getY() - motionEvent.getY();
                float x = motionEvent2.getX() - motionEvent.getX();
                if (Math.abs(x) > Math.abs(y)) {
                    if (Math.abs(x) > 50.0f && Math.abs(f) > 50.0f) {
                        if (x > 0.0f) {
                            if (Vid_player_DownshotPreferenceUtil.getInstance(Vid_player_DownshotOnSwipeTouchListener.this.context).getLock()) {
                                return true;
                            }
                            Vid_player_DownshotOnSwipeTouchListener.this.onSwipeRight(Math.abs(x));
                            return true;
                        } else if (Vid_player_DownshotPreferenceUtil.getInstance(Vid_player_DownshotOnSwipeTouchListener.this.context).getLock()) {
                            return true;
                        } else {
                            Vid_player_DownshotOnSwipeTouchListener.this.onSwipeLeft(Math.abs(x));
                        }
                    }
                    return true;
                }
                if (Math.abs(y) > 50.0f && Math.abs(f2) > 50.0f) {
                    if (motionEvent.getX() > Vid_player_DownshotOnSwipeTouchListener.this.playerView.getRootView().getWidth() / 2) {
                        if (y > 0.0f) {
                            if (Vid_player_DownshotPreferenceUtil.getInstance(Vid_player_DownshotOnSwipeTouchListener.this.context).getLock()) {
                                return true;
                            }
                            Vid_player_DownshotOnSwipeTouchListener.this.volumeDown(Math.abs(y));
                            return true;
                        } else if (Vid_player_DownshotPreferenceUtil.getInstance(Vid_player_DownshotOnSwipeTouchListener.this.context).getLock()) {
                            return true;
                        } else {
                            Vid_player_DownshotOnSwipeTouchListener.this.volumeUp(Math.abs(y));
                            return true;
                        }
                    } else if (y > 0.0f) {
                        if (Vid_player_DownshotPreferenceUtil.getInstance(Vid_player_DownshotOnSwipeTouchListener.this.context).getLock()) {
                            return true;
                        }
                        Vid_player_DownshotOnSwipeTouchListener.this.brightnessDown(Math.abs(y));
                        return true;
                    } else if (Vid_player_DownshotPreferenceUtil.getInstance(Vid_player_DownshotOnSwipeTouchListener.this.context).getLock()) {
                        return true;
                    } else {
                        Vid_player_DownshotOnSwipeTouchListener.this.brightnessUp(Math.abs(y));
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
    }

    public Vid_player_DownshotOnSwipeTouchListener(Context context, SimpleExoPlayer simpleExoPlayer, PlayerView playerView, AudioManager audioManager) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.player = simpleExoPlayer;
        this.playerView = playerView;
        this.audioManager = audioManager;
        this.context = context;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    public void onSwipeRight(float f) {
        TextView textView = (TextView) ((Activity) this.context).findViewById(R.id.text);
        if (this.player.getPlayWhenReady()) {
            this.player.setPlayWhenReady(false);
            SimpleExoPlayer simpleExoPlayer = this.player;
            simpleExoPlayer.seekTo((long) (((float) simpleExoPlayer.getCurrentPosition()) + (Math.abs(f) * 60.0f)));
            this.player.setPlayWhenReady(true);
            textView.setText(duration(Long.valueOf(this.player.getCurrentPosition())));
            textView.setVisibility(View.VISIBLE);
            setIn(textView);
            return;
        }
        SimpleExoPlayer simpleExoPlayer2 = this.player;
        simpleExoPlayer2.seekTo((long) (((float) simpleExoPlayer2.getCurrentPosition()) + (Math.abs(f) * 60.0f)));
        textView.setText(duration(Long.valueOf(this.player.getCurrentPosition())));
        textView.setVisibility(View.VISIBLE);
        setIn(textView);
    }

    public void onSwipeLeft(float f) {
        TextView textView = (TextView) ((Activity) this.context).findViewById(R.id.text);
        if (this.player.getPlayWhenReady()) {
            this.player.setPlayWhenReady(false);
            SimpleExoPlayer simpleExoPlayer = this.player;
            simpleExoPlayer.seekTo((long) (((float) simpleExoPlayer.getCurrentPosition()) - (Math.abs(f) * 60.0f)));
            this.player.setPlayWhenReady(true);
            textView.setText(duration(Long.valueOf(this.player.getCurrentPosition())));
            textView.setVisibility(View.VISIBLE);
            setIn(textView);
            return;
        }
        SimpleExoPlayer simpleExoPlayer2 = this.player;
        simpleExoPlayer2.seekTo((long) (((float) simpleExoPlayer2.getCurrentPosition()) - (Math.abs(f) * 60.0f)));
        textView.setText(duration(Long.valueOf(this.player.getCurrentPosition())));
        textView.setVisibility(View.VISIBLE);
        setIn(textView);
    }

    public void brightnessUp(float f) {
        WindowManager.LayoutParams attributes = ((Activity) this.context).getWindow().getAttributes();
        TextView textView = (TextView) ((Activity) this.context).findViewById(R.id.text);
        float lastBrightness = Vid_player_DownshotPreferenceUtil.getInstance(this.context).getLastBrightness() + (f / 1000.0f);
        if (lastBrightness < 1.0f) {
            attributes.screenBrightness = lastBrightness;
            ((Activity) this.context).getWindow().setAttributes(attributes);
            Vid_player_DownshotPreferenceUtil.getInstance(this.context).saveLastBrightness(attributes.screenBrightness);
            textView.setText(String.format("Brightness:%d%%", Integer.valueOf((int) Math.floor(attributes.screenBrightness * 100.0f))));
            textView.setVisibility(View.VISIBLE);
            setIn(textView);
            return;
        }
        attributes.screenBrightness = 1.0f;
        ((Activity) this.context).getWindow().setAttributes(attributes);
        Vid_player_DownshotPreferenceUtil.getInstance(this.context).saveLastBrightness(attributes.screenBrightness);
        textView.setText(String.format("Brightness:%d%%", Integer.valueOf((int) Math.floor(attributes.screenBrightness * 100.0f))));
        textView.setVisibility(View.VISIBLE);
        setIn(textView);
    }

    public void brightnessDown(float f) {
        WindowManager.LayoutParams attributes = ((Activity) this.context).getWindow().getAttributes();
        TextView textView = (TextView) ((Activity) this.context).findViewById(R.id.text);
        float lastBrightness = Vid_player_DownshotPreferenceUtil.getInstance(this.context).getLastBrightness() - (f / 1000.0f);
        if (lastBrightness > 0.0f) {
            attributes.screenBrightness = lastBrightness;
            ((Activity) this.context).getWindow().setAttributes(attributes);
            Vid_player_DownshotPreferenceUtil.getInstance(this.context).saveLastBrightness(attributes.screenBrightness);
            textView.setText(String.format("Brightness:%d%%", Integer.valueOf((int) Math.floor(attributes.screenBrightness * 100.0f))));
            textView.setVisibility(View.VISIBLE);
            setIn(textView);
            return;
        }
        attributes.screenBrightness = 0.0f;
        ((Activity) this.context).getWindow().setAttributes(attributes);
        Vid_player_DownshotPreferenceUtil.getInstance(this.context).saveLastBrightness(attributes.screenBrightness);
        textView.setText(String.format("Brightness:%d%%", Integer.valueOf((int) Math.floor(attributes.screenBrightness * 100.0f))));
        textView.setVisibility(View.VISIBLE);
        setIn(textView);
    }

    private void setIn(final TextView textView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public final void run() {
                textView.setVisibility(View.INVISIBLE);
            }
        }, 1500L);
    }

    public void volumeUp(float f) {
        int streamVolume = this.audioManager.getStreamVolume(3);
        int streamMaxVolume = this.audioManager.getStreamMaxVolume(3);
        TextView textView = (TextView) ((Activity) this.context).findViewById(R.id.text);
        int i = ((int) (f / 100.0f)) + streamVolume;
        if (i < streamMaxVolume) {
            this.audioManager.setStreamVolume(3, i, 0);
            textView.setText(String.format("Volume:%d", Integer.valueOf(i)));
            textView.setVisibility(View.VISIBLE);
            setIn(textView);
            return;
        }
        this.audioManager.setStreamVolume(3, streamMaxVolume, 0);
        textView.setText(String.format("Volume:%d", Integer.valueOf(streamMaxVolume)));
        textView.setVisibility(View.VISIBLE);
        setIn(textView);
    }

    public void volumeDown(float f) {
        TextView textView = (TextView) ((Activity) this.context).findViewById(R.id.text);
        int streamVolume = this.audioManager.getStreamVolume(3) - ((int) (f / 100.0f));
        if (streamVolume > 0) {
            this.audioManager.setStreamVolume(3, streamVolume, 0);
            textView.setText(String.format("Volume:%d", Integer.valueOf(streamVolume)));
            textView.setVisibility(View.VISIBLE);
            setIn(textView);
            return;
        }
        this.audioManager.setStreamVolume(3, 0, 0);
        textView.setText("Volume:0");
        textView.setVisibility(View.VISIBLE);
        setIn(textView);
    }

    private String duration(Long l) {
        long hours = TimeUnit.MILLISECONDS.toHours(l.longValue());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(l.longValue()) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l.longValue()));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(l.longValue()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l.longValue()));
        return hours >= 1 ? String.format("%02d:%02d:%02d", Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds)) : String.format("%02d:%02d", Long.valueOf(minutes), Long.valueOf(seconds));
    }
}
