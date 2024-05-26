package com.allformats.video.player.downloader.video_player.Extra;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;


public class Vid_player_TextreView extends TextureView {
    protected static final String TAG = "JZResizeTextureView";
    public int currentVideoHeight;
    public int currentVideoWidth;

    public Vid_player_TextreView(Context context) {
        super(context);
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
    }

    public Vid_player_TextreView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
        this.currentVideoWidth = 0;
        this.currentVideoHeight = 0;
    }

    public void setVideoSize(int i, int i2) {
        if (this.currentVideoWidth != i || this.currentVideoHeight != i2) {
            this.currentVideoWidth = i;
            this.currentVideoHeight = i2;
            requestLayout();
        }
    }

    @Override 
    public void setRotation(float f) {
        if (f != getRotation()) {
            super.setRotation(f);
            requestLayout();
        }
    }

    @Override 
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        Log.i(TAG, "onMeasure  [" + hashCode() + "] ");
        int rotation = (int) getRotation();
        int i5 = this.currentVideoWidth;
        int i6 = this.currentVideoHeight;
        int measuredHeight = ((View) getParent()).getMeasuredHeight();
        int measuredWidth = ((View) getParent()).getMeasuredWidth();
        if (!(measuredWidth == 0 || measuredHeight == 0 || i5 == 0 || i6 == 0 || Vid_player_Video.VIDEO_IMAGE_DISPLAY_TYPE != 1)) {
            if (rotation == 90 || rotation == 270) {
                measuredWidth = measuredHeight;
            }
            i6 = (i5 * measuredHeight) / measuredWidth;
        }
        if (rotation == 90 || rotation == 270) {
            i3 = i;
            i4 = i2;
        } else {
            i4 = i;
            i3 = i2;
        }
        int defaultSize = getDefaultSize(i5, i4);
        int defaultSize2 = getDefaultSize(i6, i3);
        if (i5 > 0 && i6 > 0) {
            int mode = MeasureSpec.getMode(i4);
            int size = MeasureSpec.getSize(i4);
            int mode2 = MeasureSpec.getMode(i3);
            int size2 = MeasureSpec.getSize(i3);
            Log.i(TAG, "widthMeasureSpec  [" + MeasureSpec.toString(i4) + "]");
            Log.i(TAG, "heightMeasureSpec [" + MeasureSpec.toString(i3) + "]");
            defaultSize2 = 0;
            if (mode == 1073741824 && mode2 == 1073741824) {
                int i7 = size2 * i5;
                int i8 = size * i6;
                if (i7 < i8) {
                    int i9 = i7 / i6;
                } else if (i7 > i8) {
                    defaultSize2 = i8 / i5;
                }
            } else if (mode == 1073741824) {
                defaultSize2 = (size * i6) / i5;
                if (mode2 == Integer.MIN_VALUE && defaultSize2 > size2) {
                    int i10 = (size2 * i5) / i6;
                }
            } else if (mode2 == 1073741824) {
                int i11 = (size2 * i5) / i6;
                if (mode == Integer.MIN_VALUE && i11 > size) {
                    defaultSize2 = (size * i6) / i5;
                }
            } else {
                int i12 = (mode2 != Integer.MIN_VALUE || i6 <= size2) ? i5 : (size2 * i5) / i6;
                if (mode == Integer.MIN_VALUE && i12 > size) {
                    int i13 = (size * i6) / i5;
                }
            }
            defaultSize = size;
        }
        if (!(measuredWidth == 0 || measuredHeight == 0 || i5 == 0 || i6 == 0)) {
            if (Vid_player_Video.VIDEO_IMAGE_DISPLAY_TYPE != 3 && Vid_player_Video.VIDEO_IMAGE_DISPLAY_TYPE == 2) {
                if (rotation == 90 || rotation == 270) {
                    measuredWidth = measuredHeight;
                }
                double d = i6;
                double d2 = i5;
                Double.isNaN(d);
                Double.isNaN(d2);
                Double.isNaN(d);
                Double.isNaN(d2);
                Double.isNaN(d);
                Double.isNaN(d2);
                double d3 = d / d2;
                double d4 = measuredHeight;
                double d5 = measuredWidth;
                Double.isNaN(d4);
                Double.isNaN(d5);
                Double.isNaN(d4);
                Double.isNaN(d5);
                Double.isNaN(d4);
                Double.isNaN(d5);
                double d6 = d4 / d5;
                if (d3 > d6) {
                    double d7 = defaultSize;
                    Double.isNaN(d5);
                    Double.isNaN(d7);
                    Double.isNaN(d5);
                    Double.isNaN(d7);
                    Double.isNaN(d5);
                    Double.isNaN(d7);
                    double d8 = d5 / d7;
                    double d9 = defaultSize2;
                    Double.isNaN(d9);
                    Double.isNaN(d9);
                    Double.isNaN(d9);
                    measuredHeight = (int) (d8 * d9);
                    i5 = measuredWidth;
                } else if (d3 < d6) {
                    double d10 = defaultSize2;
                    Double.isNaN(d4);
                    Double.isNaN(d10);
                    Double.isNaN(d4);
                    Double.isNaN(d10);
                    Double.isNaN(d4);
                    Double.isNaN(d10);
                    double d11 = d4 / d10;
                    double d12 = defaultSize;
                    Double.isNaN(d12);
                    Double.isNaN(d12);
                    Double.isNaN(d12);
                    i5 = (int) (d11 * d12);
                }
                setMeasuredDimension(i5, measuredHeight);
            }
            setMeasuredDimension(i5, i6);
        }
        setMeasuredDimension(defaultSize, defaultSize2);
    }
}
