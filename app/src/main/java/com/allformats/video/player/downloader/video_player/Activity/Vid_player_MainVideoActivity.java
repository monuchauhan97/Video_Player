package com.allformats.video.player.downloader.video_player.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentTransaction;

import com.allformats.video.player.downloader.R;
import com.allformats.video.player.downloader.video_player.Fragment.Vid_player_VideoFragment;
import com.allformats.video.player.downloader.privatevideobrowser.insta_downloader.Models.Vid_player_MyEvent;

import plugin.adsdk.service.BaseActivity;

public class Vid_player_MainVideoActivity extends BaseActivity {
    ImageView refresh;

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vid_player_activity_main_video);
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new Vid_player_VideoFragment(), "fragment").setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }
        this.refresh = (ImageView) findViewById(R.id.refresh);
        this.refresh.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                Vid_player_MainVideoActivity.lambda$onCreate$0(view);
            }
        });
    }

    public static void lambda$onCreate$0(View view) {
        Vid_player_MyEvent eventBus = new Vid_player_MyEvent();
        eventBus.setType(1);
        eventBus.setValue(0);
        org.greenrobot.eventbus.EventBus.getDefault().post(eventBus);
    }

}
