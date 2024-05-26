package com.allformats.video.player.downloader.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class Vid_player_NormalTextView extends AppCompatTextView {
    public Vid_player_NormalTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public Vid_player_NormalTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public Vid_player_NormalTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Ubuntu-Regular.ttf"), 1);
    }
}
