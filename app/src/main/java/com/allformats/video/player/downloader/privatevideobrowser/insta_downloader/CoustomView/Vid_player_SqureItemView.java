package com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.CoustomView;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class Vid_player_SqureItemView extends AppCompatImageView {
    public Vid_player_SqureItemView(Context context) {
        super(context);
    }

    public Vid_player_SqureItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Vid_player_SqureItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override
    protected void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        if (mode == 1073741824 && mode2 != 1073741824) {
            setMeasuredDimension(size, size);
        } else if (mode2 != 1073741824 || mode == 1073741824) {
            super.onMeasure(i, i2);
        } else {
            setMeasuredDimension(size2, size2);
        }
    }
}
