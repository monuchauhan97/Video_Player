package com.allformats.video.player.downloader.ds_tube_android_util.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.ds_tube_android_util.classes.Vid_player_DialogCompat;


public class Vid_player_WaitVidplayerDialog extends Vid_player_DialogCompat {
    public Vid_player_WaitVidplayerDialog(Activity activity) {
        super(activity);
    }

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_wait_dialog_layout);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }
}
