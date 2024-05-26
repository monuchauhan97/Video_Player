package com.allformats.video.player.downloader.privatevideobrowser.calcyVideoView;

import android.content.Context;
import android.util.Log;
import android.view.OrientationEventListener;


public class Vid_player_OrientationDetector {
    private static final int HOLDING_THRESHOLD = 1500;
    private static final String TAG = "OrientationDetector";
    private Context context;
    private OrientationChangeListener listener;
    private OrientationEventListener orientationEventListener;
    private int rotationThreshold = 20;
    private long holdingTime = 0;
    private long lastCalcTime = 0;
    private Direction lastDirection = Direction.PORTRAIT;
    private int currentOrientation = 1;

    
    public enum Direction {
        PORTRAIT,
        REVERSE_PORTRAIT,
        LANDSCAPE,
        REVERSE_LANDSCAPE
    }

    
    public interface OrientationChangeListener {
        void onOrientationChanged(int i, Direction direction);
    }

    public Vid_player_OrientationDetector(Context context) {
        this.context = context;
    }

    public void setOrientationChangeListener(OrientationChangeListener orientationChangeListener) {
        this.listener = orientationChangeListener;
    }

    public void enable() {
        if (this.orientationEventListener == null) {
            this.orientationEventListener = new OrientationEventListener(this.context, 2) { // from class: com.allformats.video.player.downloader.privatevideobrowser.calcyVideoView.Vid_player_OrientationDetector.1
                @Override // android.view.OrientationEventListener
                public void onOrientationChanged(int i) {
                    Direction calcDirection = Vid_player_OrientationDetector.this.calcDirection(i);
                    if (calcDirection != null) {
                        if (calcDirection != Vid_player_OrientationDetector.this.lastDirection) {
                            Vid_player_OrientationDetector.this.resetTime();
                            Vid_player_OrientationDetector.this.lastDirection = calcDirection;
                            return;
                        }
                        Vid_player_OrientationDetector.this.calcHoldingTime();
                        if (Vid_player_OrientationDetector.this.holdingTime <= 1500) {
                            return;
                        }
                        if (calcDirection == Direction.LANDSCAPE) {
                            if (Vid_player_OrientationDetector.this.currentOrientation != 0) {
                                Log.d(Vid_player_OrientationDetector.TAG, "switch to SCREEN_ORIENTATION_LANDSCAPE");
                                Vid_player_OrientationDetector.this.currentOrientation = 0;
                                if (Vid_player_OrientationDetector.this.listener != null) {
                                    Vid_player_OrientationDetector.this.listener.onOrientationChanged(0, calcDirection);
                                }
                            }
                        } else if (calcDirection == Direction.PORTRAIT) {
                            if (Vid_player_OrientationDetector.this.currentOrientation != 1) {
                                Log.d(Vid_player_OrientationDetector.TAG, "switch to SCREEN_ORIENTATION_PORTRAIT");
                                Vid_player_OrientationDetector.this.currentOrientation = 1;
                                if (Vid_player_OrientationDetector.this.listener != null) {
                                    Vid_player_OrientationDetector.this.listener.onOrientationChanged(1, calcDirection);
                                }
                            }
                        } else if (calcDirection == Direction.REVERSE_PORTRAIT) {
                            if (Vid_player_OrientationDetector.this.currentOrientation != 9) {
                                Log.d(Vid_player_OrientationDetector.TAG, "switch to SCREEN_ORIENTATION_REVERSE_PORTRAIT");
                                Vid_player_OrientationDetector.this.currentOrientation = 9;
                                if (Vid_player_OrientationDetector.this.listener != null) {
                                    Vid_player_OrientationDetector.this.listener.onOrientationChanged(9, calcDirection);
                                }
                            }
                        } else if (calcDirection == Direction.REVERSE_LANDSCAPE && Vid_player_OrientationDetector.this.currentOrientation != 8) {
                            Log.d(Vid_player_OrientationDetector.TAG, "switch to SCREEN_ORIENTATION_REVERSE_LANDSCAPE");
                            Vid_player_OrientationDetector.this.currentOrientation = 8;
                            if (Vid_player_OrientationDetector.this.listener != null) {
                                Vid_player_OrientationDetector.this.listener.onOrientationChanged(8, calcDirection);
                            }
                        }
                    }
                }
            };
        }
        this.orientationEventListener.enable();
    }

    
    public void calcHoldingTime() {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.lastCalcTime == 0) {
            this.lastCalcTime = currentTimeMillis;
        }
        this.holdingTime += currentTimeMillis - this.lastCalcTime;
        this.lastCalcTime = currentTimeMillis;
    }

    
    public void resetTime() {
        this.lastCalcTime = 0L;
        this.holdingTime = 0L;
    }

    
    public Direction calcDirection(int i) {
        int i2 = this.rotationThreshold;
        if (i <= i2 || i >= 360 - i2) {
            return Direction.PORTRAIT;
        }
        if (Math.abs(i - 180) <= this.rotationThreshold) {
            return Direction.REVERSE_PORTRAIT;
        }
        if (Math.abs(i - 90) <= this.rotationThreshold) {
            return Direction.REVERSE_LANDSCAPE;
        }
        if (Math.abs(i - 270) <= this.rotationThreshold) {
            return Direction.LANDSCAPE;
        }
        return null;
    }

    public void setInitialDirection(Direction direction) {
        this.lastDirection = direction;
    }

    public void disable() {
        OrientationEventListener orientationEventListener = this.orientationEventListener;
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
    }

    public void setThresholdDegree(int i) {
        this.rotationThreshold = i;
    }
}
