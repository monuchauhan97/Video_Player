package com.allformats.video.player.downloader.privatevideobrowser.webFragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;


public class Vid_player_TouchableWebView extends WebView implements View.OnTouchListener {
    private float clickX;
    private float clickY;

    public Vid_player_TouchableWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnTouchListener(this);
    }

    @Override 
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0) {
            return false;
        }
        this.clickX = motionEvent.getX();
        this.clickY = motionEvent.getY();
        return false;
    }

    public float getClickX() {
        return this.clickX;
    }

    public float getClickY() {
        return this.clickY;
    }
}
