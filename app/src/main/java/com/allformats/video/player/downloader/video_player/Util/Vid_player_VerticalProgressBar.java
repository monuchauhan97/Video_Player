package com.allformats.video.player.downloader.video_player.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

public class Vid_player_VerticalProgressBar extends ProgressBar {
    private int w;
    private int x;
    private int y;
    private int z;

    @Override 
    public void drawableStateChanged() {
        super.drawableStateChanged();
    }

    public Vid_player_VerticalProgressBar(Context context) {
        super(context);
    }

    public Vid_player_VerticalProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public Vid_player_VerticalProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override 
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i2, i, i4, i3);
        this.x = i;
        this.y = i2;
        this.z = i3;
        this.w = i4;
    }

    @Override 
    public synchronized void onMeasure(int i, int i2) {
        super.onMeasure(i2, i);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override 
    public void onDraw(Canvas canvas) {
        canvas.rotate(-90.0f);
        canvas.translate(-getHeight(), 0.0f);
        super.onDraw(canvas);
    }

    @Override 
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            setSelected(true);
            setPressed(true);
        } else if (action == 1) {
            setSelected(false);
            setPressed(false);
        } else if (action == 2) {
            setProgress(getMax() - ((int) ((getMax() * motionEvent.getY()) / getHeight())));
            onSizeChanged(getWidth(), getHeight(), 0, 0);
        }
        return true;
    }

    @Override // android.widget.ProgressBar
    public synchronized void setProgress(int i) {
        if (i >= 0) {
            super.setProgress(i);
        } else {
            super.setProgress(0);
        }
        onSizeChanged(this.x, this.y, this.z, this.w);
    }
}
